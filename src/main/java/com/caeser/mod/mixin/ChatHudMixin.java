package com.caeser.mod.mixin;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Shadow private List<ChatHudLine.Visible> visibleMessages;
    @Shadow private List<ChatHudLine> messages;

    private String lastMessageString = "";
    private int stackCount = 1;

    @org.spongepowered.asm.mixin.injection.ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), argsOnly = true)
    private Text modifyMessageForChatHeads(Text message) {
        if (!CaeserConfig.INSTANCE.chatHeads) return message;

        String rawOriginal = message.getString();
        if (rawOriginal.startsWith("<") && rawOriginal.contains("> ")) {
            if (CaeserConfig.INSTANCE.chatHeadsBeforeName) {
                return Text.literal("   ").append(message);
            } else {
                if (message.getContent() instanceof net.minecraft.text.TranslatableTextContent translatable) {
                    if (translatable.getKey().equals("chat.type.text") && translatable.getArgs().length >= 2) {
                        Object[] args = translatable.getArgs();
                        Object[] newArgs = new Object[args.length];
                        System.arraycopy(args, 0, newArgs, 0, args.length);
                        
                        Object content = newArgs[1];
                        if (content instanceof Text textContent) {
                            newArgs[1] = Text.literal("   ").append(textContent);
                        } else if (content instanceof String strContent) {
                            newArgs[1] = "   " + strContent;
                        }
                        
                        net.minecraft.text.MutableText newText = net.minecraft.text.Text.translatable(translatable.getKey(), newArgs).setStyle(message.getStyle());
                        for (Text sibling : message.getSiblings()) {
                            newText.append(sibling);
                        }
                        return newText;
                    }
                }
                
                return Text.literal(rawOriginal.replaceFirst("> ", ">   ")).setStyle(message.getStyle());
            }
        }
        return message;
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
    private void caeserInterceptAddMessage(Text message, net.minecraft.network.message.MessageSignatureData signature, net.minecraft.client.gui.hud.MessageIndicator indicator, CallbackInfo ci) {
        if (!CaeserConfig.INSTANCE.stackMessages) return;

        String rawOriginal = message.getString();
        String msgString = rawOriginal.replaceAll("§[0-9a-fk-or]", "").trim();

        System.out.println("[CaeserStacking] Original: '" + rawOriginal + "'");
        System.out.println("[CaeserStacking] Stripped: '" + msgString + "'");
        System.out.println("[CaeserStacking] LastMsg:  '" + this.lastMessageString + "'");
        System.out.println("[CaeserStacking] Match? " + msgString.equalsIgnoreCase(this.lastMessageString));

        if (msgString.equalsIgnoreCase(this.lastMessageString)) {
            this.stackCount++;
            if (this.stackCount <= CaeserConfig.INSTANCE.maxMessageStack) {
                // Cancel the original method
                ci.cancel();

                // Remove the last message from the lists completely
                if (!this.messages.isEmpty()) {
                    this.messages.remove(0); // ArrayListDeque or ArrayList
                }
                
                // Because a message can take up multiple visible lines, we must remove all visible lines that belong to the last message's creation tick.
                // We'll just remove everything that was added in the last tick if it matches, but actually, the easiest is to just clear and let it rebuild,
                // or safely remove from visibleMessages based on the most recent creationTick.
                if (!this.visibleMessages.isEmpty()) {
                    int lastTick = this.visibleMessages.get(0).addedTime();
                    this.visibleMessages.removeIf(visible -> visible.addedTime() == lastTick);
                }

                // Add the new stacked message
                Text stackedText = Text.literal("").append(message).append(" §7(" + this.stackCount + ")");
                
                // We recursively call addMessage with our new text, but temporarily disable stacking to avoid infinite loop
                CaeserConfig.INSTANCE.stackMessages = false;
                ((ChatHud)(Object)this).addMessage(stackedText, signature, indicator);
                CaeserConfig.INSTANCE.stackMessages = true;
                return;
            } else {
                this.stackCount = 1; // Loop back
            }
        } else {
            this.lastMessageString = msgString;
            this.stackCount = 1;
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;IIIZZ)V", at = @At("TAIL"))
    private void caeserRenderChatHeads(DrawContext context, TextRenderer textRenderer, int currentTick, int mouseX, int mouseY, boolean focused, boolean bl, CallbackInfo ci) {
        if (!CaeserConfig.INSTANCE.chatHeads || this.visibleMessages.isEmpty()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) return;

        double scale = client.options.getChatScale().getValue();
        int lineSpacing = client.options.getChatLineSpacing().getValue().intValue();
        int lineHeight = 9 * (lineSpacing + 1);

        context.getMatrices().pushMatrix();
        
        // Move matrix to the chat's base position at the bottom of the screen
        int windowHeight = client.getWindow().getScaledHeight();
        context.getMatrices().translate(0.0f, (float)(windowHeight - 40));
        
        context.getMatrices().scale((float)scale, (float)scale);

        int maxLines = ((ChatHud)(Object)this).getVisibleLineCount();
        int drawn = 0;

        for (int i = 0; i < this.visibleMessages.size() && drawn < maxLines; i++) {
            ChatHudLine.Visible line = this.visibleMessages.get(i);
            
            StringBuilder sb = new StringBuilder();
            line.content().accept((index, style, codePoint) -> {
                sb.appendCodePoint(codePoint);
                return true;
            });
            String rawText = sb.toString().trim();
            String playerName = null;

            if (rawText.startsWith("<")) {
                int end = rawText.indexOf(">");
                if (end > 1) playerName = rawText.substring(1, end);
            }

            if (playerName != null) {
                String[] parts = playerName.split(" ");
                String actualName = parts[parts.length - 1];

                PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(actualName);
                if (entry != null) {
                    SkinTextures skin = entry.getSkinTextures();
                    
                    int y = -((drawn + 1) * lineHeight) + 1;
                    int headSize = 8;
                    int x;
                    
                    if (CaeserConfig.INSTANCE.chatHeadsBeforeName) {
                        x = 2;
                    } else {
                        int endIdx = rawText.indexOf(">");
                        if (endIdx > 0) {
                            x = textRenderer.getWidth(rawText.substring(0, endIdx + 1)) + 2;
                        } else {
                            x = 2;
                        }
                    }
                    
                    PlayerSkinDrawer.draw(context, skin, x, y, headSize);
                }
            }
            drawn++;
        }

        context.getMatrices().popMatrix();
    }
}

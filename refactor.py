import os
import re

directory = "src/main/java/com/caeser/mod/gui"

files_to_refactor = [
    "ChatHeadsCategoryScreen.java",
    "LowFireCategoryScreen.java",
    "MotionBlurCategoryScreen.java",
    "NoFogCategoryScreen.java",
    "StackingCategoryScreen.java",
]

for filename in files_to_refactor:
    path = os.path.join(directory, filename)
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    
    # 1. Change extends Screen to extends CaeserModalScreen
    content = content.replace("extends Screen {", "extends CaeserModalScreen {")
    
    # 2. Change init() to initModal()
    content = content.replace("protected void init() {", "protected void initModal() {")
    
    # 3. Fix width/height/startX/startY calculations
    # In these files, they usually have something like:
    # int x = this.width / 2 - 100;
    # Or int startX = (this.width - panelWidth) / 2;
    content = re.sub(r'int x = this\.width / 2 - \d+;', 'int x = this.startX + 60;', content)
    content = re.sub(r'int y = \d+;', 'int y = this.startY + 45;', content)
    content = re.sub(r'int startX = .*?;', '', content)
    content = re.sub(r'int startY = .*?;', '', content)
    content = re.sub(r'int panelWidth = .*?;', '', content)
    content = re.sub(r'int panelHeight = .*?;', '', content)
    
    # 4. Remove custom render method completely since CaeserModalScreen handles it
    content = re.sub(r'@Override\s*public void render\(DrawContext context, int mouseX, int mouseY, float delta\) \{.*?\}\s*', '', content, flags=re.DOTALL)
    
    # 5. Remove close() method completely since CaeserModalScreen handles it
    content = re.sub(r'@Override\s*public void close\(\) \{.*?\}\s*', '', content, flags=re.DOTALL)
    
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)

print("Done")

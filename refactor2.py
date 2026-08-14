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
    
    # Fix super(Text...) to super(parent, Text...)
    content = re.sub(r'super\((Text\.literal\(.*?\))\);', r'super(parent, \1);', content)
    
    # Remove this.parent = parent;
    content = re.sub(r'this\.parent = parent;', '', content)
    
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)

print("Done")

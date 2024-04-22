
import os

path = "fate strange fake"

if os.path.exists(path):
    print("文件已经存在")
else:
    os.mkdir(path)

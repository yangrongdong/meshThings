import os
from urllib.request import urlopen
from urllib.request import Request
from urllib.parse import unquote

from bs4 import BeautifulSoup

def createDir(path):
    if os.path.exists(path):
        print("文件已经存在:" + path)
    else:
        os.mkdir(path)   
        print("文件创建成功:" + path) 

base_url = "https://www.wenku8.net/novel/0/347/"

url = "https://www.wenku8.net/novel/0/347/index.htm"
def getHtml(url):
    headers = {'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:125.0) Gecko/20100101 Firefox/125.0'}
    req = Request(url=url,headers = headers)
    html = urlopen(req).read().decode('gbk')
    unquote_html = unquote(html)
    return (unquote_html)
    


unquote_html = getHtml(url)
soup = BeautifulSoup(unquote_html, 'html.parser') 
# print(soup)  
# 标题
title = soup.find('div', {"id": "title"}).getText()
# print(title)
# 作者
author = soup.find('div', {"id": "info"}).getText()
# print(author)

# 章节列表
characters_html = soup.find('table')

# tds
tds = characters_html.findAll('td')


# 创建小说目录
path = "《" + title + "》" + author
createDir(path)

c_path = None

# 生成总目录 
for x in tds:
    with open(path + "/" + "目录.txt","a",encoding="utf-8") as f:
        f.write(x.getText())
        f.write("\n")

for x in tds:
    isVolume = (x.attrs['class'][0] == 'vcss')
    a = x.find('a')  
    if a == None and isVolume == False:
        continue
    if isVolume == False and c_path == None:
        print(x)
        print("章节获取错误")
        break
    if isVolume:
        c_path = path + "/" +x.getText()
        createDir(c_path)
        continue
    if a != None:
        link = a.attrs['href']
        c_url = base_url + link
        c_html = getHtml(c_url)
        soup = BeautifulSoup(c_html, 'html.parser')  
        
        c_title = soup.find('div',{"id":"title"}).getText()
        cc_path = c_path + '/' + c_title + ".txt"
        c_content = soup.find('div',{"id":"content"}).getText()
        with open(cc_path,"w",encoding="utf-8") as f:
            f.write(c_content)
        

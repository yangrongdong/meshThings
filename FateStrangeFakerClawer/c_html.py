from main import getHtml
from urllib.request import urlopen
from urllib.request import Request
from urllib.parse import unquote

from bs4 import BeautifulSoup
c_url = "https://www.wenku8.net/novel/0/347/12125.htm"

html = getHtml(c_url)

unquote_html = getHtml(c_url)
soup = BeautifulSoup(unquote_html, 'html.parser')  

title = soup.find('div',{"id":"title"}).getText()
content = soup.find('div',{"id":"content"}).getText()

print(title)
print(content)
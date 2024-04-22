import os
from urllib.request import urlopen
from urllib.request import Request
from urllib.parse import unquote

from bs4 import BeautifulSoup

character_html = '<td class="vcss" colspan="4" vid="143226">第八卷</td>'

x_html = '<td class="vcss" colspan="4" vid="12124">第一卷</td>'

soup = BeautifulSoup(x_html, 'html.parser')

a = soup.find('a')
c = soup.find('td', {"class" : "vcss"})
if(c.attrs['class'][0] == 'vcss'):
    print('1')
print(c.attrs['class'])





'''
Created on Apr 6, 2016

@author: richard
'''

from pyspark import SparkContext


myOut = ""
if __name__ == '__main__':
    sc = SparkContext("local")
    text_file = sc.textFile("/user/cloudera/socialmedia/answers_noHeader.csv")
    parts = text_file.map(lambda p: p.split(";") ) 
    parts = parts.map(lambda p: ('time',int(p[11])-int(p[4])))
    parts = parts.filter(lambda p: p[1]>0).filter(lambda p: p[1]<3600)
    myOut = parts.count()
print("================================== my test out ==========================")
print(myOut)
print("==================================== good bye ============================")
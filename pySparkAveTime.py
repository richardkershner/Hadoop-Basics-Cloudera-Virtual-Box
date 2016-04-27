'''
Created on Apr 6, 2016

@author: Richard Kershner
'''

from pyspark import SparkContext
from decimal import Decimal


myOut = ""
if __name__ == '__main__':
    sc = SparkContext("local")
    text_file = sc.textFile("/user/cloudera/socialmedia/answers_noHeader.csv")
    parts = text_file.map(lambda p: p.split(";") ) 
    totCount = parts.count()
    parts = parts.map(lambda p: ('time',Decimal(p[11])-int(p[4])))
    parts = parts.reduceByKey(lambda p,y:p+y)
    parts = parts.map(lambda p: (p[0],(p[1]/totCount)/3600))
    myOut = parts.collect()
print("================================== test out ==========================")
print(myOut)
print("==================================== good bye ============================")

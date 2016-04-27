'''
Created on Apr 6, 2016

@author: Richard Kershner
'''

from pyspark import SparkContext

myOut = ""
if __name__ == '__main__':
    sc = SparkContext("local")
    text_file = sc.textFile("/user/cloudera/socialmedia/answers_noHeader.csv")
    parts = text_file.map(lambda p: p.split(";") ) 
    parts = parts.flatMap(lambda p: (p[5].split(',')) )
    parts = parts.map(lambda p: (p,1))
    parts = parts.reduceByKey(lambda p,y:p+y)
    parts = parts.map(lambda p:(p[1],p[0]))
    parts = parts.sortByKey(True, 2)
    parts.saveAsTextFile("/user/cloudera/socialmedia/out")
    myOut = parts.top(10)
print("================================== my test out ==========================")
print(myOut)
print("==================================== good bye ============================")

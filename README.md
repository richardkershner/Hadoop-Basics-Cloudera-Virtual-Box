# Hadoop-Basics-Cloudera-Virtual-Box
This is from classroom projects for getting certified in Hadoop.  Included in this is a social media project project which can easily be used as help in other setups.  Eclipse use with Hadoop, very basic.  map Reduce, PIG and HIVE(SQL) Hadoop.
>
> Below is as follows
* Overview with answers
* map reduce.  Also reference Java files
* Pig (Pig Latin)
* HIVE (SQL)
> Note:  All 3 are the same set of problems solved in different ways.

##OVERVIEW
> First, going over the data:
**Downloaded answers.csv, deleted top row and saved as answers_noHeader.csv**
  * 263540 records in the noHeader version
  * Note fields seperated by ';' and tags field seperated by ','
  * The data as seperated by ';' can be devided into columns by the following tags.  Note, these tags might have to be altered pending on which system they are used in as certain words might be restricted for code purposes.  id int, grid int, i int, gs int, qt int, tags string, gvc int, gac int, aid int, j int, as int, at int
  * qt(question time) and at(answer time) are in epoch format.  (at - qt) / 3600 = times in minutes

The 4 Questions with Answers are processed in each of the 3 formats:  mapReduce jar(s), Pig (Pig Latin) and hive (SQL querry).  First, and last thoughts, this project is best suited for Hive, in general, as it allows for the flexibility of using all three as needed and the data is fairly organized.

**Q/A 1 - Top 10 tags used**
  * sql	9695, python	10028, subjective	12416, javascript	12589, php	12910, aspûnet	14525, c++	17445, ûnet	19509, java	20003, c#	38399


**Q/A 2 - Average time to answer quesions**
  * total questioned:	263520  --> note, this does not match total lines.  Possibility, NULLS are skipped
  * **average time** in raw epoch format:	133792
  * in seconds:	2229
  * in minutes:	37
  * in hours:	0

**Q/A 3 -  Number of questions which got answered within 1 hour**
  * 174598

**Q/A 4 - tags of questions which got answered within 1 hour**
  * Total Tags = 12288 tags
  * See document MapRed_QA4_part-r-00000  which is a text document with the answer

##MapReduce (Java)    Please see Java files to go with solutions
> **Pre-setup and loading.**
  * Data file editting and reviewing
  * Create  file answer_noHeader.csv by removing the first row.
  * open terminal window and find the files on the Cloudera Virtual Box setup
  * $ cd  /home/cloudera/Documents/dataSocialMediaExercise/
  * $ wc -l answers.csv    -->   263541 answers.csv  which matches the .csv file count 
  * $ wc -l answers_noHeader.csv  --> 263540 answers_noHeader.csv
> 
> **HDFS – setting file to Hadoop file system**
  * $ hadoop fs -mkdir socialmedia
  * $ hadoop fs -put answers_noHeader.csv socialmedia
  * $ hadoop fs -ls      -- which now shows the sub folder on cloudera
  * browse via HUE under datafiles and there is socialmedia with the files in it.  Files and uploads can also be done via the HUE file browser.
> 
> **Since I am running the jar(s) from the local machine.**
  * In terminal, change to the directory where I am copying the jar files to.
  * Note where file is used in running the Java .jar files, the full path could be used.
> 
> **Question 1 -- Top 10 tags used**
  * See hdfs_top10.java  and hdfs_top10_b.java
  * The first one creates 2 columns, tag name, tag count.
  * Second one sorts and limits to the top 10.
  * **In the Linux terminal.**
     * $ hadoop jar hdfs_top10.jar com.hdfs_top10 /user/cloudera/socialmedia/answers_noHeader.csv  /user/cloudera/socialmedia/out
        * hadoop jar --> Let's it know it is running a Java jar file in the Hadoop file system.
        * hdfs_top10.jar  --> the name of the jar file created from the Java code
        * com.hdfs_top10  --> in the Java code, this is the package name '.' and the java class
        * /user/cloudera/socialmedia/answers_noHeader.csv --> full path in the HDFS of the data.
           * This could also point to the Hive warehouse for a Hive upload.
        * /user/cloudera/socialmedia/out --> The folder *out* is created with the output data files in it.
   
   **Questions 2, 3, 4 use the same format format, except only one call each**
    * $ hadoop jar hdfs_averagetime.jar com.hdfs_averagetime /user/cloudera/socialmedia/answers_noHeader.csv /user/cloudera/socialmedia/out
    * $ hadoop jar hdfs_underHrCnt.jar com.hdfs_underHrCnt /user/cloudera/socialmedia/answers_noHeader.csv /user/cloudera/socialmedia/out
    * $ hadoop jar hdfs_tagsofqh.jar com.hdfs_tagsofqh /user/cloudera/socialmedia/answers_noHeader.csv /user/cloudera/socialmedia/out
    
   
     * Make sure to feed the answer from the first one into the second one.
     * $ hadoop jar hdfs_top10_b.jar com.hdfs_top10_b /user/cloudera/socialmedia/out/part-r-00000  /user/cloudera/socialmedia/out

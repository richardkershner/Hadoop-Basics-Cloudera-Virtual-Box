# Hadoop (Spark)-Basics-Cloudera-Virtual-Box
This is from classroom projects for getting certified as as Software Developer in Hadoop File System (HDFS).  Included is a social media project which can easily be used as help in other basic test setups.  

This covers: Cloudera Quick start on Virtual Box;  map Reduce; PIG and HIVE(SQL).  Added in are 3 very basic spark Map Reduce calls.
>
> **Index**
* Virtual Box, Cloudera quick start single node for testing
* Overview with answers
* map reduce.  Also reference Java files
* Pig (Pig Latin)
* HIVE (SQL)
> Note:  All 3 are the same set of problems solved in different ways.

##Virtual Box, Cloudera quick start notes
> Virtual Box allows running a different operating system, client, on a host computer. In this example a Windows 10 home edition has the Virtual Box setup with the disc image.  The disc image is linux Centos with Cloudera, one of the HDFS options, on it.  
> 
> In short, this creates a single node for testing and learning HDFS on which is accessable from a PC. 
> 
> Both downloads can be found from their respective sites, FREE. 
  * http://www.cloudera.com/downloads.html    -->  select Quickstarts
    * As of Aug. 18th,2016, only the older cloudera quickstart, CDH 5.7, has the VB image
  * http://www.oracle.com/technetwork/server-storage/virtualbox/downloads/index.html
> 
  * **Note in Virtual Box.**
    * Make sure to use FILE and from the pull down, IMPORT to bring the Cloudera disk image.
      * Using add button will cause errors.
    * When starting up Cloudera in Virtual Box(VB), it will go through a setup screen.  Once the initial setup is done, click the esc button on the keyboard to load the actual linux cloudera.  (Hope that saves somebody the time of looking it up!)
    * In Virtual Box, under Devices, set both shared clipboard and Drag and drop to bidirectional.
       * The Drag and Drop is great for transfering files into VB but requires extra settings to work the other way.
 > 
  *  **Note in Cloudera on the VB**
    * Terminal window easy access icon at the top of the window.
    * HUE and File manager are the main two things used in the web browser.
       * Maximize the window to see File Manager under the HUE as it is way to the right.
    * Updating the linux system in the VB was necessary for one of the problems.  This also caused changes in the windows so the auto sizing doesn't work as good, along with other changes.  This means extra scrolling when viewing different things in the VB.

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
    * *Make sure to feed the answer from the first one into the second one.*
    * $ hadoop jar hdfs_top10_b.jar com.hdfs_top10_b /user/cloudera/socialmedia/out/part-r-00000  /user/cloudera/socialmedia/out
   **Questions 2, 3, 4 use the same format format, except only one call each**
    * $ hadoop jar hdfs_averagetime.jar com.hdfs_averagetime /user/cloudera/socialmedia/answers_noHeader.csv /user/cloudera/socialmedia/out
    * $ hadoop jar hdfs_underHrCnt.jar com.hdfs_underHrCnt /user/cloudera/socialmedia/answers_noHeader.csv /user/cloudera/socialmedia/out
    * $ hadoop jar hdfs_tagsofqh.jar com.hdfs_tagsofqh /user/cloudera/socialmedia/answers_noHeader.csv /user/cloudera/socialmedia/out


##Using Pig and Pig Latin to solve the problems
> Pig is used ontop of the HDFS uploaded files as an alternative method to pull data as the mapReduce.
  * Open a terminal window
     * $ pig -x mapred;  l  --> log into shell in the hdfs system as opposed to local
     * grunt>     --> Pig terminal
  * Note the following tags are helpful for viewing data while figuring out the answer.
     * ILLUSTRATE myVariable;  --> shows random row of data
  * Note the following format returned.
     * map which shows both the key and the value   
     * tuples (value1, value2)
> 
> **QUESTION 1 -  Top 10 most commonly used tags in this data set.**
  * grunt> answers = LOAD '/user/cloudera/socialmedia/answers_noHeader.csv' USING PigStorage(';') AS (id: int, grid: int, i: int, gs: int, qt: int, tags: chararray, gvc: int, gac: int, aid: int, j: int, as: int, at: int);
  * grunt> tag_bag = foreach answers GENERATE TOKENIZE(tags);
  * grunt>  all_tags = foreach tag_bag generate FLATTEN($0) AS sing_tag:chararray;
  * grunt> ILLUSTRATE all_tags;
  * grunt> tags_grp =group all_tags BY sing_tag;which
  * grunt> tag_cnt = foreach tags_grp generate group, COUNT(all_tags) as wc_cnt;
  * grunt>  ILLUSTRATE tag_cnt;
  * grunt>tag_ordered = ORDER tag_cnt BY wc_cnt DESC;  ASC DESC
  * grunt>  ILLUSTRATE tag_ordered;  
  * grunt> no_null =  FILTER tag_ordered  BY group is not null;
  * grunt> top10 = limit no_null 10; 
> 
> **Question 2 – Average time to answer questions.**
  * grunt> answers = LOAD '/user/cloudera/socialmedia/answers_noHeader.csv' USING PigStorage(';') AS (id: int, grid: int, i: int, gs: int, qt: int, tags: chararray, gvc: int, gac: int, aid: int, j: int, as: int, at: int);
  * grunt> a_time = foreach answers GENERATE at - qt AS time:int;
  * grunt> time_grp = group a_time all;
  * grunt> time_avg = foreach time_grp GENERATE AVG(a_time.time) as tam:double;
  * grunt> time_in_minutes = foreach time_avg generate tam  / 3600 AS tm:double;
  * grunt> dump;
> 
> **Question 3 -- Number of questions which got answered within 1 hour.**
  * grunt> answers = LOAD '/user/cloudera/socialmedia/answers_noHeader.csv' USING PigStorage(';') AS (id: int, grid: int, i: int, gs: int, qt: int, tags: chararray, gvc: int, gac: int, aid: int, j: int, as: int, at: int);
  * grunt> q60 = FILTER answers BY at-qt < 3600;
  * runt> cnt   = foreach (GROUP q60 ALL) GENERATE COUNT(q60);
> 
> **Question 4 -- tags of questions which got answered within 1 hour.**
  * grunt> answers = LOAD '/user/cloudera/socialmedia/answers_noHeader.csv' USING PigStorage(';') AS (id: int, grid: int, i: int, gs: int, qt: int, tags: chararray, gvc: int, gac: int, aid: int, j: int, as: int, at: int);
  * grunt> q60 = FILTER answers BY at-qt < 3600;
  * grunt> tag_bag = foreach q60 GENERATE TOKENIZE(tags);
  * grunt>  all_tags = foreach tag_bag GENERATE FLATTEN($0) AS sing_tag:chararray;
  * grunt>  tags_grp =group all_tags BY sing_tag;
  * grunt>  tags = foreach tags_grp GENERATE group AS sing_tag:chararray;
  * grunt> cnt = foreach (GROUP tags ALL) GENERATE COUNT(tags);
  
 
## Using HIVE and HIVE SQL to solve the problem
> Hive creates it's own file setup.  This is, by default, in the HIVE Warehouse. 
 * The file is stored as a myDatabase.db folder with the tables, in this case answer, under it.
 * From the terminal window, start the Hive terminal.  This can also, in part, be done via the HUE interface.
   * $ hive    --> starts hive shell
   * hive> create table answers(id int, grid int, i int, gs int, qt int, tags string, gvc int, gac int, aid int, j int, as int, at int) ROW FORMAT DELIMITED FIELDS TERMINATED BY "\;";
   * hive> Load DATA LOCAL INPATH '/home/cloudera/Documents/dataSocialMediaExercise/answers_noHeader.csv' INTO TABLE answers;
> 
> **QUESTION 1 -  Top 10 most commonly used tags in this data set.**
  * hive> SELECT my_tag, count(*) AS cnt FROM (SELECT EXPLODE(split(tags, ',')) AS my_tag FROM answers) inner_query GROUP BY my_tag ORDER BY cnt DESC limit 10;
> 
> **Question 2 – Average time to answer questions.**
  * hive> SELECT AVG(time_sum) FROM (SELECT at - qt AS time_sum FROM answers)inner_query; 
>   
> **Question 3 -- Number of questions which got answered within 1 hour.**
  * hive> SELECT count(*) FROM (SELECT at - qt AS time FROM answers ) inner_query  WHERE time < 3600; 
>  
> **Question 4 -- tags of questions which got answered within 1 hour.**
  * hive> SELECT COUNT(*) FROM (SELECT * FROM (SELECT EXPLODE(split(tags, ',')) AS my_tag FROM (SELECT tags, at, qt FROM answers WHERE at - qt < 3600) inner_query) inner_query GROUP BY my_tag) inner_querry;
  
#PySpark#
> Basics printed out as part of the terminal output.  
> This also works with saveAsTextFile(*path*) and saveAsSequentialFile(*path*) back into the HDFS file system embedded into the .py file.
> Becuase these are basics codes, more standard formats of passing the source file and the target file should be used.
*       *import sys
*       source_file_local = sys.argv[1]
*       target_in_HDFS = sys.argv[2]*
* 
* $ pyspark pySparkAveTime.py
* $ pyspark pySparkQuestUnderOneHour.py
* $ pyspark pySparkTop10.py

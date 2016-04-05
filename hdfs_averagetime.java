package com;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


// Mapper
public class hdfs_averagetime {
	//Input key = 0, value =  ID(3); grid(563355);i(62701);gs(10); qt(1235000081); tags(php, error, gd); gvc(1047); gac(16); aid(563454); j(893); as(18); at(1234000501)
	//                        2; 563355;62701;10;1235000081;php, error;1047;2;563372;67183;18;1234000501
	//  note [4] is time of question(epoch) and [11] is time of answer(epoch)

	public static class ccMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
		Text outkey = new Text();
		IntWritable outvalue = new IntWritable();
		
		public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
			String[] splitLine = value.toString().split(";");
			String starttime = splitLine[4];
			String endtime = splitLine[11];
			int deltatime = Integer.parseInt(endtime) - Integer.parseInt(starttime);
			if (deltatime>0){
				outkey.set("time");
				outvalue.set(deltatime);
				context.write(outkey, outvalue);
			}
		}
	}
		
	//REDUCER
	//Input key = Exercise & Fitness_clarksville, value =  cash or credit
	//Output key = Exercise & Fitness_clarksville value= (100,2000,450)
	public static class ccReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
		Text keyvalue = new Text();
		IntWritable outvalue = new IntWritable();
		public void reduce(Text key,Iterable<IntWritable> values,Context context) throws IOException, InterruptedException{
			Double sum =(double) 0;
			int totQuestions =0;
			for(IntWritable value: values){
				sum = sum + value.get();
				totQuestions +=1;
			}
			//
			int average = (int)(sum/totQuestions);
			int secs = average/60;
			int mins = secs/60;
			int hrs = mins/60; 
			keyvalue.set("total questioned:");
			outvalue.set(totQuestions);
			context.write(keyvalue, outvalue);
			keyvalue.set("average in raw epoch format:");
			outvalue.set(average);
			context.write(keyvalue, outvalue);
			keyvalue.set("time in seoncds:");
			outvalue.set(secs);
			context.write(keyvalue, outvalue);
			keyvalue.set("time in minutes:");
			outvalue.set(mins);
			context.write(keyvalue, outvalue);
			keyvalue.set("time in hours:");
			outvalue.set(hrs);
			context.write(keyvalue, outvalue);
		}
	}
	

	//DRIVER
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "cash credit job");

		job.setJarByClass( hdfs_averagetime.class );
	    job.setMapperClass( ccMapper.class );
	    job.setReducerClass( ccReducer.class );
	    
	    job.setMapOutputKeyClass( Text.class );
	    job.setMapOutputValueClass( IntWritable.class );
	    
	    job.setOutputKeyClass( Text.class );	    
	    job.setOutputValueClass( IntWritable.class );
	    
	    FileInputFormat.addInputPath( job, new Path( args[0] ) );
	    FileOutputFormat.setOutputPath( job, new Path( args[1] ) );
	    
	    System.exit( job.waitForCompletion( true ) ? 0 : 1 );
	}

}

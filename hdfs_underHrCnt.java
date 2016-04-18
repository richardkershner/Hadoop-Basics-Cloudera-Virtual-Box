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
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


// Mapper
public class hdfs_underHrCnt {

	public static class ccMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
		Text outkey = new Text();
		IntWritable outvalue = new IntWritable();
		
		public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
			String[] splitLine = value.toString().split(";");
			String starttime = splitLine[4];
			String endtime = splitLine[11];
			int deltatime = Integer.parseInt(endtime) - Integer.parseInt(starttime);
			if (deltatime <3600){
				outkey.set("questionCount");
				outvalue.set(1);
				context.write(outkey, outvalue);
			}
		}
	}
		
	//REDUCER
	public static class ccReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
		Text keyvalue = new Text();
		IntWritable outvalue = new IntWritable();
		public void reduce(Text key,Iterable<IntWritable> values,Context context) throws IOException, InterruptedException{
			int count = 0;
			for(IntWritable value: values){
				count ++;
			}
			keyvalue.set("tag");
			outvalue.set(count);
			context.write(keyvalue, outvalue);
		}
	}
	

	//DRIVER
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "cash credit job");

		job.setJarByClass( hdfs_underHrCnt.class );
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

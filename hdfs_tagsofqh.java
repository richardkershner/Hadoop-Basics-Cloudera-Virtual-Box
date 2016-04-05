package com;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


// Mapper
public class hdfs_tagsofqh {

	public static class ccMapper extends Mapper<LongWritable,Text,Text,Text>{
		Text outkey = new Text();
		Text outvalue = new Text();
		
		public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
			String[] splitLine = value.toString().split(";");
			String starttime = splitLine[4];
			String endtime = splitLine[11];
			String[] tags = splitLine[5].split(",");
			int deltatime = Integer.parseInt(endtime) - Integer.parseInt(starttime);
			if (deltatime <3600){
				for (String tag : tags){
					outkey.set(tag);
					outvalue.set("-");
					context.write(outkey, outvalue);
				}
			}
		}
	}
		
	//REDUCER
	public static class ccReducer extends Reducer<Text,Text,Text,Text>{
		Text keyvalue = new Text();
		Text outvalue = new Text();
		public void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException{
				keyvalue.set("tag");
				outvalue.set(key);
				context.write(keyvalue, outvalue);
	
		}
	}
	

	//DRIVER
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "cash credit job");

		job.setJarByClass( hdfs_tagsofqh.class );
	    job.setMapperClass( ccMapper.class );
	    job.setReducerClass( ccReducer.class );
	    
	    job.setMapOutputKeyClass( Text.class );
	    job.setMapOutputValueClass( Text.class );
	    
	    job.setOutputKeyClass( Text.class );	    
	    job.setOutputValueClass( Text.class );
	    
	    FileInputFormat.addInputPath( job, new Path( args[0] ) );
	    FileOutputFormat.setOutputPath( job, new Path( args[1] ) );
	    
	    System.exit( job.waitForCompletion( true ) ? 0 : 1 );
	}

}

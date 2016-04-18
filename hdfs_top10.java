package com;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

//import com.testClass.WcMapper;
//import com.testClass.WcReducer;

// Mapper
public class hdfs_top10 {
	public static class ccMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
		Text outkey = new Text();
		IntWritable outvalue = new IntWritable();
		
		public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
			String[] tags = value.toString().split(";")[5].split(",");
			for (String tag : tags){
				outkey.set(tag);
				outvalue.set(1);
				context.write(outkey, outvalue);
			}
		}
	}
		
	//REDUCER
	public static class ccReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
		IntWritable outvalue = new IntWritable();
		public void reduce(Text key,Iterable<IntWritable> values,Context context) throws IOException, InterruptedException{
			int sum =0;
			for(IntWritable value: values){
				sum = sum + value.get();
			}
			outvalue.set(sum);
			context.write(key, outvalue);
		}
	}
	
	//Partitioner
	public static class CustomPartitioner extends Partitioner<Text, IntWritable>{

		@Override
		public int getPartition(Text key, IntWritable value, int numPartitions) {
			int index =0;
			String testString = "cash";
			if(testString.equals(key.toString())){
				index = 1;
			}
			return index;
		}
	}

	//DRIVER
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "cash credit job");

		job.setJarByClass( hdfs_top10.class );
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

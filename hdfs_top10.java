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
	//-  Top 10 most commonly used tags in this data set, column 5 tag count
	//Input key = 0, value =  ID(3); grid(563355);i(62701);gs(10); qt(1235000081); tags(php, error, gd); gvc(1047); gac(16); aid(563454); j(893); as(18); at(1234000501)
	//                        2; 563355;62701;10;1235000081;php, error;1047;2;563372;67183;18;1234000501
	//Output key = tags name (ie php), value =  1

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
	//Input key = Exercise & Fitness_clarksville, value =  cash or credit
	//Output key = Exercise & Fitness_clarksville value= (100,2000,450)
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
	    
	    //job.setNumReduceTasks(1);
	    //job.setPartitionerClass(CustomPartitioner.class);
	    
	    job.setMapOutputKeyClass( Text.class );
	    job.setMapOutputValueClass( IntWritable.class );
	    
	    job.setOutputKeyClass( Text.class );	    
	    job.setOutputValueClass( IntWritable.class );
	    
	    FileInputFormat.addInputPath( job, new Path( args[0] ) );
	    FileOutputFormat.setOutputPath( job, new Path( args[1] ) );
	    
	    System.exit( job.waitForCompletion( true ) ? 0 : 1 );
	}

}

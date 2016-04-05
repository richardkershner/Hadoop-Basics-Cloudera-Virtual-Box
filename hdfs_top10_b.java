package com;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
//import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

//import com.testClass.WcMapper;
//import com.testClass.WcReducer;

// Mapper
public class hdfs_top10_b {
	//-  Top 10 most commonly used tags in this -- takes output from all tags with counts and returns only 10
	//Input key = 0, value =  
	//                        2; 563355;62701;10;1235000081;php, error;1047;2;563372;67183;18;1234000501
	//Output key = "tags" value =  "php=1000"

	public static class ccMapper extends Mapper<LongWritable, Text, Text, Text>{
		Text outkey = new Text();
		Text outvalue = new Text();
		public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
			outkey.set("tag");
			outvalue.set(value.toString()); // hdfs \t  1432
			context.write(outkey, outvalue);
		}
	}
		
	//REDUCER
	//Input key = tag
	//Output key = totalCountt]
	public static class ccReducer extends Reducer<Text,Text,Text,Text>{
		public void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException{
			// we want all tags tag hdfs=1000
			// add hdfs to tree, and 1000  then delete.........
			TreeMap< Double, String> tags10 = new TreeMap< Double, String>();
			for(Text value: values){
				String[] v = value.toString().split("\t");
				tags10.put((double)Integer.parseInt(v[1]),v[0]);
				if (tags10.size() > 10){
					tags10.remove(tags10.firstKey());
				}
			}// end of create tags10
			Text outkey = new Text();
			Text outvalue = new Text();
			for (Double t : tags10.keySet()){
				outkey.set(tags10.get(t).toString());
				outvalue.set(Integer.toString(t.intValue()) );
				context.write(outkey, outvalue);
			}
		}
	}
	

	//DRIVER
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "hdfs_top10 part b");

		job.setJarByClass( hdfs_top10_b.class );
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

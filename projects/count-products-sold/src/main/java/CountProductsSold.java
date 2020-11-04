// Credits to https://www.guru99.com/create-your-first-hadoop-program.html.

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
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class CountProductsSold {

    public class SalesMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        private Text country = new Text();
        private IntWritable one = new IntWritable(1);

        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String valueString = value.toString();
            String[] singleCountryData = valueString.split(",");
            country.set(singleCountryData[7]);
            context.write(country, one);
        }
    }

    public class SalesReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable frequency = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int frequencyForCountry = 0;
            for (IntWritable val : values) {
                frequencyForCountry += val.get();
            }
            frequency.set(frequencyForCountry);
            context.write(key, frequency);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration config = new Configuration();
        config.set(TextOutputFormat.SEPARATOR, ",");

        Job job = Job.getInstance(config, "count products sold");
        job.setJarByClass(CountProductsSold.class);

        job.setMapperClass(SalesMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setCombinerClass(SalesReducer.class);

        job.setReducerClass(SalesReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
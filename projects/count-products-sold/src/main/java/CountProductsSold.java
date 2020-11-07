// Credits to https://www.guru99.com/create-your-first-hadoop-program.html.

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class CountProductsSold {

    public enum ProductsSold {NUM_SUMS}

    public static class SalesMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
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

    public static class SalesReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable frequency = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int frequencyForCountry = 0;
            for (IntWritable val : values) {
                frequencyForCountry += val.get();
                context.getCounter(ProductsSold.NUM_SUMS).increment(1);
            }
            frequency.set(frequencyForCountry);
            context.write(key, frequency);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
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

        TextInputFormat.addInputPath(job, new Path(args[0]));
        TextOutputFormat.setOutputPath(job, new Path(args[1]));

        int exitStatus = job.waitForCompletion(true) ? 0 : 1;

        Counters counters = job.getCounters();
        Counter sumsCounter = counters.findCounter(ProductsSold.NUM_SUMS);
        System.out.println(sumsCounter.getDisplayName() + ":" + sumsCounter.getValue());

        System.exit(exitStatus);
    }
}
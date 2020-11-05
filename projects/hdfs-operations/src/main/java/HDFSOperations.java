import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.security.PrivilegedExceptionAction;

public class HDFSOperations {
    public static void main(String[] args) throws IOException, InterruptedException {
        String hdfsUrl = "hdfs://hadoop-namenode:9000";
        Configuration configuration = new Configuration();

        String username = "hadoop";
        UserGroupInformation ugi = UserGroupInformation.createRemoteUser(username);
        ugi.doAs(new PrivilegedExceptionAction<Void>() {
            public Void run() throws Exception {

                String text = "Hello, this is sample text to be written to a HDFS file.";
                FileSystem fs = FileSystem.get(new URI(hdfsUrl), configuration);

                OutputStream outputStream = fs.create(new Path("/data/example-operation-java.txt"));

                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(outputStream));
                br.write(text);
                br.close();
                return null;
            }
        });
    }
}
#!/bin/bash

hdfs dfs -rm -r /data/word-counting
hdfs dfs -mkdir /data/word-counting
yes "two words" | head -n 100000000 > large-file
ls -lh large-file
echo "Copying file to Hadoop..."
hdfs dfs -put large-file /data/word-counting/
hdfs dfs -ls /data/word-counting/
rm large-file

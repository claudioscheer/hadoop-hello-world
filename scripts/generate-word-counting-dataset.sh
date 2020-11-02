#!/bin/bash

hadoop fs -rm -r large-file /data/word-counting
hadoop fs -mkdir large-file /data/word-counting
yes "two words" | head -n 100000000 > large-file
ls -lh large-file
echo "Copying file to Hadoop..."
hadoop fs -put large-file /data/word-counting/
hadoop fs -ls /data/word-counting/
rm large-file

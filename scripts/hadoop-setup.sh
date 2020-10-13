#!/bin/bash

apt-get install openjdk-8-jdk -y
echo "export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64" >> ~/.bashrc
source ~/.bashrc

apt-get install ssh pdsh -y

wget https://downloads.apache.org/hadoop/common/hadoop-3.3.0/hadoop-3.3.0.tar.gz -O hadoop.tar.gz
tar -xvf hadoop.tar.gz
rm hadoop.tar.gz

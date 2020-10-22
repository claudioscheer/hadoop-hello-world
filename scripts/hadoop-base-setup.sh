#!/bin/bash

sudo apt-get install openjdk-8-jdk -y
sudo apt-get install ssh pdsh neovim git openssh-server -y

ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
chmod 0600 ~/.ssh/authorized_keys

wget https://downloads.apache.org/hadoop/common/hadoop-3.3.0/hadoop-3.3.0.tar.gz -O $HOME/hadoop.tar.gz
tar -xvf $HOME/hadoop.tar.gz -C $HOME
rm $HOME/hadoop.tar.gz

cat >> $HOME/~.bashrc << EOF

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export HADOOP_HOME=$HOME/hadoop-3.3.0
export HADOOP_INSTALL=$HADOOP_HOME
export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_COMMON_HOME=$HADOOP_HOME
export HADOOP_HDFS_HOME=$HADOOP_HOME
export YARN_HOME=$HADOOP_HOME
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
export PATH=$PATH:$HADOOP_HOME/sbin:$HADOOP_HOME/bin
export PDSH_RCMD_TYPE=ssh
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib/native"
EOF
source $HOME/.bashrc

cat >> $HOME/hadoop-3.3.0/etc/hadoop/hadoop-env.sh << EOF

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
EOF

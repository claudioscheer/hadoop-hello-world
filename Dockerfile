FROM ubuntu:latest

RUN apt-get update
RUN apt-get upgrade -y

COPY ./hadoop-setup.sh /home
RUN chmod +x /home/hadoop-setup.sh
RUN /home/hadoop-setup.sh
RUN rm /home/hadoop-setup.sh

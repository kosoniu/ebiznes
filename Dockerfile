FROM ubuntu:18.04

RUN apt-get update && apt-get upgrade

RUN apt-get install -y wget yum dpkg

# INSTALL JAVA, NODEJS, NPM
RUN apt-get install -y openjdk-8-jdk nodejs npm

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME

## INSTALL SBT
RUN wget www.scala-lang.org/files/archive/scala-2.12.13.deb
RUN dpkg -i scala*.deb


FROM ubuntu:18.04

RUN apt update && apt install -y build-essential unzip git curl wget zip

RUN apt-get install -y wget yum dpkg unzip git curl wget zip

RUN apt-get update &&\
	apt-get upgrade -y &&\
    apt-get install -y  software-properties-common

# JS
RUN curl -sL https://deb.nodesource.com/setup_12.x | bash -
RUN apt-get install -y nodejs
RUN npm install -g npm@latest

RUN useradd -ms /bin/bash karolkoson
RUN adduser karolkoson sudo

EXPOSE 9000

USER karolkoson
WORKDIR /home/karolkoson/
RUN curl -s "https://get.sdkman.io" | bash
RUN chmod a+x "/home/karolkoson/.sdkman/bin/sdkman-init.sh"
RUN bash -c "source /home/karolkoson/.sdkman/bin/sdkman-init.sh && sdk install java 8.0.272.hs-adpt"
RUN bash -c "source /home/karolkoson/.sdkman/bin/sdkman-init.sh && sdk install sbt 1.4.8"
RUN bash -c "source /home/karolkoson/.sdkman/bin/sdkman-init.sh && sdk install scala 2.12.13"

RUN mkdir ebiznes
WORKDIR /home/karolkoson/ebiznes/

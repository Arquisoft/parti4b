#!/bin/sh
if [ ! -d kafka ]; then
    wget http://apache.rediris.es/kafka/0.10.2.0/kafka_2.11-0.10.2.0.tgz -O kafka.tgz
    mkdir -p kafka && tar xzf kafka.tgz -C kafka --strip-components 1
fi
nohup bash -c "cd kafka && bin/zookeeper-server-start.sh config/zookeeper.properties &"
nohup bash -c "cd kafka && bin/kafka-server-start.sh config/server.properties &"
java -javaagent:heroku-javaagent-1.5.jar=stdout=true,lxmem=true -cp target/*.jar es.uniovi.asw.Application
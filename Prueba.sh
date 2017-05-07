#!/bin/sh
if [ ! -d kafka ]; then
    wget http://apache.rediris.es/kafka/0.10.2.0/kafka_2.11-0.10.2.0.tgz -O kafka.tgz
fi
mkdir -p kafka && tar xzf kafka.tgz -C kafka --strip-components 1
nohup bash -c "cd kafka && bin/zookeeper-server-start.sh config/zookeeper.properties &"
sleep 10
nohup bash -c "cd kafka && bin/kafka-server-start.sh config/server.properties &"
sleep 10
java $JAVA_OPTS -Dserver.port=$PORT -jar target/*.jar  
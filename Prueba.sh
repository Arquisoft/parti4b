#!/bin/sh
mkdir -p kafka && tar xzf kafka.tgz -C kafka --strip-components 1
nohup bash -c "cd kafka && bin/zookeeper-server-start.sh config/zookeeper.properties &"
nohup bash -c "cd kafka && bin/kafka-server-start.sh config/server.properties &"
sleep -s 10
java $JAVA_OPTS -Dserver.port=$PORT -jar target/*.jar  
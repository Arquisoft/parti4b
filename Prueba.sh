#!/bin/sh
if [ ! -d kafka ]; then
    wget http://apache.rediris.es/kafka/0.10.2.0/kafka_2.11-0.10.2.0.tgz -O kafka.tgz
    mkdir -p kafka && tar xzf kafka.tgz -C kafka --strip-components 1
    rm kafka.tgz
fi
cd kafka
bash bin/zookeeper-server-start.sh config/zookeeper.properties &
sleep 10
bash bin/kafka-server-start.sh config/server.properties &
cd ..
java -Dserver.port=$PORT -jar target/parti4b-0.0.1.jar 

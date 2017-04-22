#!/bin/sh
gnome-terminal -e "bash bin/zookeeper-server-start.sh config/zookeeper.properties"
sleep 10s
gnome-terminal -e "bash bin/kafka-server-start.sh config/server.properties"


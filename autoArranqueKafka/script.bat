start "" bin\windows\zookeeper-server-start.bat config\zookeeper.properties
ping -n 10 localhost > null
start "" bin\windows\kafka-server-start.bat config\server.properties


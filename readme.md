### Zookeeper
bin\windows\zookeeper-server-start.bat config\zookeeper.properties

### Broker
bin\windows\kafka-server-start.bat config\server.properties

### Consumer
bin\windows\kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic parcel-delivered --key-deserializer org.apache.kafka.common.serialization.StringDeserializer --value-deserializer org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator="-" --from-beginning

### List of topic
bin\windows\kafka-topics.bat --zookeeper localhost:2181 --list 

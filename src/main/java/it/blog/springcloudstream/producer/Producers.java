package it.blog.springcloudstream.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Soby Chacko
 */
public class Producers {

	public static void main(String... args) throws InterruptedException {

		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		List<KeyValue<String, String>> parcelDestination = Arrays.asList(
				new KeyValue<>("CD98199", "45.4091705,9.1505369"),
				new KeyValue<>("YT99398", "45.4861499,9.1873881")
		);

		DefaultKafkaProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(props);
		KafkaTemplate<String, String> template = new KafkaTemplate<>(pf, true);
		template.setDefaultTopic("parcel-destination");
        
		for (KeyValue<String,String> keyValue : parcelDestination) {
			template.sendDefault(keyValue.key, keyValue.value);
		}

		DefaultKafkaProducerFactory<String, String> pf1 = new DefaultKafkaProducerFactory<>(props);
		KafkaTemplate<String, String> template1 = new KafkaTemplate<>(pf1, true);
		template1.setDefaultTopic("parcel-position");
	
		int leftLimit = 0;
        int rightLimit = 99;
        
        while(true)
        {
        	int latPosition = (int) (leftLimit + (Math.random() * (rightLimit - leftLimit)));
        	
        	template1.sendDefault("CD98199", "45.4091705,9.15053" + latPosition);
        	template1.sendDefault("YT99398", "45.4861499,9.18738" + latPosition);        	
        	
        	Thread.sleep(500);
        	
        }        
	}
	

}
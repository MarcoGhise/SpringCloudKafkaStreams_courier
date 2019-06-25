package it.blog.springcloudstream.consumer;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Serialized;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.annotations.KafkaStreamsProcessor;
import org.springframework.messaging.handler.annotation.SendTo;
import it.blog.springcloudstream.bean.Gps;

@SpringBootApplication
public class Delivery {

	public static void main(String[] args) {
		SpringApplication.run(Delivery.class, args);
	}

	@EnableBinding(KStreamProcessDelibery.class)
	public static class DeliveryTableJoin {

		@StreamListener
		@SendTo("output")
		public KStream<String, String> process(@Input("input") KStream<String, String> parcelPositionStream,
				@Input("inputTable") KTable<String, String> parcelDestinationTable) {

			return parcelPositionStream
					// Join the stream against the table.
					//
					// Null values possible: In general, null values are possible for region (i.e.
					// the value of
					// the KTable we are joining against) so we must guard against that (here: by
					// setting the
					// fallback region "UNKNOWN"). In this specific example this is not really
					// needed because
					// we know, based on the test setup, that all users have appropriate region
					// entries at the
					// time we perform the join.
					//
					// Also, we need to return a tuple of (region, clicks) for each user. But
					// because Java does
					// not support tuples out-of-the-box, we must use a custom class
					// `RegionWithClicks` to
					// achieve the same effect.  
					.join(parcelDestinationTable, (destination, position) -> new Gps(destination, position),
							Joined.with(Serdes.String(), Serdes.String(), null))
					// Change the stream from <user> -> <region, clicks> to <region> -> <clicks>
					.filter((key, value) -> value.getDestination().equals(value.getPosition()))					
					.map((key, value) -> new KeyValue<String, String>(key, value.toString()));
					

		}
		/*
		private static KeyValue<String, Gps> print(String key, Gps value)
		{
			System.out.println("Valore Chiave " + key + " Valore value " + value);
			
			return new KeyValue<String, Gps>(key, value);
			
		}
		*/
	}
	
	interface KStreamProcessDelibery extends KafkaStreamsProcessor {

		@Input("inputTable")
		KTable<?, ?> inputKTable();
	}

}

import java.io.*;
import java.util.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;

class Kafkahw{
    static String brokers;
    static String topic;
    static int records;
    static int recordSize;
    static boolean state = true;

    static class AddSize implements Header{
        @Override
        public String key() {
            return "";
        }

        @Override
        public byte[] value() {
            return new byte[recordSize - 30];
        }
    }

    public static void getCmd(String[] args){
        try {
    	    brokers = args[1];
	        topic = args[3];
	        records = Integer. parseInt(args[5]);
	        recordSize = Integer. parseInt(args[7]);
        }catch (Exception e) {
            state = false;
        }
    }
    
    public static void main(String[] args){
         //System.out.println(String.format("key-%06d", 1000));
         //System.out.println(String.format("value-%06d", 999));
        //for(String arg : args)
            //System.out.println(arg); 
        getCmd(args);
        if(state){
            //System.out.println("" + brokers + topic + records + recordSize);

            // Create instance of nested Static class
            Kafkahw.AddSize addSize = new Kafkahw.AddSize();

            Properties props = new Properties();
            props.put("bootstrap.servers", brokers);
            props.put("acks", "all");
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

            Producer<String, String> producer = new KafkaProducer<>(props);
            for (int i = 0; i < records; i++){
                    List<Header> headerOne = new ArrayList<Header>();
                    headerOne.add(addSize);

                    producer.send(new ProducerRecord<String, String>(topic,null,String.format("key-%06d", i), String.format("value-%06d", i),headerOne));
            }

            producer.close();
            System.out.println("program end");
            
        }else{
                System.out.println("error args");
        }

    }
 
}
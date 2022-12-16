//package com.tweetapp.handler;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaProducer {
//
//	@Autowired
//	private KafkaTemplate<String, String> kafkaTemplate;
//
//	public void sendMessageToTopic(String message) {
//		kafkaTemplate.send("ChangePasswordTopic", message);
//	}
//
//	/*
//	.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
//	 .\bin\windows\kafka-server-start.bat .\config\server.properties
//	 */
//
//}
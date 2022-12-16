//package com.tweetapp.handler;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class KafkaConsumer {
//
//	@KafkaListener(topics = "ChangePasswordTopic", groupId = "tweetApp")
//	public void listenToCodeDecodeKafkaTopic(String messageReceived) {
//		log.info("Message received, The user trying to change password:" + messageReceived);
//	}
//}
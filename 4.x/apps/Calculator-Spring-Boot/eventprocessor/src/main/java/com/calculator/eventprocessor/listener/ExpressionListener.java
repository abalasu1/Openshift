package com.calculator.eventprocessor.listener;

import java.io.IOException;
import java.util.HashMap;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.calculator.eventprocessor.model.ExpressionPair;
import com.calculator.eventprocessor.stream.ProcessorStreams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.calculator.eventprocessor.proxy.CompositeOpProxy;

@Component
public class ExpressionListener {	
	@Autowired
	private CompositeOpProxy compositeOpProxy;
	
	@Resource(name="stringRedisTemplate")
	private ListOperations<String, Object> expressionResults;
	
	@StreamListener(ProcessorStreams.INPUT)
	@SendTo(ProcessorStreams.OUTPUT)
	public HashMap<String, Object> evaluateExpression(@Payload ExpressionPair expressionpair) {
		String identifier = expressionpair.getIdentifier();
		String expression = expressionpair.getExpression();
		String uuid = expressionpair.getUuid();
		
		HashMap<String, Object> results = compositeOpProxy.eval(expression);
		results.put("identifier", identifier);				
		results.put("uuid", uuid);
		
        try {
        	String json = new ObjectMapper().writeValueAsString(results);
        	expressionResults.leftPush(uuid, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	/*@KafkaListener(topics = ProcessorStreams.OUTPUT, groupId = "calculator")
    public void store(String expressionResult) {
		ExpressionResult result = new ExpressionResult(expressionResult);
		expressionResults.leftPush(result.getUuid(), expressionResult);
    }*/
}
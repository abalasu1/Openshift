package com.calculator.eventprocessor.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calculator.eventprocessor.model.ExpressionPair;
import com.calculator.eventprocessor.model.ExpressionResult;
import com.calculator.eventprocessor.stream.ProcessorStreams;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ProcessorController {
    @Autowired
    @Resource(name="stringRedisTemplate")
	private ListOperations<String, String> expressionResults;
    
    private MessageChannel processorInput;
    
    @Autowired
    @Qualifier(ProcessorStreams.INPUT)
    public void SendingBean(MessageChannel processorInput) {
        this.processorInput = processorInput;
    }
    
	@RequestMapping(value="/eventprocessor/submit", method = RequestMethod.POST)
	public String processExpressions (@RequestBody String expressionSet) {
		String uuid = UUID.randomUUID().toString();
		ObjectMapper mapper = new ObjectMapper();

		List <ExpressionPair> expressions = new ArrayList <> ();
		try {			
			expressions = mapper.readValue(expressionSet, mapper.getTypeFactory().constructCollectionType(List.class, ExpressionPair.class));
			
			ListIterator <ExpressionPair> expressionIterator = expressions.listIterator();
			while (expressionIterator.hasNext()) {
				ExpressionPair expr = expressionIterator.next();
				expr.setUuid(uuid);
				
				processorInput.send(MessageBuilder.withPayload(expr).build());
				System.out.println("PROCESS EXPRESSION = " + expr.toString());
			}
		} catch (IOException e) {
            e.printStackTrace();
        }
		
		return uuid;		
	}
	
	@RequestMapping(value="/eventprocessor/result", method = RequestMethod.GET)
	public HashMap <String, Object> getResults (@RequestParam(value="uuid", required=true) String uuid) {		
		HashMap <String, Object> finalResults = new HashMap <String, Object> ();
		List <String> results = new ArrayList <String> ();
		
		results = expressionResults.range(uuid, 0, -1);
		for (String r: results) {
			ExpressionResult result = new ExpressionResult (r);
			finalResults.put (result.getIdentifier(), result);
		}

		return finalResults;		
	}
}

package com.calculator.processor.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.calculator.processor.service.ProcessorService;

@RestController
public class ProcessorController {
    @Autowired
    private ProcessorService processorService;
    
	@RequestMapping(value="/processor/calculate", method = RequestMethod.POST)
	public HashMap<String, Object> processExpressions (@RequestBody HashMap<String, String> expressions) {
		HashMap <String, Object> results = new HashMap <String, Object> ();		

		for(Map.Entry <String, String> entry : expressions.entrySet()) {
			String key   = entry.getKey();
		    String value = entry.getValue();

		    HashMap <String, Object> eachexprResult = processorService.evaluateEachExpr(key, value);		    
		    results.put(key, eachexprResult);
		}

		return results;		
	}
    
    @RequestMapping(value="/processor/mtcalculate", method = RequestMethod.POST)
	public HashMap<String, Object> mtprocessExpressions (@RequestBody HashMap<String, String> expressions) 
	 throws InterruptedException, ExecutionException{
		HashMap<String, Object> results = new HashMap <String, Object> ();	
		
		List<String> keys = new ArrayList<String>();
		List<Future<HashMap<String, Object>>> futures = new ArrayList<Future<HashMap <String, Object>>> ();

		for(Map.Entry <String, String> entry : expressions.entrySet()) {
			String key   = entry.getKey();
			keys.add(key);
			
		    String value = entry.getValue();
		    Future<HashMap <String, Object>> future = processorService.mtevaluateEachExpr(key, value);
		    
		    futures.add(future);		    
		}
		
		for (int i = 0; i < futures.size(); i ++) {
			Future <HashMap <String, Object>> future = futures.get(i);
			while (true) {
				if (future.isDone ()) {
					HashMap <String, Object> eachexprResult = future.get();
					results.put(keys.get(i), eachexprResult);

					break;
				}
			}
	    }

		return results;		
	}
}

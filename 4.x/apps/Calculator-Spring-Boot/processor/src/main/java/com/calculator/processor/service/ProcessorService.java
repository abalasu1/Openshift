package com.calculator.processor.service;

import java.util.HashMap;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.calculator.processor.proxies.CompositeOpProxy;

@Service
@EnableAsync
public class ProcessorService {
	@Autowired
	private CompositeOpProxy compositeOpProxy;
	
	public HashMap <String, Object> evaluateEachExpr (String key, String value) {
		 HashMap <String, Object> eachexprResult = new HashMap <String, Object> ();
		    
		 eachexprResult.put("identifier", key);
		 try { eachexprResult.put("expr", java.net.URLDecoder.decode(value, "en-US")); }
		 catch (Exception e) { }
		 
		 eachexprResult.put("output", compositeOpProxy.eval(value));		 
		 return eachexprResult;
	}

	@Async
	public Future <HashMap <String, Object>> mtevaluateEachExpr (String key, String value) {
		 HashMap <String, Object> eachexprResult = new HashMap <String, Object> ();
		    
		 eachexprResult.put("identifier", key);
		 try { eachexprResult.put("expr", java.net.URLDecoder.decode(value, "en-US")); }
		 catch (Exception e) { }
		 
		 eachexprResult.put("output", compositeOpProxy.eval(value));		 
		 return new AsyncResult <HashMap <String, Object>> (eachexprResult);
	}
}

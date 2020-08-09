package com.calculator.eventprocessor.proxy;

import java.util.HashMap;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="COMPOSITE-OP", url="http://composite-op:8005")
public interface CompositeOpProxy {
	@GetMapping("/compositeop/eval")
	HashMap<String, Object> eval(@RequestParam("expr") String expr);
}
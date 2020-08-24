package com.calculator.compositeop.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="BASIC-OP-MULTIPLY", url="http://basic-op-multiply:8003")
public interface MultiplyProxy {
	@GetMapping("/basicop/multiply")
	int multiply(@RequestParam("n1") int n1, @RequestParam("n2") int n2);
}
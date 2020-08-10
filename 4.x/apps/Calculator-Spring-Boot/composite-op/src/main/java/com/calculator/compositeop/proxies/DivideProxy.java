package com.calculator.compositeop.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="BASIC-OP-DIVIDE", url="http://basic-op-divide:8004")
public interface DivideProxy {
	@GetMapping("/basicop/divide")
	int divide(@RequestParam("n1") int n1, @RequestParam("n2") int n2);
}
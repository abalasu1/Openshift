package com.calculator.compositeop.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="BASIC-OP-SUBTRACT", url="http://basic-op-subtract:8002")
public interface SubtractProxy {
	@GetMapping("/basicop/subtract")
	int subtract(@RequestParam("n1") int n1, @RequestParam("n2") int n2);
}
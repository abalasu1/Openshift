package com.calculator.compositeop.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="BASIC-OP-ADD", url="http://basic-op-add:8001")
public interface AddProxy {
	@GetMapping("/basicop/add")
	int add(@RequestParam("n1") int n1, @RequestParam("n2") int n2);
}
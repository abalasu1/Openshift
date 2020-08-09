package com.calculator.basicop;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicOpSubtractController {
    
    @RequestMapping(value="/basicop/subtract", method = RequestMethod.GET)
    public int subtract(@RequestParam(value="n1", required=true) int n1, @RequestParam(value="n2", required=true) int n2) {
    	return n1 - n2;
	}
}

package com.calculator.compositeop.controller;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calculator.compositeop.proxies.AddProxy;
import com.calculator.compositeop.proxies.SubtractProxy;
import com.calculator.compositeop.proxies.MultiplyProxy;
import com.calculator.compositeop.proxies.DivideProxy;

@RestController
public class CompositeOpController {
	
	@Autowired
	private AddProxy addProxy;
	
	@Autowired
	private SubtractProxy subtractProxy;
	
	@Autowired
	private MultiplyProxy multiplyProxy;
	
	@Autowired
	private DivideProxy divideProxy;
	
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }
    
    private int getPrecedence(char ch) {
        switch (ch) { 
        	case '+': 
        	case '-': 
        		return 1; 
       
        	case '*': 
        	case '/': 
        		return 2;
        } 
        
        return -1; 
    }
    
    public String converttoRPN (String expr, String delimiter) {
    	ArrayList <Character> stack = new ArrayList <Character> ();
        String rpnexpr = "";
        
        int pos = 0;
        for (int i = 0; i < expr.length(); i ++) {
        	char ch = expr.charAt(i);         	
        	if (!isOperator(ch)) {
        		rpnexpr += ch;
        	    if (pos < (expr.length() - 1) && isOperator(expr.charAt(pos + 1))) rpnexpr += delimiter;
        	}
        	else if (ch == '(') {
        		stack.add('(');
      	      	rpnexpr += delimiter;        		
        	}
        	else if (ch == ')') {
                while (!stack.isEmpty() && stack.get(stack.size() - 1) != '(') {
                    rpnexpr += stack.get(stack.size() - 1);
                    stack.remove(stack.size() - 1);
                    
                    rpnexpr += delimiter;
                }

                stack.remove(stack.size() - 1);
        	}
        	else {
        		while (!stack.isEmpty() && stack.get(stack.size() - 1) != '(' && getPrecedence(ch) <= getPrecedence(stack.get(stack.size() - 1))) {
        			rpnexpr += stack.get(stack.size() - 1);
        			stack.remove(stack.size() - 1);
        			
        			rpnexpr += delimiter;
        		}
        		
        	    stack.add(ch);
        	}
        	
            pos += 1;
        }
        
        while (!stack.isEmpty()) {
        	rpnexpr += delimiter;
        	
        	rpnexpr += stack.get(stack.size() - 1);
        	stack.remove(stack.size() - 1);
        }

        return rpnexpr.trim().replaceAll(" +", " ");
    }    
        	
	public int executeOp(char op, int n1, int n2) {
		int result = 0;
		switch(op) {
			case '+':
				result = addProxy.add(n1, n2);
				break;
			case '-':
				result = subtractProxy.subtract(n1, n2);
				break;
			case '*':
				result = multiplyProxy.multiply(n1, n2);
				break;
			case '/':
				result = divideProxy.divide(n1, n2);
				break;
		}
		
		return result;
	}
	
    @RequestMapping(value="/compositeop/square", method = RequestMethod.GET)
    public int square(@RequestParam(value="n1", required=true) int n1) {
    	return executeOp('*', n1, n1);
	}
    
    @RequestMapping(value="/compositeop/cube", method = RequestMethod.GET)
    public int cube(@RequestParam(value="n1", required=true) int n1) {
    	int square = executeOp('*', n1, n1);
    	return executeOp('*', square, n1);
	}
    
    @RequestMapping(value="/compositeop/mean", method = RequestMethod.GET)
    public int mean(@RequestParam(value="n", required=true) List<Integer> n) {
    	int result = 0;
    	for (int i = 0; i < n.size(); i++) {
    		result = executeOp('+', result, n.get(i));
    	}
    	
    	result = executeOp('/', result, n.size());
    	return result;
	}
    
    public int processrpnexpr(String rpnexpr, HashMap <String, Object> stepn) {
    	Deque<Integer> iResults = new LinkedList<>();    	
        String delimiter = " ";
        
        int lp = 0;
        String[] symbols = rpnexpr.split(delimiter);
        
        for (String token : symbols) {
        	HashMap<String, Object> eachstep = new HashMap<String, Object> ();
        	
        	if (token.length() == 1 && "+-*/".contains(token)) {
        		final int y = iResults.removeFirst();
        		final int x = iResults.removeFirst();
        		
        		if (isOperator(token.charAt(0))) {
                	lp = lp + 1;
        			eachstep.put("Op", token);
        			
        			eachstep.put("n1", x);
        			eachstep.put("n2", y);
        			
        	        stepn.put("Step ".concat(String.valueOf(lp)), eachstep);
        	        
        			iResults.addFirst(executeOp(token.charAt(0), x, y));
        		} else {
        			throw new IllegalArgumentException  ("Malformed RPN at :" + token);
        		}
        	} else { 
        		iResults.addFirst(Integer.parseInt(token));
          }        
        }
               
        return iResults.removeFirst();
	}
    
    @RequestMapping(value="/compositeop/evalrpnexpr", method = RequestMethod.GET)
    public HashMap<String, Object> evalrpnexpr(@RequestParam(value="expr", required=true) String expr) {
    	HashMap<String, Object> steps = new HashMap <String, Object> ();
    	steps.put("rpnexpr", expr);
    	
    	HashMap<String, Object> stepn = new HashMap <String, Object> ();
    	
    	int result = processrpnexpr(expr, stepn);
        steps.put("value", result);
    	    	
    	steps.put("steps", stepn);    	
    	return steps;
    }
    
    @RequestMapping(value="/compositeop/eval", method = RequestMethod.GET)
    public HashMap<String, Object> eval(@RequestParam(value="expr", required=true) String expr) {
    	HashMap<String, Object> steps = new HashMap <String, Object> ();
    	steps.put("expr", expr);
    	
    	String rpnexpr = converttoRPN(expr, " ");
    	steps.put("rpnexpr", rpnexpr);
    	
    	HashMap<String, Object> stepn = new HashMap <String, Object> ();
    	int result = processrpnexpr(rpnexpr, stepn);
    	
    	steps.put("value", result);
    	steps.put("steps", stepn);
    	
    	return steps;
    }    
}
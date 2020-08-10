package com.calculator.eventprocessor.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExpressionResult  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String identifier;
	private String rpnexpr;
	
	private String expr;
	private int result;
	
	private String uuid;

	public ExpressionResult(String expressionResult) {
		ObjectMapper mapper = new ObjectMapper();
        try {
        	JsonNode rootNode = mapper.readTree(expressionResult);
        	identifier = rootNode.get("identifier").asText();
        	
        	rpnexpr = rootNode.get("rpnexpr").asText();
        	expr = rootNode.get("expr").asText();
        	
        	result = rootNode.get("value").asInt();
        	uuid = rootNode.get("uuid").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public ExpressionResult(String identifier, String rpnexpr, String expr, int result, String uuid) {
		super();
		this.identifier = identifier;
		this.rpnexpr = rpnexpr;
		this.expr = expr;
		this.result = result;
		this.uuid = uuid;
	}
	
	public ExpressionResult(HashMap<String, Object> results) {
		super();
		this.identifier = results.get("identifier").toString();
		this.rpnexpr = results.get("rpnexpr").toString();
		this.expr = results.get("expr").toString();
		this.result = (int) results.get("result");
		this.uuid = results.get("uuid").toString();
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getRpnexpr() {
		return rpnexpr;
	}

	public void setRpnexpr(String rpnexpr) {
		this.rpnexpr = rpnexpr;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}	

	public double getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ExpressionResult [identifier=" + identifier + ", rpnexpr=" + rpnexpr + ", expr=" + expr + ", result="
				+ result + ", uuid=" + uuid + "]";
	}
}
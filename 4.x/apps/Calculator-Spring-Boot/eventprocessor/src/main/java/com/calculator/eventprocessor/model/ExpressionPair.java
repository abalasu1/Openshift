package com.calculator.eventprocessor.model;

import java.io.Serializable;

public class ExpressionPair  implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private String identifier;
    private String expression;
    private String uuid;

	public ExpressionPair() {
    }
    
    public ExpressionPair(String identifier, String expression, String uuid) {
		super();
		this.identifier = identifier;
		this.expression = expression;
		this.uuid = uuid;
	}

	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getExpression() {
		return expression;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "ExpressionPair [identifier=" + identifier + ", expression=" + expression + ", uuid=" + uuid + "]";
	}
}
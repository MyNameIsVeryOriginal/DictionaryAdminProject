package org.restapi.crud.crud.model;

public class CrudModel {
	private String word;
	private String type;
	private String definition;
	
	public CrudModel() {
		// TODO Auto-generated constructor stub
	}
	
	public CrudModel(String word, String type, String definition) {
		super();
		this.word = word;
		this.type = type;
		this.definition = definition;
	}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	@Override
	public String toString() {
		return "CrudModel [word=" + word + ", type=" + type + ", definition=" + definition + "]";
	}
	
			
}

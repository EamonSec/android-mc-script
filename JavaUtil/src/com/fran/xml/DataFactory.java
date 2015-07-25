package com.fran.xml;

public class DataFactory {

	public static DataFactory factory;
	
	
	public static DataFactory getFactory(){
		if(factory==null){
			factory=new DataFactory();
		}
		return factory;
	}
	
	
	
}

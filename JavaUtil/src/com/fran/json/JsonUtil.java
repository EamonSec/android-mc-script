package com.fran.json;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fran.json.JsonUtil.Person;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtil {

	
	
	@Test
	public void fun(){
		List<Person>persons=getPersons();
		Gson gson=new Gson();
		String json = gson.toJson(persons);
		List<Person> parseJsonArray = parseJsonArray(json, Person.class);
		System.out.println(parseJsonArray);
	}
	
	private List<Person> getPersons()
	{
		List<Person>persons=new ArrayList<>();
		int i=0;
		while(i<5){
			Person p=new Person();
			p.setName("zz");
			p.setPassword("zzzzz");
			i++;
			persons.add(p);
		}
		return persons;
	}

	public <T>List<T> parseJsonArray(String string,Class<T>clazz){
		List<T> ts=new ArrayList<>();
		Gson gson = new Gson();
		JsonElement jEle = new JsonParser().parse(string);
		JsonArray jsonArray = jEle.getAsJsonArray();
		for(int i=0;i<jsonArray.size();i++){
			JsonObject jsonObject=(JsonObject) jsonArray.get(i);
			T fromJson = gson.fromJson(jsonObject,clazz);
			ts.add(fromJson);
		}
		return ts;
	}
	
	 public static class Person{
		 private String name;
		 private String password;
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public String getPassword()
		{
			return password;
		}
		public void setPassword(String password)
		{
			this.password = password;
		}
		@Override
		public String toString()
		{
			return "Person [name=" + name + ", password=" + password + "]";
		}
		 
		 
	 }
	 @Test
	 public void fun1(){
		 
		 
	 }
	
}

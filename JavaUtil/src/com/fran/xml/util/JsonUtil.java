package com.fran.xml.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtil {

	private static JsonUtil jsonUtil2;

	public static JsonUtil getUtil()
	{
		if (jsonUtil2 == null)
		{
			jsonUtil2 = new JsonUtil();
		}
		return jsonUtil2;
	}

	public <T> List<T> parseJsonArray(String string, Class<T> clazz)
	{
		List<T> ts = null;
		try
		{
			ts = new ArrayList<T>();
			Gson gson = new Gson();
			JsonElement jEle = new JsonParser().parse(string);
			JsonArray jsonArray = jEle.getAsJsonArray();
			for (int i = 0; i < jsonArray.size(); i++)
			{
				JsonObject jsonObject = (JsonObject) jsonArray.get(i);
				T fromJson = gson.fromJson(jsonObject, clazz);
				ts.add(fromJson);
			}
		} catch (Exception exception)
		{
			exception.printStackTrace();
			return null;
		}
		return ts;
	}

	/**
	 * 解析json，并转换成类
	 * 
	 * @param jsonObject
	 *            需要转换的JSONObject
	 * @param clazz
	 *            转换成什么类型
	 * @return Z
	 */
	public <T> T parseJson(String json, Class<T> clazz)
	{
		T t = new Gson().fromJson(json, clazz);
		return t;
	}

	/**
	 * 将单个对象转换成json格式
	 * 
	 * @param bean
	 * @return
	 */
	public <T> String toJson(T bean)
	{
		Gson g = new Gson();
		String json = g.toJson(bean);
		return json;

	}

	/**
	 * 将单个对象的list集合转换成json格式
	 * 
	 * @param array
	 * @return
	 */
	public <T> String toJsonArray(List<T> array)
	{
		Gson g = new Gson();
		String jsonArr = g.toJson(array);
		return jsonArr;
	}

	public static JsonObject getDefault()
	{
		return new JsonObject();
	}
}

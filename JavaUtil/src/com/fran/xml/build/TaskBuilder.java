package com.fran.xml.build;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fran.xml.StringFormat;
import com.fran.xml.xmldecode.IPropertyDecoder;
import com.fran.xml.xmldecode.PropertyDecoder;
import com.fran.xml.xmldecode.PropertyWrapper;

public class TaskBuilder {
	private static IPropertyDecoder decoder = PropertyDecoder.getDecoder();

	@Test
	public void fun() throws Exception
	{
		build("/Users/apple/Documents/myWork/JavaUtil/src/property.xml", "/Users/apple/Desktop/temp/");
	}

	public static void build(String propertyFile, String outputFile) throws Exception
	{
		List<PropertyWrapper> ppws = decoder.getPropertyWrapper(propertyFile);
		List<String> taskNames = getTaskName(ppws);
		List<StringBuilder> taskBuilder = buildText(ppws);
		PropertyDecoder.outPut(taskBuilder, taskNames, outputFile);
	}
	public static void builde(List<PropertyWrapper> ppws, String outputFile ) throws Exception{
		List<String> taskNames = getTaskName(ppws);
		List<StringBuilder> taskBuilder = buildText(ppws);
		PropertyDecoder.outPut(taskBuilder, taskNames, outputFile);
	
	}
	
	private static List<StringBuilder> buildText(List<PropertyWrapper> ppws) throws Exception
	{
		List<StringBuilder> taskBuilder = new ArrayList<>();
		for (PropertyWrapper ppw : ppws)
		{
			taskBuilder.add(getTaskTextBuilder(ppw));
		}

		return taskBuilder;
	}

	// 用请求码做task的名字
	private static List<String> getTaskName(List<PropertyWrapper> ppws)
	{
		ArrayList<String> tasksName = new ArrayList<>();
		for (PropertyWrapper ppw : ppws)
		{
			tasksName.add(getTaskName(ppw));
		}
		return tasksName;
	}

	/**
	 * 拼接task的名字
	 * 
	 * @param ppw
	 * @return
	 */
	public static String getTaskName(PropertyWrapper ppw)
	{
		return StringFormat.toUpPrimary(ppw.getRequestCode()) + "Task";
	}

	// 拼接task的主要内容
	private static StringBuilder getTaskTextBuilder(PropertyWrapper ppw)
	{
		StringBuilder builder = new StringBuilder();
		appendPackage(builder, ppw);
		appendImport(builder, ppw);
		appendHeader(builder, ppw);
		StringFormat.appendStart(builder);
		StringFormat.appendField("private", ppw.getCallback(), StringFormat.toLowerPrimary(ppw.getCallback()), builder);
		StringFormat.appendField("private ", "String", "msg", builder);
		StringFormat.appendConstruch(StringFormat.toUpPrimary(ppw.getRequestCode())+"Task", ppw.getCallback(), builder);
		appendDoInbackground(builder, ppw);
		appendOnPostExecute(builder, ppw);
		StringFormat.appendAnnotation(builder, "调用接口获取后台json", "3");
		appendGetJsonMethod(ppw, builder);
		StringFormat.appendAnnotation(builder, "正确获得json后用于转换成相关实体并返回 ", "3");
		appendGetObjectMethod(ppw, builder);
		StringFormat.appendEnd(builder);
		return builder;
	}

	/**
	 * 创建获取到json实体后解析的方法,这里写死了，用JsonUtil解析实体和集合
	 * 
	 * @param ppw
	 * @param builder
	 */
	private static void appendGetObjectMethod(PropertyWrapper ppw, StringBuilder builder)
	{
		StringFormat.appendMethod(builder, "private", "Object", "getObject", null, "String result");
		StringFormat.appendStart(builder);
		StringFormat.appendLog(builder, "test", "result");
		builder.append("return JsonUtil.getUtil().");
		if (ppw.getResultType().equals("1"))
		{
			// 单个实体
			builder.append("parseJson");
		} else
		{
			// 集合实体
			builder.append("parseJsonArray");
		}
		builder.append("(result,").append(ppw.getResultEntity()).append(".class);");
		StringFormat.appendEnd(builder);
	}

	private static void appendGetJsonMethod(PropertyWrapper ppw, StringBuilder builder)
	{
		StringFormat.appendMethod(builder, "private", "String", "getJsonData", "Exception", ppw.getMethodArg());
		StringFormat.appendStart(builder);
		builder.append("return ").append(ppw.getFactoryName()).append(".")
				.append(StringFormat.GET_FACTORY_INSTANCE_NAME).append("().")
				.append(StringFormat.GET_INTERFACE_INSTANCE_NAME).append("().").append(ppw.getMethodName()).append("(")
				.append(StringFormat.getPureParams(ppw.getMethodArg())).append(");");
		StringFormat.appendEnd(builder);
	}

	private static void appendOnPostExecute(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("protected void onPostExecute(String result)").append("{").append("if(")
				.append(StringFormat.toLowerPrimary(ppw.getCallback())).append("==null)").append("{return;}")
				.append("if(result==null){").append(StringFormat.toLowerPrimary(ppw.getCallback()))
				.append(".fail(msg,-1);}").append("else{").append(StringFormat.toLowerPrimary(ppw.getCallback()))
				.append(".success(getObject(result));}").append("}");
	}

	private static void appendDoInbackground(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("protected String doInBackground(String...params)");
		StringFormat.appendStart(builder);
		String[] paramsString = StringFormat.getParams(ppw.getMethodArg());
		for (int i = 0; i < paramsString.length; i++)
		{

			builder.append("String ").append(paramsString[i]).append("=params[").append(i).append("];");
		}
		builder.append("String json=null;\n").append("try{").append("json=getJsonData(")
				.append(StringFormat.getPureParams(ppw.getMethodArg())).append(");\n}catch(Exception e)").append("{")
				.append("e.printStackTrace();msg = e.getMessage();").append("}").append("return json;");
		StringFormat.appendEnd(builder);
	}

	private static void appendHeader(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("public class ").append(getTaskName(ppw)).append(" extends AsyncTask<String,Integer,String>");

	}

	private static void appendImport(StringBuilder builder, PropertyWrapper ppw)
	{

		String taskClassName = ppw.getTaskClassName();
		builder.append("import ");
		if (taskClassName != null)
			builder.append(taskClassName);
		else
			builder.append("android.os.AsyncTask");
		builder.append(";");

	}

	private static void appendPackage(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("package ").append(ppw.getTaskPkg()).append(";");
	}

}

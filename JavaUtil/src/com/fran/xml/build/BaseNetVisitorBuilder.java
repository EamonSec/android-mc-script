package com.fran.xml.build;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fran.xml.StringFormat;
import com.fran.xml.xmldecode.IPropertyDecoder;
import com.fran.xml.xmldecode.PropertyDecoder;
import com.fran.xml.xmldecode.PropertyWrapper;

/**
 * 访问接口和实现类
 * 
 * @author 周稹
 *
 */
public class BaseNetVisitorBuilder {
	IPropertyDecoder decoder = PropertyDecoder.getDecoder();

	@Test
	public void fn() throws Exception
	{
		build("/Users/apple/Documents/myWork/JavaUtil/src/property.xml", "/Users/apple/Desktop/temp/", true);
	}

	public static void build(List<PropertyWrapper> ppws, String outputFile, boolean isOnlyJson) throws IOException
	{

		String interfaceText = buildText(ppws, false, isOnlyJson);
		String implText = buildText(ppws, true, isOnlyJson);
		if (outputFile == null)
		{
			System.out.println("nterfaceText:" + interfaceText);
			System.err.println("implText:" + implText);
			return;
		}
		FileWriter interfaceWriter = new FileWriter(new File(outputFile + "interface.java"));
		FileWriter implWriter = new FileWriter(new File(outputFile + "interfaceImpl.java"));
		interfaceWriter.write(interfaceText);
		interfaceWriter.flush();
		implWriter.write(implText);
		implWriter.flush();
		interfaceWriter.close();
		implWriter.close();

	}

	/**
	 * 搭建接口文件和实现类
	 * 
	 * @param xmlFileName
	 *            配置文件路径
	 * @param outputFile
	 *            输出文件路径
	 * @param isOnlyJson
	 *            是否要将参数包装成json，并只发送一个json的键值对("json",json),否则每个参数都设置为键值对
	 * @throws Exception
	 */
	public void build(String xmlFileName, String outputFile, boolean isOnlyJson) throws Exception
	{

		String interfaceText = buildText(decoder.getPropertyWrapper(xmlFileName), false, isOnlyJson);
		String implText = buildText(decoder.getPropertyWrapper(xmlFileName), true, isOnlyJson);
		if (outputFile == null)
		{
			System.out.println("nterfaceText:" + interfaceText);
			System.err.println("implText:" + implText);
			return;
		}
		FileWriter interfaceWriter = new FileWriter(new File(outputFile + "interface.java"));
		FileWriter implWriter = new FileWriter(new File(outputFile + "interfaceImpl.java"));
		interfaceWriter.write(interfaceText);
		interfaceWriter.flush();
		implWriter.write(implText);
		implWriter.flush();
		interfaceWriter.close();
		implWriter.close();
	}

	private static String buildText(List<PropertyWrapper> ppws, boolean isImpl, boolean isOnlyJson)
	{
		StringBuilder builder = new StringBuilder();
		appendHeader(builder, isImpl);
		appendContent(builder, ppws, isImpl, isOnlyJson);
		appendEnd(builder);
		return builder.toString();
	}

	private static void appendEnd(StringBuilder builder)
	{
		builder.append("}");
	}

	private static void appendContent(StringBuilder builder, List<PropertyWrapper> ppws, boolean isImpl, boolean isOnlyJson)
	{

		for (int i = 0, j = ppws.size(); i < j; i++)
		{
			PropertyWrapper ppw = ppws.get(i);
			if (isImpl)
			{
				appendImplMethod(builder, ppw, isOnlyJson);
			} else
			{
				appendInterfaceMethod(builder, ppw);
			}
		}

	}

	private static void appendImplMethod(StringBuilder builder, PropertyWrapper ppw, boolean isOnlyJson)
	{
		StringFormat.appendAnnotation(builder, ppw);
		appendImplMethodHead(builder, ppw);
		appendStart(builder);
		String[] params = StringFormat.getParams(ppw.getMethodArg());
		if (isOnlyJson)
		{
			buildJson(builder, params);
			buildNameValuePair(builder);
			addPair(builder, "json", "jsonObject.toString()");
		} else
		{
			buildNameValuePair(builder);
			for (int i = 0; i < params.length; i++)
			{
				addPair(builder, params[i], params[i]);
			}
		}
		addPair(builder, "requestCode", "\"" + ppw.getRequestCode() + "\"");
		appendNetVisitor(builder, ppw);
		appendReturn(builder, ppw);
		appendEnd(builder);

	}

	private static void appendReturn(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("return result;");
	}

	private static void appendImplMethodHead(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("public ").append(ppw.getMethodReturn()).append(" ").append(ppw.getMethodName()).append("(")
				.append(ppw.getMethodArg()).append(")throws Exception");
	}

	private static void appendStart(StringBuilder builder)
	{
		builder.append("{");
	}

	/**
	 * 第一个参数为url(从xml获取),第二个参数为values
	 * 
	 * @param builder
	 * @param ppw
	 */
	private static void appendNetVisitor(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("String result=");
		builder.append(ppw.getNetProperty()).append("(").append("UrlBean.").append(StringFormat.getFinalPrimaryParam(ppw.getRequestCode())+"_URL").append(",").append("values")
				.append(");");
	}

	private static void addPair(StringBuilder builder, String key, String value)
	{
		String trim = key.trim();
		System.out.println(trim);
		builder.append("values.add(new BasicNameValuePair(").append("\"").append(key).append("\",").append(value)
				.append("));");
	}

	// 建json 值 jsonObject
	private static void buildNameValuePair(StringBuilder builder)
	{
		builder.append("List<NameValuePair> values = new ArrayList<NameValuePair>();");
	}

	private static void buildJson(StringBuilder builder, String[] params)
	{
		builder.append("JsonObject jsonObject=new JsonObject();");
		for (String str : params)
		{
			putString(builder, str);
		}

	}

	private static void putString(StringBuilder builder, String str)
	{
		builder.append("jsonObject.addProperty(");
		builder.append("\"" + str + "\",").append(str);
		builder.append(");");
	}

	private static void appendInterfaceMethod(StringBuilder builder, PropertyWrapper ppw)
	{
		StringFormat.appendAnnotation(builder, ppw);
		builder.append("public ").append(ppw.getMethodReturn()).append(" ").append(ppw.getMethodName()).append("(")
				.append(ppw.getMethodArg()).append(")throws Exception;\n");
	}

	private static void appendHeader(StringBuilder builder, boolean isImpl)
	{
		builder.append("public  ");
		if (isImpl)
		{
			builder.append(" class InterfaceImpl implements Interface");
		} else
		{
			builder.append(" interface Interface ");
		}
		appendStart(builder);
	}

}

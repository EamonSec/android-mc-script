package com.fran.xml.build;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fran.xml.StringFormat;
import com.fran.xml.xmldecode.PropertyDecoder;
import com.fran.xml.xmldecode.PropertyWrapper;

public class CallbackBuilder {

	public static void build(String propertyPath, String outputFile) throws Exception
	{
		List<PropertyWrapper> ppws = PropertyDecoder.getDecoder().getPropertyWrapper(propertyPath);
		String buildCallbackText = buildCallbackText(ppws.get(0));
		StringFormat.output(buildCallbackText, outputFile+"CallBack.java");
	}

	public static void  build(List<PropertyWrapper> ppws,String outputFile) throws IOException{
		String buildCallbackText = buildCallbackText(ppws.get(0));
		StringFormat.output(buildCallbackText, outputFile+"CallBack.java");
	}
	
	private static String buildCallbackText(PropertyWrapper ppw)
	{
		StringBuilder builder = new StringBuilder();
		String callbackName = ppw.getCallback();
		String callbackPkg = ppw.getCallbackPkg();
		buildText(builder, callbackName, callbackPkg);
		return builder.toString();
	}

	private static void buildText(StringBuilder builder, String callbackName, String callbackPkg)
	{
		appendPkg(builder, callbackPkg);
		appendHeader(builder);
		StringFormat.appendStart(builder);
		appendCallbackInterface(builder, callbackName);
		StringFormat.appendEnd(builder);

	}

	private static void appendCallbackInterface(StringBuilder builder, String callbackName)
	{
		StringFormat.appendAnnotation(builder,"在获取完数据后回调","3");
		builder.append("public interface ").append(callbackName);
		StringFormat.appendStart(builder);
		builder.append("void success(Object obj);").append("\n").append("void fail(String msg,int status);");
		StringFormat.appendEnd(builder);
	}

	@Test
	public void fun() throws Exception
	{
		build("/Users/apple/Documents/myWork/JavaUtil/src/property.xml", "/Users/apple/Desktop/temp/");	}

	private static void appendHeader(StringBuilder builder)
	{
		builder.append("public class ").append("CallBack");
	}

	private static void appendPkg(StringBuilder builder, String callbackPkg)
	{
		builder.append("package ").append(callbackPkg).append(";");
	}


}

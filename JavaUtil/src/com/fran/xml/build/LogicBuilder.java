package com.fran.xml.build;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fran.xml.StringFormat;
import com.fran.xml.xmldecode.PropertyDecoder;
import com.fran.xml.xmldecode.PropertyWrapper;

public class LogicBuilder {
	@Test
	public void fiun() throws Exception
	{
		build("/Users/apple/Documents/myWork/JavaUtil/src/property.xml", "/Users/apple/Desktop/temp/");
	}

	public static void build(String propertyPath, String outputPath) throws Exception
	{
		List<PropertyWrapper> ppw = PropertyDecoder.getDecoder().getPropertyWrapper(propertyPath);
		String logicContent = getContent(ppw);
		StringFormat.outPut(logicContent,"Surface", outputPath);
	}

	public static void build(List<PropertyWrapper> ppws,String outputPath) throws IOException{
		String logicContent = getContent(ppws);
		StringFormat.outPut(logicContent,"Surface", outputPath);
	}
	
	private static String getContent(List<PropertyWrapper> ppws)
	{
		StringBuilder builder = new StringBuilder();
		appendHead(builder);
		for (int i = 0, j = ppws.size(); i < j; i++)
		{
			PropertyWrapper ppw = ppws.get(i);
			appendSurfaceMethod(builder, ppw);
		}
		StringFormat.appendEnd(builder);
		return builder.toString();
	}

	private static void appendHead(StringBuilder builder)
	{
		builder.append("public class Surface ");
		StringFormat.appendStart(builder);
		builder.append("private static Surface surface;");
		builder.append("public static Surface get(){if(surface==null){surface=new Surface();}return surface;}");
		
	}

	private static void appendSurfaceMethod(StringBuilder builder, PropertyWrapper ppw)
	{
		StringFormat.appendAnnotation(builder, ppw);
		builder.append("public void ");
		builder.append(StringFormat.toLowerPrimary(ppw.getRequestCode())).append("(").append(ppw.getMethodArg())
				.append(",").append(ppw.getCallback()).append(" ")
				.append(StringFormat.toLowerPrimary(ppw.getCallback())).append(")");
		StringFormat.appendStart(builder);
		builder.append("new ").append(TaskBuilder.getTaskName(ppw)).append("(")
				.append(StringFormat.toLowerPrimary(ppw.getCallback())).append(").execute(");
		String pureParams = StringFormat.getPureParams(ppw.getMethodArg());
		if (pureParams != null)
		{
			builder.append(pureParams);
		}
		builder.append(");");
		StringFormat.appendEnd(builder);
	}

	@Test
	public void fun()
	{

	}
}

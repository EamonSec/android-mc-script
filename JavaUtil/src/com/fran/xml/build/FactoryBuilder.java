package com.fran.xml.build;

import java.util.List;
import com.fran.xml.StringFormat;
import com.fran.xml.xmldecode.PropertyWrapper;

public class FactoryBuilder {

	public static void build(List<PropertyWrapper> ppws, String outPutFile) throws Exception
	{
		String contentText = build(ppws);
		StringFormat.output(contentText, outPutFile + ppws.get(0).getFactoryName() + ".java");
	}

	private static String build(List<PropertyWrapper> ppws)
	{
		StringBuilder builder = new StringBuilder();
		appendHeader(builder, ppws.get(0));
		StringFormat.appendStart(builder);
		appendFactoryInstance(builder, ppws.get(0));
		appendInterfaceField(builder, ppws.get(0));
		appendInterfaceIGetmplMethod(builder, ppws.get(0));
		StringFormat.appendEnd(builder);
		return builder.toString();
	}

	private static void appendInterfaceIGetmplMethod(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("public static Interface ").append(StringFormat.GET_INTERFACE_INSTANCE_NAME).append("(){")
				.append("if(mInterface==null)mInterface=new InterfaceImpl();").append("return mInterface;}");

	}

	private static void appendInterfaceField(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("private static ").append("Interface ").append(" ").append(" mInterface").append(";");
	}

	private static void appendHeader(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("package ").append(ppw.getFactoryPkg()).append(";");
		appendline(builder);
		builder.append("public class ").append(ppw.getFactoryName());

	}

	private static void appendline(StringBuilder builder)
	{
		builder.append("\n");
	}

	private static void appendFactoryInstance(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("private static ").append(ppw.getFactoryName()).append(" ").append("factory").append(";\n")
				.append("public static ").append(ppw.getFactoryName()).append(" ")
				.append(StringFormat.GET_FACTORY_INSTANCE_NAME).append("(){");
		builder.append("if(factory==null)factory=new ").append(ppw.getFactoryName()).append("();\n")
				.append("return factory;}");
	}

	public static final String IMPL = "mInterface";
}

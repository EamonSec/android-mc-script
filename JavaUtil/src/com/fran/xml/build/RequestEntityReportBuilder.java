package com.fran.xml.build;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fran.xml.StringFormat;
import com.fran.xml.xmldecode.PropertyDecoder;
import com.fran.xml.xmldecode.PropertyWrapper;

public class RequestEntityReportBuilder {

	@Test
	public void fun() throws Exception{
		build("/Users/apple/Documents/myWork/JavaUtil/src/property.xml", "/Users/apple/Desktop/temp/");
	}
	
	public static void build(String propertyPath, String outputPath) throws Exception{
		List<PropertyWrapper> ppws = PropertyDecoder.getDecoder().getPropertyWrapper(propertyPath);
		String report=buildReporter(ppws);
		StringFormat.output(report, outputPath+"report.txt");
	}

	public static void build(List<PropertyWrapper> ppws,String outPutPath) throws IOException{
		String report=buildReporter(ppws);
		StringFormat.output(report, outPutPath+"report.txt");
	}
	private static String buildReporter(List<PropertyWrapper> ppws)
	{
		StringBuilder builder=new StringBuilder();
		appendHeader(builder);
		builder.append("--------------------我是开始线----------------------------");
		appendLine(builder);
		for(int i=0;i<ppws.size();i++){
			appendUpon(builder,ppws.get(i));
			appendLine(builder);
			appendLine(builder);
		}
		builder.append("--------------------我是结束线----------------------------");
		return builder.toString();
	}

	private static void appendUpon(StringBuilder builder, PropertyWrapper ppw)
	{
		String requestCode = ppw.getRequestCode();
		String resultEntity = ppw.getResultEntity();
		String resultType = ppw.getResultType();
		String resultContainType = ppw.getResultContainType();
		buildupon(builder,requestCode,resultEntity,resultType,resultContainType);
		
		
		
	}

	private static void buildupon(StringBuilder builder, String requestCode, String resultEntity, String resultType, String resultContainType)
	{
			builder.append(requestCode)
			.append("------------------");
			if(resultType.equals("1")){
				//单个实体
				builder.append(resultEntity);
			}else{
				//集合
				builder.append(resultContainType)
				.append("<")
				.append(resultEntity)
				.append(">");
			}
			
	}

	private static void appendHeader(StringBuilder builder)
	{
			builder.append("请求码对应回调返回实体");
			appendLine(builder);
	}

	private static void appendLine(StringBuilder builder)
	{
		builder.append("\n");
	}
	
}

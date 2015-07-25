package com.fran.xml;

import java.util.List;

import com.fran.xml.build.BaseNetVisitorBuilder;
import com.fran.xml.build.CallbackBuilder;
import com.fran.xml.build.FactoryBuilder;
import com.fran.xml.build.LogicBuilder;
import com.fran.xml.build.OnclickBuild;
import com.fran.xml.build.RequestEntityReportBuilder;
import com.fran.xml.build.TaskBuilder;
import com.fran.xml.build.UrlBeanBuild;
import com.fran.xml.xmldecode.PropertyDecoder;
import com.fran.xml.xmldecode.PropertyWrapper;

public class BuildAll {

	public static void main(String[] args)
	{
		try
		{
			buildAll("/Users/apple/Desktop/temp/property.xml", "/Users/apple/Desktop/temp/");
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void buildAll(String propertyPath,String outputPath) throws Exception{
		List<PropertyWrapper> ppws = PropertyDecoder.getDecoder().getPropertyWrapper(propertyPath);
		FactoryBuilder.build(ppws, outputPath);
		BaseNetVisitorBuilder.build(ppws, outputPath, true);
		CallbackBuilder.build(ppws, outputPath);
		LogicBuilder.build(ppws, outputPath);
		OnclickBuild.bulid(ppws, outputPath);
		RequestEntityReportBuilder.build(ppws, outputPath);
		TaskBuilder.builde(ppws, outputPath);
		UrlBeanBuild.build(ppws, outputPath);
	}

}

package com.fran.xml.build;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.fran.xml.StringFormat;
import com.fran.xml.xmldecode.PropertyDecoder;
import com.fran.xml.xmldecode.PropertyWrapper;

public class OnclickBuild {

	@Test
	public void fun() throws Exception
	{
		build("/Users/apple/Documents/myWork/JavaUtil/src/property.xml", "/Users/apple/Desktop/temp/");
	}

	public static void build(String propertyPath, String outputPath) throws Exception
	{
		List<PropertyWrapper> ppws = PropertyDecoder.getDecoder().getPropertyWrapper(propertyPath);
		String xmlTestText = getXmlTest(ppws);
		String onClickText = getOnClickText(ppws);
		StringFormat.output(xmlTestText, outputPath + "onclick_test.xml");
		StringFormat.output(onClickText, outputPath + "TestOnClickActivity.java");
	}

	public static void bulid(List<PropertyWrapper> ppws, String outputPath) throws IOException
	{
		String xmlTestText = getXmlTest(ppws);
		String onClickText = getOnClickText(ppws);
		StringFormat.output(xmlTestText, outputPath + "onclick_test.xml");
		StringFormat.output(onClickText, outputPath + "TestOnClickActivity.java");
	}

	private static String getOnClickText(List<PropertyWrapper> ppws)
	{
		StringBuilder builder = new StringBuilder();
		for (PropertyWrapper ppw : ppws)
		{
			appendOnclickMethod(builder, ppw);
		}
		return builder.toString();
	}

	private static void appendOnclickMethod(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("public void ").append(ppw.getMethodName()).append("(").append("View view").append(")");
		StringFormat.appendStart(builder);
		builder.append("").append("Surface.get().").append(ppw.getMethodName()).append("(");
		int size = getArgSize(ppw);
		for (int i = 0; i < size; i++)
		{
			builder.append(" \"test" + i + "\"");
			builder.append(",");
		}
		builder.append("this);");
		StringFormat.appendEnd(builder);
	}

	private static int getArgSize(PropertyWrapper ppw)
	{
		String methodArg = ppw.getMethodArg();
		String[] split = methodArg.split(",");
		return split.length;
	}

	private static String getXmlTest(List<PropertyWrapper> ppws)
	{

		StringBuilder builder = new StringBuilder();
		appendXmlHeader(builder);
		for (PropertyWrapper ppw : ppws)
		{
			appendButton(builder, ppw);
		}
		appendXmlFooter(builder);
		return builder.toString();
	}

	private static void appendButton(StringBuilder builder, PropertyWrapper ppw)
	{
		builder.append("<Button  android:layout_width=\"match_parent\" android:layout_height=\"wrap_content\"");
		builder.append("android:onClick=\"").append(ppw.getMethodName()).append("\"\n").append("android:text=\"")
				.append(ppw.getMethodName()).append("\"\n").append("/>");

	}

	private static void appendXmlFooter(StringBuilder builder)
	{
		builder.append("</LinearLayout></ScrollView>");

	}

	private static void appendXmlHeader(StringBuilder builder)
	{
		builder.append(
				"<ScrollView xmlns:android=\"http://schemas.android.com/apk/res/android\" xmlns:tools=\"http://schemas.android.com/tools\" android:layout_width=\"match_parent\" android:layout_height=\"match_parent\" android:orientation=\"vertical\" android:paddingBottom=\"@dimen/activity_vertical_margin\" android:paddingLeft=\"@dimen/activity_horizontal_margin\" android:paddingRight=\"@dimen/activity_horizontal_margin\" android:paddingTop=\"@dimen/activity_vertical_margin\"tools:context=\"com.umejustan.androidbginterfacetest.MainActivity\" ><LinearLayout  android:layout_width=\"match_parent\" android:layout_height=\"wrap_content\" android:orientation=\"vertical\">");

	}

}

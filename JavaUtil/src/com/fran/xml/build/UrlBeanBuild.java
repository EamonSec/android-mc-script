package com.fran.xml.build;

import java.io.IOException;
import java.util.List;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.fran.xml.StringFormat;
import com.fran.xml.xmldecode.PropertyWrapper;

public class UrlBeanBuild {

	public static void build(List<PropertyWrapper> ppws, String outputPath) throws IOException
	{
		String logicContent = getContent(ppws);
		StringFormat.output(logicContent, outputPath+"UrlBean.java");
	}

	private static String getContent(List<PropertyWrapper> ppws)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("public class UrlBean");
		StringFormat.appendStart(builder);
		builder.append("public static final String BASE_URL=").append("\"").append(ppws.get(0).getBaseUrl())
				.append("\";\n");
		for (PropertyWrapper ppw : ppws)
		{
			appendUrl(builder, ppw);
		}
		StringFormat.appendEnd(builder);

		return builder.toString();
	}

	private static void appendUrl(StringBuilder builder, PropertyWrapper ppw)
	{
		StringFormat.appendAnnotation(builder, ppw);
		builder.append("public static final String ")
				.append(StringFormat.getFinalPrimaryParam(ppw.getRequestCode())+"_URL=").append("")
				.append("BASE_URL+\"").append(ppw.getNetUrl()).append("\";\n");
	}

}

package com.fran.xml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.fran.xml.xmldecode.PropertyWrapper;

public class StringFormat {
	/**
	 * 将stringFormat转成STRING_FORMAT
	 * 
	 * @param param
	 * @return
	 */
	public static String getFinalPrimaryParam(String param)
	{
		StringBuffer sb = new StringBuffer(param);
		Matcher matcher = Pattern.compile("[A-Z]").matcher(param);
		int tempIndex = 0;
		while (matcher.find())
		{
			int start = matcher.start();
			if (start == 0)
				continue;
			sb.insert(start + tempIndex, "_");
			tempIndex++;
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 首字符大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toUpPrimary(String str)
	{
		String substring = str.substring(1);
		String substring2 = str.substring(0, 1).toUpperCase();
		return substring2 + substring;
	}

	/**
	 * 首字符小写
	 * 
	 * @param str
	 * @return
	 */
	public static String toLowerPrimary(String str)
	{
		String substring = str.substring(1);
		String substring2 = str.substring(0, 1).toLowerCase();
		return substring2 + substring;
	}

	/**
	 * 首字符大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toUpAllPrimaryName(String taskName)
	{
		String substring = taskName.substring(0, 1);
		String substring2 = taskName.substring(1);
		String upperCase = substring.toUpperCase();
		String name = upperCase + substring2;
		return name;
	}

	/**
	 * 将括号里面的所有参数值名字提取出来
	 * 
	 * @param param
	 * @return
	 */
	public static String getParamsString(String param)
	{
		if (param == null || param.equals(""))
		{
			return "";
		}
		StringBuilder argsBuilder = new StringBuilder();
		String[] split = param.split(",");

		for (int i = 0; i < split.length; i++)
		{
			argsBuilder.append(filterArgs(split[i]));
			if (i == split.length - 1)
			{
				argsBuilder.append("");
			} else
			{
				argsBuilder.append(",");
			}
		}

		return argsBuilder.toString();
	}

	/**
	 * 将参数提取成数组再返回"String str,long lon"-->[str,lon]
	 * 
	 * @param string
	 * @return
	 */
	public static String[] getParams(String string)
	{
		String[] split = string.split(",");
		String[] args = new String[split.length];
		for (int i = 0; i < split.length; i++)
		{
			args[i] = filterArgs(split[i], false).substring(1);
		}
		return args;

	}

	/**
	 * 将参数原型中的参数值字符提取出来 如 String str-->str
	 * 
	 * @param string
	 * @param boolean
	 *            是否要自动转换成String
	 * @return
	 */
	public static String filterArgs(String string, boolean isAddPix)
	{
		String temp = "";
		boolean isString = false;
		if (string.startsWith("Str"))
		{
			temp = string.substring(6);
			isString = true;
		} else if (string.startsWith("int"))
		{
			isString = false;
			temp = string.substring(3);

		} else if (string.startsWith("long"))
		{
			isString = false;
			temp = string.substring(4);
		} else
		{
			isString = true;
			temp = "哦？告诉我你的基本类型？";
		}
		if (!isString && isAddPix)
		{
			temp = "String.valueOf(" + temp + ")";
		}

		return temp;
	}

	/**
	 * 将参数原型中的参数值字符提取出来,并将其他类型的变量转换成String int i-->String.valueOf(i)
	 * 
	 * @param string
	 * @return
	 */
	public static String filterArgs(String string)
	{
		return filterArgs(string, true);
	}

	public static String[] filterArgs(boolean isAddPix, String... string)
	{
		String[] strs = new String[string.length];
		for (int i = 0; i < string.length; i++)
		{
			strs[i] = filterArgs(string[i], isAddPix);
		}
		return strs;
	}

	/**
	 * String str,String str-->str,str
	 * 
	 * @param str
	 * @return
	 */
	public static String getPureParams(String str)
	{
		String[] split = str.split(",");
		StringBuilder builder = new StringBuilder();
		boolean isLast = false;
		for (int i = 0; i < split.length; i++)
		{
			String arg = split[i];
			String filterArgs = filterArgs(arg, false);
			builder.append(filterArgs);
			if (i == split.length - 1)
			{
				isLast = true;
			}
			if (!isLast)
			{
				builder.append(",");
			}

		}
		return builder.toString();
	}

	@Test
	public void fun()
	{
		String pureParams = getPureParams("String str,long str");

		System.out.println(pureParams);
	}

	/**
	 * 添加注释
	 * 
	 * @param builder
	 * @param ppw
	 */
	public static void appendAnnotation(StringBuilder builder, PropertyWrapper ppw)
	{
		appendAnnotation(builder, ppw.getDescribe(), ppw.getLevel());
	}

	public static void appendAnnotation(StringBuilder builder, String text, String level)
	{
		switch (level)
			{
			case "1":
				builder.append("//").append(text);
				break;
			case "2":
				builder.append("/*").append(text).append("*/");
				break;
			case "3":
				builder.append("/**").append(text).append("*/");
				break;
			default:
				builder.append("/**").append(text).append("*/");
				break;
			}

	}

	@Test
	public void fun2()
	{
		StringBuilder builder = new StringBuilder();
		appendConstruch("FinView", "Onfetched", builder);
		System.out.println(builder.toString());
	}

	public static void appendMethod(StringBuilder builder, String methodType, String returnType, String methodName,
			String throwz, String... methodArgs)
	{
		builder.append(methodType).append(" " + returnType).append(" " + methodName).append(" (");
		appendArgs(builder, methodArgs);
		builder.append(")");
		if (throwz != null)
		{
			builder.append("throws " + throwz);
		}

		builder.append("\n{return null;}");
	}

	public static void appendMethod(StringBuilder builder, String methodType, String returnType, String methodName,
			String throwz, String methodArgs)
	{
		if (throwz == null)
		{
			throwz = "RuntimeException";
		}
		builder.append(methodType).append(" " + returnType).append(" " + methodName).append(" (").append(methodArgs)
				.append(")throws ").append(throwz).append("\n");
	}

	public static final String GET_FACTORY_INSTANCE_NAME = "getFactory";
	public static final String GET_INTERFACE_INSTANCE_NAME = "getImpl";

	/**
	 * 从数组中拼接字符串参数
	 * 
	 * @param builder
	 * @param methodArgs
	 */
	public static void appendArgs(StringBuilder builder, String[] methodArgs)
	{
		String argType = "String ";
		if (methodArgs.length == 0)
			return;
		boolean isLast = false;
		for (int i = 0; i < methodArgs.length; i++)
		{
			builder.append(argType + methodArgs[i]);
			if (i == methodArgs.length - 1)
			{
				isLast = true;
			} else
			{
				isLast = false;
			}
			if (!isLast)
			{
				builder.append(",");
			}
		}
	}

	/**
	 * 添加一个构造器，包含一个回调
	 * 
	 * @param className
	 * @param callBackName
	 * @param builder
	 */
	public static void appendConstruch(String className, String callBackName, StringBuilder builder)
	{
		builder.append("public ").append(className).append("(").append(callBackName).append(" ")
				.append(toLowerPrimary(callBackName)).append("){").append("this.").append(toLowerPrimary(callBackName))
				.append("=").append(toLowerPrimary(callBackName)).append(";}");
	}

	public static void appendStart(StringBuilder builder)
	{
		builder.append("{");
	}

	public static void appendEnd(StringBuilder builder)
	{
		builder.append("}");
	}

	/**
	 * 给StirngBuilder增加一个Field字段
	 * 
	 * @param type
	 *            类型private
	 * @param typeName
	 *            类型 String
	 * @param fieldName
	 *            字段名
	 * @param builder
	 */
	public static void appendField(String type, String typeName, String fieldName, StringBuilder builder)
	{
		builder.append(type).append(" " + typeName).append(" " + StringFormat.toLowerPrimary(fieldName)).append(";");
	}

	public static void outPut(String text, String name, String outPutPath) throws IOException
	{

		String file = outPutPath + name + ".java";
		FileWriter writer = new FileWriter(file);
		writer.write(text);
		writer.flush();
		writer.close();
	}

	public static void output(String text, String outputPath) throws IOException
	{
		FileWriter writer = new FileWriter(outputPath);
		writer.write(text);
		writer.flush();
		writer.close();
	}

	public static void appendLog(StringBuilder builder, String log, String text)
	{
		builder.append("Log.i(\"").append(log).append("\",\"\"+").append(text).append(");");
	}

}

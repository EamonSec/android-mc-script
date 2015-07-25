package com.fran.xml.xmldecode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * xml解析器
 * 
 * @author zz
 *
 */
public class PropertyDecoder implements IPropertyDecoder{
	public PropertyDecoder() {
		// TODO Auto-generated constructor stub
	}

	private static PropertyDecoder decoder;

	public static PropertyDecoder getDecoder()
	{
		if (decoder == null)
		{
			decoder = new PropertyDecoder();
		}
		return decoder;
	}

	/**
	 * 解析xml文件
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public List<PropertyWrapper> getPropertyWrapper(String fileName) throws Exception
	{
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(fileName));
		Element root = document.getRootElement();
		return getProperty(root);
	}

	private List<PropertyWrapper> getProperty(Element root) throws Exception
	{
		List<PropertyWrapper> ppws = new ArrayList<>();
		Iterator<?> eleIterator = root.elementIterator();
		while (eleIterator.hasNext())
		{
			Element ele = (Element) eleIterator.next();
			ppws.add(getPropertyWrapper(ele));
		}
		return ppws;

	}

	/**
	 * 从子节点中获得property对象
	 * 
	 * @param childElement
	 * @return
	 * @throws Exception
	 */
	private PropertyWrapper getPropertyWrapper(Element childElement) throws Exception
	{
		Iterator<?> elementIterator = childElement.elementIterator();
		PropertyWrapper wrapper = new PropertyWrapper();
		while (elementIterator.hasNext())
		{
			Element ele = (Element) elementIterator.next();
			String name = ele.getName();
			switch (name)
				{
				case Bean.CALL_BACK:
					initCallBack(wrapper, ele);
					break;
				case Bean.DESCRIBE:
					initDescribe(wrapper, ele);
					break;
				case Bean.FACTORY:
					initFactory(wrapper, ele);
					break;
				case Bean.METHOD:
					initMethod(wrapper, ele);
					break;
				case Bean.NET:
					initNet(wrapper, ele);
					break;
				case Bean.REQUESTCODE:
					initRequestCode(wrapper, ele);
					break;
				case Bean.TASK:
					initTask(wrapper, ele);
					break;
				case Bean.PACKAGE:
					initPackage(wrapper, ele);
					break;
				default:
					throw new Exception("不存在的属性：" + name);
				}
		}
		return wrapper;
	}

	private void initPackage(PropertyWrapper wrapper, Element ele)
	{
		wrapper.setPackageName(ele.getStringValue().trim());
	}

	private void initCallBack(PropertyWrapper wrapper, Element ele)
	{
		wrapper.setCallbackName(ele.attribute(0).getValue().trim());
		wrapper.setCallbackPkg( ele.attribute(1).getValue().trim());
	}

	private void initFactory(PropertyWrapper wrapper, Element ele)
	{
		wrapper.setFactoryName(ele.attribute(0).getValue().trim());
		wrapper.setFactoryPkg(ele.attribute(1).getValue());
	}

	private void initTask(PropertyWrapper wrapper, Element ele)
	{
		wrapper.setTaskPkg(ele.attribute(0).getValue().trim());
		wrapper.setTaskClassName( ele.attribute(1).getValue().trim());
		wrapper.setResultType(ele.attribute(2).getValue().trim());
		wrapper.setResultEntity(ele.attribute(3).getValue().trim());
		String value = ele.attribute(4).getValue();
		wrapper.setResultContainType(value);
	}

	private void initNet(PropertyWrapper wrapper, Element ele)
	{
		wrapper.setBaseUrl(ele.attribute(1).getValue().trim());
		wrapper.setNetUrl( ele.attribute(0).getValue().trim());
		wrapper.setNetProperty(ele.getStringValue().trim());
	}

	private void initMethod(PropertyWrapper wrapper, Element ele) throws Exception
	{
		List<?> attributes = ele.attributes();
		for (Object attr : attributes)
		{
			Attribute attribute = (Attribute) attr;
			String name = attribute.getName().trim();
			String value = attribute.getValue().trim();
			switch (name)
				{
				case Bean.NAME:
					wrapper.setMethodName(value);
					break;
				case Bean.ARGS:
					wrapper.setMethodArg(value);
					break;
				case Bean.RETURN:
					wrapper.setMethodReturn(value);
					break;
				default:
					throw new Exception("无效的属性:" + name);
				}

		}

	}

	private void initRequestCode(PropertyWrapper wrapper, Element ele)
	{
		String stringValue = ele.getStringValue();
		wrapper.setRequestCode(stringValue.trim());
	}

	private void initDescribe(PropertyWrapper wrapper, Element ele)
	{
		String stringValue = ele.getStringValue();
		String value = ele.attribute(0).getValue();
		wrapper.setDescribe(stringValue.trim());
		wrapper.setLevel(value.trim());
	}

	public static void outPut(List<StringBuilder> taskBuilder, List<String> fileNames, String path) throws IOException
	{
		for (int i = 0, j = taskBuilder.size(); i < j; i++)
		{
			StringBuilder tBuilder = taskBuilder.get(i);
			File file = new File(path + "" + fileNames.get(i) + ".java");
			FileWriter writer = new FileWriter(file);
			writer.write(tBuilder.toString());
			writer.flush();
			writer.close();
		}
	}
	
	public static class Bean {
		public static final String DESCRIBE = "describe";
		public static final String REQUESTCODE = "requestCode";
		public static final String METHOD = "method";
		public static final String CALL_BACK = "callBack";
		public static final String TASK = "task";
		public static final String FACTORY = "factory";
		public static final String NET = "net";
		public static final String PACKAGE = "package";

		public static final String NAME = "name";
		public static final String ARGS = "args";
		public static final String RETURN = "return";

	}

}

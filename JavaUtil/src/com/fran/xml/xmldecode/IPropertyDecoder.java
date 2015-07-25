package com.fran.xml.xmldecode;

import java.util.List;

public interface IPropertyDecoder {
	/**
	 * 解析xml文件
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public List<PropertyWrapper> getPropertyWrapper(String fileName) throws Exception;
}

package com.fran.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

public class ClassUtil {

	public List<String> getAllClassesName(Class<?> clazz) throws ClassNotFoundException, IOException {
		List<Class<?>> allClasses = getAllClasses(clazz);
		List<String> classNames = new ArrayList<String>();
		for (Class<?> claz : allClasses) {
			String simpleName = claz.getSimpleName();
			classNames.add(simpleName);
		}
		classNames.add("3");
		return classNames;
	}

	public List<Class<?>> getAllClasses(Class<?> clazz) throws ClassNotFoundException, IOException {
		String pkgName = clazz.getPackage().getName();
		List<Class<?>> classes = getClasses(pkgName);
		return classes;
	}

	private List<Class<?>> getClasses(String pkgName) throws IOException, ClassNotFoundException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = pkgName.replace(".", "/");
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<>();
		while (resources.hasMoreElements()) {
			URL nextElement = resources.nextElement();
			dirs.add(new File(nextElement.getFile()));
		}
		ArrayList<Class<?>> classes = new ArrayList<>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, pkgName));
		}
		return classes;
	}

	private Collection<? extends Class<?>> findClasses(File directory, String pkgName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert!file.getName().contains(".");
				classes.addAll(findClasses(directory, pkgName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(pkgName + "." + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
}

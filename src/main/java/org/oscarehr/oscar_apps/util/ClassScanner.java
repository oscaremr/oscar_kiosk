package org.oscarehr.oscar_apps.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

/**
 * This is a utility class to scan for classes in your classpath.
 * Be careful using this utility. In general you should not
 * be Class.forName-ing everything you find or otherwise 
 * loading every class you see as it will take up unnecessary
 * memory. Having said that, unless you do bye code introspection,
 * it is the only "reasonable" way and maybe ok if you happen to
 * know the classes are used anyways or if the listing is small. 
 */
public class ClassScanner
{
	private static final Logger logger = MiscUtils.getLogger();

	/**
	 * Find classes in same package as this class
	 * @throws IOException 
	 */
	public static ArrayList<String> findClassesInSamePackage(Class<?> c) throws IOException
	{
		String className = c.getName();
		String packageName = className.substring(0, className.lastIndexOf('.'));
		return(findClassesByPackageName(packageName));
	}

	public static ArrayList<String> findClassesByPackageName(String packageName) throws IOException
	{
		ArrayList<String> results = new ArrayList<String>();
		ClassLoader cl = ClassScanner.class.getClassLoader();
		String directoryName = packageName.replace('.', '/');

		Enumeration<URL> urls = cl.getResources(directoryName);
		while (urls.hasMoreElements())
		{
			URL url = urls.nextElement();
			String protocol = url.getProtocol();

			if ("file".equals(protocol)) addFileResults(results, packageName, url);
			else if ("jar".equals(protocol)) addJarResults(results, directoryName, url);
			else logger.error("Doh! I missed a protocol type : " + url.toString());
		}

		return(results);
	}

	private static void addJarResults(ArrayList<String> results, String directoryName, URL url) throws IOException
	{
		// file = file:/home/tedman/.m2/repository/org/apache/james/apache-mime4j/0.6/apache-mime4j-0.6.jar!/org/apache/james/mime4j/codec
		// we want just the middle portion 
		String urlFileName = url.getFile();
		String jarFileName = urlFileName.substring("file:".length(), urlFileName.length() - (directoryName.length() + 2));

		JarFile jf = new JarFile(jarFileName);
		Enumeration<JarEntry> list = jf.entries();
		while (list.hasMoreElements())
		{
			JarEntry entry = list.nextElement();
			if (entry.isDirectory()) continue;

			String fileName = entry.getName();
			if (fileName.startsWith(directoryName) && fileName.endsWith(".class"))
			{
				String tempName=entry.getName();
				tempName=tempName.substring(0,tempName.length()-".class".length());
				tempName=tempName.replace('/', '.');
				results.add(tempName);
			}
		}
	}

	private static void addFileResults(ArrayList<String> results, String packageName, URL url)
	{
		File packageDirectory = new File(url.getFile());
		File[] list = packageDirectory.listFiles();
		for (File f : list)
		{
			if (f.isFile() && f.getName().endsWith(".class"))
			{
				String tempName=f.getName();
				tempName=tempName.substring(0,tempName.length()-".class".length());
				tempName=packageName + '.' +tempName;
				results.add(tempName);
			}
		}
	}

	public static void main(String... argv) throws Exception
	{
		ArrayList<String> result = findClassesInSamePackage(ClassScanner.class);
		for (String s : result)
		{
			logger.info("1:" + s);
		}
		
		result = findClassesByPackageName("org.apache.james.mime4j.codec");
		for (String s : result)
		{
			logger.info("2:" + s);
		}

		result = findClassesByPackageName("org.oscarehr.oscar_apps.check_in.card_parsers");
		for (String s : result)
		{
			Class<?> c=Class.forName(s);
			logger.info("3:" + s + ":"+c.getSuperclass());
		}
	}
}

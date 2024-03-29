package javamop.logicpluginshells;

import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.io.*;
import java.net.URL;

import javamop.Main;
import javamop.MOPException;
import javamop.parser.logicrepositorysyntax.*;

public class LogicPluginShellFactory {

	static public LogicPluginShell findLogicShellPlugin(String packageName, String monitorType) {
		ArrayList<Class<?>> logicPlugins = null;
		try {
			/* it should return only subclasses of LogicShellPlugin */
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Class<?>[] pluginParamClass = {};

			if (Main.isJarFile)
				logicPlugins = getClassesFromJar(loader, packageName);
			else
				logicPlugins = getClasses(loader, packageName);
			if (logicPlugins != null) {
				for (Class<?> c : logicPlugins) {
					LogicPluginShell logicShellPlugin = (LogicPluginShell) c.getConstructor(pluginParamClass).newInstance();
					if (logicShellPlugin.monitorType.toLowerCase().compareTo(monitorType.toLowerCase()) == 0) {
						return logicShellPlugin;
					}
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	static private ArrayList<Class<?>> getClassesFromJar(ClassLoader loader, String packageName) throws MOPException {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		packageName = packageName.replaceAll("\\.", "/");

		try {
			JarInputStream jarFile = new JarInputStream(new FileInputStream(Main.jarFilePath));
			JarEntry jarEntry;

			while (true) {
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null) {
					break;
				}
				if ((jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class"))) {
					String className = jarEntry.getName().replaceAll("/", "\\.");
					className = className.substring(0, className.length() - ".class".length());
					Class<?> clazz = Class.forName(className);

					if (!clazz.isInterface()) {
						Class<?> superClass = clazz.getSuperclass();
						while (superClass != null) {
							if (superClass.getName() == "javamop.logicpluginshells.LogicPluginShell") {
								classes.add(clazz);
								break;
							}
							superClass = superClass.getSuperclass();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return classes;
	}

	static private ArrayList<Class<?>> getClasses(ClassLoader loader, String packageName) throws MOPException {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources;
		try {
			resources = loader.getResources(path);
		} catch (Exception e) {
			throw new MOPException("cannot load codeplugins");
		}

		if (resources != null) {
			while (resources.hasMoreElements()) {
				String filePath = resources.nextElement().getFile();
				// WINDOWS HACK
				if (filePath.indexOf("%20") > 0)
					filePath = filePath.replaceAll("%20", " ");
				if (filePath != null) {
					if (!(filePath.indexOf("!") > 0) && !(filePath.indexOf(".jar") > 0)) {
						try {
							classes.addAll(getFromDirectory(new File(filePath), packageName));
						} catch (Exception e) {
							throw new MOPException("cannot load codeplugins");
						}
					}
				}
			}
		}
		return classes;
	}

	static private ArrayList<Class<?>> getFromDirectory(File directory, String packageName) throws Exception {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
				if (file.getName().endsWith(".class")) {
					String name = packageName + '.' + stripFilenameExtension(file.getName());
					Class<?> clazz = null;
					try {
						clazz = Class.forName(name);
					} catch (Error e) {
						continue;
					}

					if (!clazz.isInterface()) {
						Class<?> superClass = clazz.getSuperclass();
						while (superClass != null) {
							if (superClass.getName() == "javamop.logicpluginshells.LogicPluginShell") {
								classes.add(clazz);
								break;
							}
							superClass = superClass.getSuperclass();
						}
					}
				} else if (file.list() != null) {
					classes.addAll(getFromDirectory(file, packageName + "." + stripFilenameExtension(file.getName())));
				}
			}
		}
		return classes;
	}

	static private String stripFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(".");
		return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
	}

	static public LogicPluginShellResult process(LogicRepositoryType logicOutput, String events) throws MOPException {
		LogicPluginShell logicShellPlugin = findLogicShellPlugin("javamop.logicpluginshells", logicOutput.getProperty().getLogic());
		if (logicShellPlugin != null) {
			LogicPluginShellResult result = logicShellPlugin.process(logicOutput, events);
			//Support deadlock detection since deadlock is not a state.
			result.properties.put("deadlock condition", "");
			return result;
		}
		else
			return null;
	}
}

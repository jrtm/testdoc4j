package no.uio.tools.testdoc.main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import no.uio.tools.testdoc.annotations.TestDocPlan;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import freemarker.template.TemplateException;

public class ReflectionsScanner {

    public static String classPath() {
        String classPath = "";
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
        for (int i = 0; i < urls.length; i++) {
            classPath = classPath + ":" + urls[i].getFile();
        }
        return classPath;
    }


    /* Add path to classpath at runtime */
    public static void addPath(String s) throws Exception {
        File f = new File(s);
        @SuppressWarnings("deprecation")
        URL u = f.toURL();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[] { URL.class });
        method.setAccessible(true);
        method.invoke(urlClassLoader, new Object[] { u });
    }


    public static List<Class> findAllTestDocClassesInClasspath(String excludeFilter) {
        List<Class> classes = new ArrayList();
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(
                ClasspathHelper.getUrlsForPackagePrefix("")).setScanners(new ResourcesScanner(),
                new TypeAnnotationsScanner(), new MethodAnnotationsScanner()));

        Set classesFound = reflections.getTypesAnnotatedWith(TestDocPlan.class);
        for (Iterator iterator = classesFound.iterator(); iterator.hasNext();) {
            Class clazz = (Class) iterator.next();
            if (!(clazz.getName().matches(excludeFilter))) {
                classes.add(clazz);
            }
        }
        return classes;
    }


    public static void main(String[] args) throws ClassNotFoundException, IOException, TemplateException {
        System.out.println("TestDoc: Generating documenation.");
        List<Class> classesFound = findAllTestDocClassesInClasspath(""); // no.uio.tools.testdoc.examples
        String html = GenerateTestDoc.generateTestDocForClasses(classesFound);
        GenerateTestDoc.writeFile("testplan.html", html);
        System.out.println("Output to 'testplan.html'.");
    }

}

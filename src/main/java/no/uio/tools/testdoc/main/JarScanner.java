package no.uio.tools.testdoc.main;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Scans jarfiles for
 * 
 * @author thomasfl
 * 
 */
public class JarScanner {

    private static boolean debug = false;


    public static void main(String[] args) {
        String jarName = "./target/testdoc.jar";
        String packageName = ""; // no.uio.tools.testdoc";
        List classes = getClasseNamesInPackage(jarName, packageName);
    }


    /*
     * Snatched from http://www.rgagnon.com/javadetails/java-0513.html
     */
    public static List getClasseNamesInPackage(String jarName, String packageName) {
        ArrayList classes = new ArrayList();

        packageName = packageName.replaceAll("\\.", "/");
        if (debug)
            System.out.println("Jar " + jarName + " looking for " + packageName);
        try {
            JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
            JarEntry jarEntry;

            while (true) {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                if ((jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class"))) {
                    if (debug)
                        System.out.println("Found " + jarEntry.getName().replaceAll("/", "\\."));
                    classes.add(jarEntry.getName().replaceAll("/", "\\."));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}

package no.uio.tools.testdoc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public abstract class JavapMethodLister implements MethodLister {

    private static final String DECOMPILER = "javap";

    private static JavapMethodLister instance;


    @Override
    public List<String> getMethodsForClass(final Class<?> clazz) {
        try {
            String[] javapArgs = getJavapProcessParametersForClass(clazz);

            Process javap = Runtime.getRuntime().exec(javapArgs);

            MethodIndexer indexer = new MethodIndexer(javap.getInputStream());
            List<String> methods = indexer.readMethods();
            if (methods.isEmpty()) {
                System.out.println("[WARNING] No methods found for class " + clazz);
            }
            return methods;
        } catch (IOException e) {
            System.out.println("[WARNING] Could not get methods for class " + clazz);
            return Collections.emptyList();
        }
    }


    protected URL getClassResource(final Class<?> clazz) {
        String classPackage = clazz.getPackage().getName();
        String className = clazz.getName().substring(classPackage.length() + 1);

        return clazz.getResource(className + ".class");
    }


    protected abstract String[] getJavapProcessParametersForClass(final Class<?> clazz);

    static class OracleJDK extends JavapMethodLister {

        @Override
        protected String[] getJavapProcessParametersForClass(final Class<?> clazz) {

            File classFile = getClassFile(clazz);

            String className = clazz.getSimpleName();
            String classPath = classFile.getParent();

            return new String[] { DECOMPILER, className, "-classpath", classPath };
        }


        private File getClassFile(final Class<?> clazz) {
            URL classResource = getClassResource(clazz);
            try {
                File classFile = new File(classResource.toURI());
                return classFile;
            } catch (URISyntaxException use) {
                return new File(classResource.getPath());
            }
        }

    }

    static class OpenJDK extends JavapMethodLister {

        @Override
        protected String[] getJavapProcessParametersForClass(final Class<?> clazz) {
            URL url = getClassResource(clazz);
            String pathToClassFile = url.toExternalForm();

            return new String[] { DECOMPILER, pathToClassFile };
        }

    }


    private static boolean isOracleJDK() {
        try {
            Process javap = Runtime.getRuntime().exec(new String[] { DECOMPILER, "-version" });
            BufferedReader reader = new BufferedReader(new InputStreamReader(javap.getInputStream()));
            String version = reader.readLine();
            reader.close();
            javap.destroy();

            // "javap -version" only exists for OpenJDK, so we can use this to check for OracleJDK
            return version != null && version.contains("Usage:");

        } catch (Exception e) {
            return false;
        }
    }


    public static JavapMethodLister getInstance() {
        if (instance != null) {
            return instance;

        } else if (isOracleJDK()) {
            System.out.println("[DEBUG] Using OracleJDK javap");
            instance = new OracleJDK();

        } else {
            System.out.println("[DEBUG] Using OpenJDK javap");
            instance = new OpenJDK();
        }

        return instance;
    }

}

package no.uio.tools.testdoc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodIndexer {

    private static final String DECOMPILER = "javap";

    private Matcher methodNameMatcher = Pattern.compile("\\w+(?=\\s*\\()").matcher("");

    private BufferedReader reader;


    private MethodIndexer(final InputStream stream) {
        this.reader = new BufferedReader(new InputStreamReader(stream));
    }


    private List<String> getMethods() throws IOException {
        List<String> methods = new ArrayList<String>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (methodNameMatcher.reset(line).find()) {
                methods.add(methodNameMatcher.group(0));
            }
        }

        return methods;
    }


    public static List<String> getMethods(final Class<?> clazz) {
        try {
            URL url = getClassResource(clazz);

            Process javap = Runtime.getRuntime().exec(new String[] { DECOMPILER, url.toExternalForm() });
            MethodIndexer indexer = new MethodIndexer(javap.getInputStream());
            return indexer.getMethods();

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


    private static URL getClassResource(final Class<?> clazz) {
        String classPackage = clazz.getPackage().getName();
        String className = clazz.getName().substring(classPackage.length() + 1);

        return clazz.getResource(className + ".class");
    }


    public static Map<String, Integer> getMethodOrders(final Class<?> clazz) {
        Map<String, Integer> methodOrders = new HashMap<String, Integer>();
        List<String> methodNames = getMethods(clazz);
        for (int i = 0; i < methodNames.size(); i++) {
            methodOrders.put(methodNames.get(i), i);
        }

        return methodOrders;
    }
}

package no.uio.tools.testdoc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodIndexer {

    private Matcher methodNameMatcher = Pattern.compile("\\w+(?=\\s*\\()").matcher("");

    private BufferedReader reader;


    public MethodIndexer(final InputStream stream) {
        this.reader = new BufferedReader(new InputStreamReader(stream));

    }


    public List<String> readMethods() throws IOException {
        List<String> methods = new ArrayList<String>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (methodNameMatcher.reset(line).find()) {
                methods.add(methodNameMatcher.group(0));
            }
        }
        reader.close();

        return methods;
    }


    public static Map<String, Integer> getMethodOrders(final Class<?> clazz, final MethodLister methodLister) {
        Map<String, Integer> methodOrders = new HashMap<String, Integer>();
        List<String> methodNames = methodLister.getMethodsForClass(clazz);
        for (int i = 0; i < methodNames.size(); i++) {
            methodOrders.put(methodNames.get(i), i);
        }

        return methodOrders;
    }

}

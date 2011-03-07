package no.uio.tools.testdoc.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import no.uio.tools.testdoc.TestDocPlanData;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Example usage:
 * 
 * java -cp ~/.m2/repository/freemarker/freemarker/2.3.8/freemarker-2.3.8.jar:target/testdoc.jar
 * no.uio.tools.testdoc.main.GenerateTestDoc target/testdoc.jar no.uio.tools
 * 
 * @author thomasfl
 * 
 */
public class GenerateTestDoc {

    public static boolean debug = false;


    public static void main(String[] args) throws ClassNotFoundException, IOException, TemplateException {
        String jarName = "";
        if (args.length == 0) {
            System.out.println("usage: testdoc source.jar [package]");
            System.exit(-1);
        } else {
            jarName = args[0];
        }

        System.out.println("Searching for TestDoc annotations in file: " + jarName);

        String packageName = ""; // no.uio.tools.testdoc";
        if (args.length > 1) {
            packageName = args[1];
            System.out.println("Filter package: " + packageName);
        }

        String output = generateTestDoc(jarName, packageName);
        // System.out.println(output);
        System.out.println("\nWriting documentation to: 'testplan.html'");
        writeFile("testplan.html", output);
    }


    public static String generateTestDoc(String jarName, String packageName) throws ClassNotFoundException,
            IOException, TemplateException {
        List classes = JarScanner.getClasseNamesInPackage(jarName, packageName);
        HashMap<String, LinkedList<TestDocPlanData>> datamodel = scanClasses(classes);
        String output = processFreemarkerTemplate(datamodel, "testdoc.ftl");
        return output;
    }


    @SuppressWarnings("rawtypes")
    public static HashMap<String, LinkedList<TestDocPlanData>> scanClasses(List classes) throws ClassNotFoundException,
            IOException {

        HashMap<String, LinkedList<TestDocPlanData>> datamodel = new HashMap<String, LinkedList<TestDocPlanData>>();
        LinkedList<TestDocPlanData> testplans = new LinkedList<TestDocPlanData>();

        for (Iterator<String> iterator = classes.iterator(); iterator.hasNext();) {
            String className = iterator.next().replaceAll(".class$", ""); // .getName();
            if (debug == true) {
                System.out.println("Scanning: " + className);
            }
            TestDocPlanData testDocPlanData = AnnotationsScanner.getAnnotationsFromClass(className);
            if (testDocPlanData != null) {
                System.out.println("Reading: " + className);
                testplans.add(testDocPlanData);
            }
        }

        datamodel.put("testplans", testplans);
        return datamodel;
    }


    /**
     * Process freemarker template to generate HTML from hashmap
     * 
     * @param datamodel
     * @param template
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    private static String processFreemarkerTemplate(HashMap<String, LinkedList<TestDocPlanData>> datamodel,
            String template) throws IOException, TemplateException {
        StringWriter output = new StringWriter();

        Configuration cfg = new Configuration();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        cfg.setClassForTemplateLoading(classLoader.getClass(), "/");

        if (debug) {
            File dir = new File("/Users/thomasfl/workspace/testdoc/src/main/resources/");
            cfg.setDirectoryForTemplateLoading(dir);
        }
        cfg.setLocalizedLookup(false);
        Template tpl = cfg.getTemplate(template);

        tpl.process(datamodel, output);
        return output.toString();
    }


    public static void writeFile(String filename, String data) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

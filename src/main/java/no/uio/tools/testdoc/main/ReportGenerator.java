package no.uio.tools.testdoc.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import no.uio.tools.testdoc.data.TestDocPlanData;

import org.apache.commons.io.FileUtils;
import org.apache.maven.reporting.MavenReportException;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ReportGenerator {

    public static boolean debug = false;


    public static String generateTestDocForClasses(final List<Class<?>> classes,
            final boolean failIfMissingTestPlanTitle) throws ClassNotFoundException, IOException, TemplateException,
            MavenReportException {
        HashMap<String, List<TestDocPlanData>> datamodel = scanClasses(classes, failIfMissingTestPlanTitle);
        String output = processFreemarkerTemplate(datamodel, "testdoc.ftl");
        return output;
    }


    public static HashMap<String, List<TestDocPlanData>> scanClasses(final List<Class<?>> classes,
            final boolean failIfMissingTestPlanTitle) throws ClassNotFoundException, IOException, MavenReportException {

        HashMap<String, List<TestDocPlanData>> datamodel = new HashMap<String, List<TestDocPlanData>>();
        List<TestDocPlanData> testplans = new LinkedList<TestDocPlanData>();

        for (Iterator<Class<?>> iterator = classes.iterator(); iterator.hasNext();) {
            Class<?> clazz = iterator.next();
            String className = clazz.getName();
            if (debug == true) {
                System.out.println("TestDoc: Scanning: " + className);
            }
            TestDocPlanData testDocPlanData = AnnotationsScanner.getAnnotationsFromClass(clazz,
                    failIfMissingTestPlanTitle);
            if (testDocPlanData != null) {
                if (debug == true) {
                    System.out.println("Reading: " + className);
                }
                testplans.add(testDocPlanData);
            }
        }

        sortAndNormalizeTestPlans(testplans);

        datamodel.put("testplans", testplans);
        return datamodel;
    }


    private static void sortAndNormalizeTestPlans(final List<TestDocPlanData> testplans) {
        Collections.sort(testplans);
        int num = 0;
        for (TestDocPlanData plan : testplans) {
            plan.setSortOrder(++num);
        }
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
    private static String processFreemarkerTemplate(final HashMap<String, List<TestDocPlanData>> datamodel,
            final String template) throws IOException, TemplateException {
        StringWriter output = new StringWriter();

        Configuration cfg = new Configuration();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        cfg.setClassForTemplateLoading(classLoader.getClass(), "");

        ClassTemplateLoader ctl = new ClassTemplateLoader(classLoader.getClass(), "/");
        cfg.setTemplateLoader(ctl);

        cfg.setLocalizedLookup(false);

        String templateText = readTextResource("/" + template);
        Template tpl = new Template(template, new StringReader(templateText), cfg);
        tpl.process(datamodel, output);
        return output.toString();
    }


    public static void writeFile(final String filename, final String data) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* Reads text file from jar archive. Used to read template files. */
    public static String readTextResource(final String s) {
        InputStream is = null;
        BufferedReader br = null;
        String line;
        ArrayList<String> list = new ArrayList<String>();

        try {
            is = FileUtils.class.getResourceAsStream(s);
            br = new BufferedReader(new InputStreamReader(is));
            while (null != (line = br.readLine())) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String retVal = "";
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            retVal = retVal + it.next();

        }
        return retVal;
    }

}

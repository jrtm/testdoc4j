package no.uio.tools.testdoc.main;

import java.io.IOException;
import java.util.List;

import freemarker.template.TemplateException;

public class TestDocRunner {

    public static void main(final String[] args) throws ClassNotFoundException, IOException, TemplateException {
        System.out.println("TestDoc: Generating documenation.");
        List<Class<?>> classesFound = AnnotationsScanner.findAllAnnotatedClasses();
        String html = ReportGenerator.generateTestDocForClasses(classesFound);
        String filename = "testplan_test.html";
        ReportGenerator.writeFile(filename, html);
        System.out.println("Output to '" + filename + "'.");
    }

}

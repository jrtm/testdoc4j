package no.uio.tools.testdoc.main;

import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.reporting.MavenReportException;

import freemarker.template.TemplateException;

public class TestDocRunner {

    private Log log;
    private boolean failIfMissingTestPlanTitle;


    public TestDocRunner(final Log log, final boolean failIfMissingTestPlanTitle) {
        this.log = log;
        this.failIfMissingTestPlanTitle = failIfMissingTestPlanTitle;
    }


    public void execute() throws ClassNotFoundException, IOException, TemplateException, MavenReportException {
        TestDoc.outputBanner(log, "Testrunner");
        TestDocClassLoader.loadClassesFromTargetFolder();
        List<Class<?>> classesFound = AnnotationsScanner.findAllAnnotatedClasses();
        String html = ReportGenerator.generateTestDocForClasses(classesFound, failIfMissingTestPlanTitle);
        String filename = "testdoc_testplan.html";

        String header = ReportGenerator.readTextResource("/header.html");
        String footer = ReportGenerator.readTextResource("/footer.html");
        ReportGenerator.writeFile(filename, header + html + footer);
        puts("TestDoc: Output report to '" + filename + "'.");
    }


    public static void main(final String[] args) throws ClassNotFoundException, IOException, TemplateException {
        TestDocRunner testDocRunner = new TestDocRunner(null, false);
        try {
            testDocRunner.execute();
        } catch (MavenReportException e) {
            System.out.println(e.getMessage());
        }
    }


    private void puts(final String string) {
        if (log == null) {
            System.out.println(string);
        } else {
            log.info(string);
        }
    }

}

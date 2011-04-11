package no.uio.tools.testdoc.main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import freemarker.template.TemplateException;

/**
 * Maven reporting mojo plugin for generating TestDoc reports.
 * 
 * 
 * Documentation: http://teleal.org/weblog/software/Howto%20write%20a%20Maven%20report%20plugin.html
 * 
 * @author twitter: @thomasfl
 * @goal testdoc
 * @phase site
 */
public class TestDocMojo extends AbstractMavenReport {

    /**
     * The Maven Project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * Doxia Site Renderer.
     * 
     * @component
     */
    protected Renderer siteRenderer;

    /**
     * The output directory.
     * 
     * @parameter expression="${project.build.directory}/generated-sources/testdoc"
     * @required
     */
    private File outputDirectory;


    protected String getOutputDirectory() {
        return outputDirectory.getAbsolutePath();
    }


    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getProject()
     */
    protected MavenProject getProject() {
        return project;
    }


    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getSiteRenderer()
     */
    protected Renderer getSiteRenderer() {
        return siteRenderer;
    }


    public String getDescription(Locale locale) {
        return "Test plan for human testers";
    }


    public String getName(Locale locale) {
        return "TestDoc";
    }


    public String getOutputName() {
        return "testdoc";
    }


    public void executeReport(Locale locale) throws MavenReportException {
        Sink sink = getSink();
        sink.head();
        sink.title();
        sink.text("TestDoc Testplan"); // <head><title> tag
        sink.title_();
        sink.head_();

        sink.body();
        try {
            ReflectionsScanner.addPath(".");
            ReflectionsScanner.addPath("./target/test-classes/.");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        List<Class> classesFound = ReflectionsScanner
                .findAllTestDocClassesInClasspath("no.uio.webapps.meldeapp.blackbox"); // no.uio.tools.testdoc.examples

        System.out.println("TestDoc: Classes with annotations found:" + classesFound.size());
        System.out.println("ClassPath: " + ReflectionsScanner.classPath());

        String html = "";
        try {
            html = GenerateTestDoc.generateTestDocForClasses(classesFound);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        // "<h1>TestDoc TestPlan</h1><br/><table><tr><td>Testplan</td></tr></table>"
        sink.rawText("<h1>TestDoc TestPlan</h1><br/>" + html);
        sink.body_();
        sink.flush();
        sink.close();
    }

}

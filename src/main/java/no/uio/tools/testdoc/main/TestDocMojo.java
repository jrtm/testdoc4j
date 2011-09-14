package no.uio.tools.testdoc.main;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.reflections.Reflections;

import freemarker.template.TemplateException;

/**
 * Maven reporting mojo plugin for generating TestDoc reports.
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


    public String getName(final Locale locale) {
        return "TestDoc";
    }


    public String getDescription(final Locale locale) {
        return "Test plans for human testers.";
    }


    public String getOutputName() {
        return "testdoc";
    }


    public static void generateTestDocReport(final Sink sink, String htmlReport) {
        sink.head();
        sink.title();
        sink.text("TestDoc Testplan");
        sink.title_();
        sink.head_();
        sink.body();

        sink.section1();
        sink.sectionTitle1();
        sink.text("TestDoc testplan");
        sink.sectionTitle1_();
        sink.section1_();

        sink.section1();
        sink.rawText(htmlReport);
        sink.section1_();

        sink.body_();
        sink.flush();
        sink.close();
    }


    @Override
    public void executeReport(final Locale locale) throws MavenReportException {
        outputTestDocBannerToLog();

        ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader urlClassLoader = null;
        try {
            String filename = System.getProperty("user.dir") + "/target/test-classes/";
            URL url = new File(filename).toURI().toURL();
            urlClassLoader = new URLClassLoader(new URL[] { url }, currentThreadClassLoader);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        Thread.currentThread().setContextClassLoader(urlClassLoader);
        Reflections reflections = new Reflections("");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(no.uio.tools.testdoc.annotations.TestDocPlan.class);
        List classesFound = new ArrayList(annotated);
        getLog().info("TestDoc found " + classesFound.size() + " classes with TestDoc annotations.");

        try {
            String html = ReportGenerator.generateTestDocForClasses(classesFound);
            generateTestDocReport(getSink(), html);

        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (TemplateException e1) {
            e1.printStackTrace();
        }

    }


    private void outputTestDocBannerToLog() {
        getLog().info(" ________________ ______________________  _______ _______  ");
        getLog().info(" \\__   __/  ____ \\  ____ \\__   __/  __  \\(  ___  )  ____ \\ ");
        getLog().info("   ) (  | (    \\/ (    \\/  ) (  | (  \\  ) (   ) | (    \\/  ");
        getLog().info("   | |  | (__   | (_____   | |  | |   ) | |   | | |        ");
        getLog().info("   | |  |  __)  (_____  )  | |  | |   | | |   | | |        ");
        getLog().info("   | |  | (           ) |  | |  | |   ) | |   | | |        ");
        getLog().info("   | |  | (____/Y\\____) |  | |  | (__/  ) (___) | (____/\\  ");
        getLog().info("   )_(  (_______|_______)  )_(  (______/(_______)_______/  ");
        getLog().info("  TestDoc - Show the world what your tests does.           ");
    }


    @Override
    protected String getOutputDirectory() {
        return outputDirectory.getAbsolutePath();
    }


    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getProject()
     */
    @Override
    protected MavenProject getProject() {
        return project;
    }


    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#getSiteRenderer()
     */
    @Override
    protected Renderer getSiteRenderer() {
        return siteRenderer;
    }

}

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


    public String getDescription(final Locale locale) {
        return "Test plans for human testers.";
    }


    public String getName(final Locale locale) {
        return "TestDoc";
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


    /* Returns an array with classes and jar files we want to add to classpath when scanning for annotations */
    private URL[] findClassURIs(String jarDirectory) throws MalformedURLException {
        File dir = new File(jarDirectory);
        List<URL> urls = new ArrayList<URL>();
        String curDir = System.getProperty("user.dir");
        String filename = curDir + "/target/test-classes/";
        URL url = new File(filename).toURI().toURL();
        urls.add(url);

        String[] children = dir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                filename = children[i];
                url = new File(jarDirectory + filename).toURI().toURL();
                urls.add(url);
            }
        }

        return urls.toArray(new URL[0]);
    }


    @Override
    public void executeReport(final Locale locale) throws MavenReportException {
        outputTestDocBannerToLog();

        String curDir = System.getProperty("user.dir");
        // String jarDirectory = curDir + "/target/meldeapp/WEB-INF/lib/";

        ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader urlClassLoader = null;
        try {
            String filename = curDir + "/target/test-classes/";
            URL url = new File(filename).toURI().toURL();
            // urlClassLoader = new URLClassLoader(findClassURIs(jarDirectory), currentThreadClassLoader);
            urlClassLoader = new URLClassLoader(new URL[] { url }, currentThreadClassLoader);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        Thread.currentThread().setContextClassLoader(urlClassLoader);

        /* ------------- */
        // String className = "no.uio.webapps.meldeapp.blackbox.ITFrontPageTest";
        // try {
        // logger.debug("Reading class: " + urlClassLoader.loadClass(className));
        // } catch (ClassNotFoundException e1) {
        // e1.printStackTrace();
        // }
        // /* ------------- */

        /* Read annotations using the org.reflections api */
        // Reflections reflections = new Reflections("no.uio.tools.testdoc");

        Reflections reflections = new Reflections(""); // no.uio");

        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(no.uio.tools.testdoc.annotations.TestDocPlan.class);

        List classesFound = new ArrayList(annotated);
        // getLog().info("Classes found: " + classesFound.size());
        // classesFound.remove(BasicExample.class);
        // classesFound.remove(AdvancedExample.class);
        // getLog().info("Classes found: " + classesFound.size());

        try {

            String html = ReportGenerator.generateTestDocForClasses(classesFound);
            // String outputFilename = "testplan3.html";
            // GenerateTestDoc.writeFile(outputFilename, html);
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
        getLog().info("  TestDoc - Show the world what your tests do.             ");
    }
}

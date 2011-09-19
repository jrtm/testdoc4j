package no.uio.tools.testdoc.main;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

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


    public static String currentFolderName() {
        Matcher matcher = Pattern.compile("/([^/]*)$").matcher(System.getProperty("user.dir"));
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }


    @Override
    public void executeReport(final Locale locale) throws MavenReportException {
        outputTestDocBannerToLog();

        ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader urlClassLoader = null;
        String curDir = System.getProperty("user.dir");
        /* NB: This only works if all jar files needed to run tests are store in */
        /* ./target/projectname/WEB-INF/lib/ */
        String jarDirectory = curDir + "/target/" + currentFolderName() + "/WEB-INF/lib/";
        try {
            // String filename = System.getProperty("user.dir") + "/target/test-classes/";
            // URL url = new File(filename).toURI().toURL();
            // urlClassLoader = new URLClassLoader(new URL[] { url }, currentThreadClassLoader);
            urlClassLoader = new URLClassLoader(findClassURIs(jarDirectory), currentThreadClassLoader);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        /* ------------- */
        // String className = "no.uio.webapps.meldeapp.blackbox.ITFrontPageTest";
        // try {
        // getLog().debug("TestDoc: Reading class: " + urlClassLoader.loadClass(className));
        // } catch (ClassNotFoundException e1) {
        // e1.printStackTrace();
        // }
        /* ------------- */

        Thread.currentThread().setContextClassLoader(urlClassLoader);

        List<Class<?>> classesFound = AnnotationsScanner.findAllAnnotatedClasses();

        // Reflections reflections = new Reflections("");
        //
        // Set<Class<?>> annotated =
        // reflections.getTypesAnnotatedWith(no.uio.tools.testdoc.annotations.TestDocPlan.class);
        //
        // List<Class<?>> classesFound = new ArrayList(annotated);

        classesFound.remove(no.uio.tools.testdoc.examples.AdvancedExample.class);

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


    /* Returns an array with classes and jar files we want to add to classpath when scanning for annotations */
    private URL[] findClassURIs(String jarDirectory) throws MalformedURLException {
        File dir = new File(jarDirectory);
        List<URL> urls = new ArrayList<URL>();

        String curDir = System.getProperty("user.dir");
        String filename = curDir + "/target/test-classes/";
        URL url = new File(filename).toURI().toURL();
        System.out.println("TestDoc 1: adding to classpath: " + url.toString());
        urls.add(url);

        String[] children = dir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                System.out.println("TestDoc 2: adding to classpath: " + jarDirectory + filename);
                filename = children[i];
                url = new File(jarDirectory + filename).toURI().toURL();
                urls.add(url);
            }
        }

        return urls.toArray(new URL[0]);
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
        getLog().info("  TestDoc - Show the world what your tests do. Version 0.2.2");
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

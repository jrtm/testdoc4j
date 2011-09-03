package no.uio.tools.testdoc.main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
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

    private static final Logger logger = Logger.getLogger(TestDocMojo.class);

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
        return "Test plan for human testers";
    }


    public String getName(final Locale locale) {
        return "TestDoc";
    }


    public String getOutputName() {
        System.out.println("***********getOutputName()**********");
        return "testdoc";
    }


    public static void generate(final Sink sink) {
        System.out.println("***********generate(sink): start **********");
        sink.head();
        sink.title();
        sink.text("TestDoc Testplan text here"); // <head><title> tag
        sink.flush();
        sink.close();
    }


    @Override
    public void executeReport(final Locale locale) throws MavenReportException {
        System.out.println("***********executeReport: start!! **********");
        Sink sink = getSink();
        sink.head();
        sink.title();
        sink.text("TestDoc Testplan text here"); // <head><title> tag
        sink.flush();
        sink.close();
        String[] args = { "" };
        try {
            ReflectionsScanner.main(args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        System.out.println("***********executeReport: done!! **********");

    }


    public void zzzz_executeReport(final Locale locale) throws MavenReportException {
        Sink sink = getSink();
        sink.head();
        sink.title();
        sink.text("TestDoc Testplan"); // <head><title> tag
        sink.title_();
        sink.head_();

        sink.body();

        // try {
        // // Get a full list of all jars needed to run the code
        // Set artifacts = transitivelyResolvePomDependencies();
        // String classpath = "";
        // for (Iterator iterator = artifacts.iterator(); iterator.hasNext();) {
        // Artifact artifact = (Artifact) iterator.next();
        // String artifactPath = artifact.getFile().getAbsolutePath();
        // // System.out.println("DEBUG: Loading Artifact: " + artifactPath);
        // // ReflectionsScanner.addPath(artifact.getFile().getAbsolutePath());
        // classpath = classpath + ":" + artifactPath;
        // }
        // // System.out.println(classpath);
        //
        // } catch (Exception e1) {
        // e1.printStackTrace();
        // }

        String html = "";
        // html = generateTestDocFromAnnotations(html);
        html = "<h1>Her kommer det noe</h1>";
        // System.out.println("TestDoc: DEBUG: Html: " + html);
        sink.rawText("<h1>TestDoc TestPlan</h1><br/>" + html);
        sink.body_();
        sink.flush();
        sink.close();
    }


    private String generateTestDocFromAnnotations(String html) {
        try {

            // System.out.println("ClassPath: " + ReflectionsScanner.classPath());
            // ReflectionsScanner.addPath("/Users/thomasfl/workspace/w3-testdoc.");
            // ReflectionsScanner.addPath("./target/classes/.");

            ReflectionsScanner.addPath("/Users/thomasfl/workspace/meldeapp/target/test-classes/.");

            // /* Get a full list of all jars needed to run the code */
            // Set artifacts = transitivelyResolvePomDependencies();
            // for (Iterator iterator = artifacts.iterator(); iterator.hasNext();) {
            // Artifact artifact = (Artifact) iterator.next();
            // // System.out.println("DEBUG: Loading Artifact: " + artifact.getFile().getAbsolutePath());
            // ReflectionsScanner.addPath(artifact.getFile().getAbsolutePath());
            //
            // }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        List<Class> classesFound = ReflectionsScanner
                .findAllTestDocClassesInClasspath("no.uio.webapps.meldeapp.blackbox"); // no.uio.tools.testdoc.examples

        System.out.println("TestDoc: Classes with annotations found:" + classesFound.size());
        // System.out.println("ClassPath: " + ReflectionsScanner.classPath());

        try {
            html = GenerateTestDoc.generateTestDocForClasses(classesFound);
        } catch (ClassNotFoundException e) {
            System.out.println("TestDoc Exception: 1");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("TestDoc Exception: 2");
            e.printStackTrace();
        } catch (TemplateException e) {
            System.out.println("TestDoc Exception: 3");
            e.printStackTrace();
        }
        return html;
    }

    /* Copied from: http://blogs.webtide.com/janb/entry/extending_the_maven_plugin_classpath */

    // /**
    // * @component
    // */
    // private ArtifactResolver artifactResolver;
    //
    // /**
    // *
    // * @component
    // */
    // private ArtifactFactory artifactFactory;
    //
    // /**
    // *
    // * @component
    // */
    // private ArtifactMetadataSource metadataSource;
    //
    // /**
    // *
    // * @parameter expression="${localRepository}"
    // */
    // private ArtifactRepository localRepository;

    /**
     * 
     * @parameter expression="${project.remoteArtifactRepositories}"
     */
    private List remoteRepositories;

    // public MavenProject loadPomAsProject(MavenProjectBuilder projectBuilder, Artifact pomArtifact)
    // throws ProjectBuildingException {
    // return projectBuilder.buildFromRepository(pomArtifact, remoteRepositories, localRepository);
    // }
    //
    //
    // public Artifact getPomArtifact(String groupId, String artifactId, String versionId) {
    // return this.artifactFactory.createBuildArtifact(groupId, artifactId, versionId, "pom");
    // }

    /*
     * Copied from
     * http://mojo.codehaus.org/fitnesse-maven-plugin/xref/org/codehaus/mojo/fitnesse/FitnesseRunnerMojo.html
     */
    /**
     * Create the transitive classpath.
     * 
     * @return The dependent artifacts.
     * @throws MojoExecutionException
     *             If the classpath can't be found.
     */
    // public Set transitivelyResolvePomDependencies() throws MojoExecutionException {
    // // make Artifacts of all the dependencies
    // Set dependencyArtifacts;
    // try {
    // dependencyArtifacts = MavenMetadataSource.createArtifacts(artifactFactory, dependencies, null, null, null);
    // } catch (InvalidDependencyVersionException e) {
    // throw new MojoExecutionException("Invalid dependency", e);
    // }
    //
    // // not forgetting the Artifact of the project itself
    // dependencyArtifacts.add(project.getArtifact());
    //
    // List listeners = Collections.EMPTY_LIST;
    //
    // // resolve all dependencies transitively to obtain a comprehensive list
    // // of jars
    // ArtifactResolutionResult result;
    // try {
    // result = artifactResolver.resolveTransitively(dependencyArtifacts, project.getArtifact(),
    // Collections.EMPTY_MAP, localRepository, remoteRepositories, metadataSource, null, listeners);
    // } catch (ArtifactResolutionException e) {
    // throw new MojoExecutionException("Unable to resolve Artifact.", e);
    // } catch (ArtifactNotFoundException e) {
    // throw new MojoExecutionException("Unable to resolve Artifact.", e);
    // }
    //
    // return result.getArtifacts();
    // }

    /**
     * The set of dependencies required by the project
     * 
     * @parameter default-value="${project.dependencies}"
     * @required
     * @readonly
     */
    private java.util.List dependencies;

}

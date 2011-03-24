package no.uio.tools.testdoc.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.site.decoration.Body;
import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.doxia.site.decoration.Skin;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

/**
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


    public void execute() throws MojoExecutionException {
        System.out.println("DEBUG: TestDoc public void execute()");
        if (!canGenerateReport()) {
            return;
        }

        DecorationModel model = new DecorationModel();
        Body body = new Body();
        model.setBody(body);
    }


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
        return "testplan";
    }


    protected void zzzz_executeReport(Locale locale) throws MavenReportException {
        System.out.println("DEBUG: Running *** TestDoc Mojo ***");
        File f = outputDirectory;

        if (!f.exists()) {
            f.mkdirs();
        }

        File touch = new File(f, "testdoc-testplan.html");

        FileWriter w = null;
        try {
            w = new FileWriter(touch);

            w.write("touch.txt");
        } catch (IOException e) {
            throw new MavenReportException("Error creating file " + touch, e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

    }


    public void executeReport(Locale locale) throws MavenReportException {
        System.out.println("DEBUG: TestDoc: executeReport(Locale locale) ");
        try {
            // DecorationModel model = new DecorationModel();
            // model.setBody(new Body());
            // Map attributes = new HashMap();
            // attributes.put("outputEncoding", "utf-8");
            // Locale locale = Locale.getDefault();
            // SiteRenderingContext siteContext = siteRenderer.createContextForSkin(getSkinArtifactFile(), attributes,
            // model, getName(locale), locale);
            // siteContext.setOutputEncoding("utf-8");

            // RenderingContext context = new RenderingContext(outputDirectory, getOutputName() + ".html");

            // SiteRendererSink sink = new SiteRendererSink(context);
            // generate(sink, locale);

            outputDirectory.mkdirs();

            Writer writer = new FileWriter(new File(outputDirectory, getOutputName() + ".html"));
            writer.append("En liten test");

            Sink sink = getSink();
            sink.text("DEBUG: ***TestDoc output test***");

            // SiteRendererSink sink = new SiteRendererSink( context );
            // generate(sink, locale);

            // outputDirectory.mkdirs();

            // Writer writer = new FileWriter(new File(outputDirectory, getOutputName() + ".html"));

            // SiteRenderingContext siteContext = siteRenderer.renderDocument(writer, renderingContext, context)

            // siteRenderer.generateDocument(writer, sink, siteContext);

            // siteRenderer.generateDocument(writer, sink, siteContext);

            // siteRenderer.copyResources(siteContext, new File(project.getBasedir(), "src/site/resources"),
            // outputDirectory);
            // } catch (RendererException e) {
            // throw new MavenReportException("An error has occurred in " + getName(Locale.ENGLISH)
            // + " report generation.", e);
        } catch (IOException e) {
            throw new MavenReportException("An error has occurred in " + getName(Locale.ENGLISH)
                    + " report generation.", e);
        }
        // } catch (MavenReportException e) {
        // throw new MavenReportException("An error has occurred in " + getName(Locale.ENGLISH)
        // + " report generation.", e);
        // }
    }

    /**
     * @component
     */
    protected ArtifactFactory factory;

    /**
     * @component
     */
    protected ArtifactResolver resolver;

    /**
     * Local Repository.
     * 
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;


    private File getSkinArtifactFile() throws MavenReportException {
        Skin skin = Skin.getDefaultSkin();

        String version = skin.getVersion();
        Artifact artifact;
        try {
            if (version == null) {
                version = Artifact.RELEASE_VERSION;
            }
            VersionRange versionSpec = VersionRange.createFromVersionSpec(version);
            artifact = factory.createDependencyArtifact(skin.getGroupId(), skin.getArtifactId(), versionSpec, "jar",
                    null, null);

            resolver.resolve(artifact, project.getRemoteArtifactRepositories(), localRepository);
        } catch (InvalidVersionSpecificationException e) {
            throw new MavenReportException("The skin version '" + version + "' is not valid: " + e.getMessage());
        } catch (ArtifactResolutionException e) {
            throw new MavenReportException("Unable to find skin", e);
        } catch (ArtifactNotFoundException e) {
            throw new MavenReportException("The skin does not exist: " + e.getMessage());
        }

        return artifact.getFile();
    }

}

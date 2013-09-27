package no.uio.tools.testdoc.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.uio.tools.testdoc.examples.AdvancedExample;
import no.uio.tools.testdoc.main.ReportGenerator;

import org.junit.Test;

import freemarker.template.TemplateException;

public class ReportGeneratorTest {

    @Test
    public void testBasicReport() throws ClassNotFoundException, IOException, TemplateException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(AdvancedExample.class);

        String report = ReportGenerator.generateTestDocForClasses(classes);
    }
}

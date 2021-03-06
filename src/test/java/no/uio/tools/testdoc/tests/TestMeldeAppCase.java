package no.uio.tools.testdoc.tests;

import static org.junit.Assert.assertEquals;
import no.uio.tools.testdoc.data.TestDocPlanData;
import no.uio.tools.testdoc.examples.ITFrontPageTest;
import no.uio.tools.testdoc.main.AnnotationsScanner;

import org.apache.maven.reporting.MavenReportException;
import org.junit.Test;

public class TestMeldeAppCase {

    @Test
    public void findTestDocTask() throws ClassNotFoundException, MavenReportException {
        Class<ITFrontPageTest> clazz = no.uio.tools.testdoc.examples.ITFrontPageTest.class;
        TestDocPlanData data = AnnotationsScanner.getAnnotationsFromClass(clazz, false);
        assertEquals(6, data.getTests().size());
    }

}

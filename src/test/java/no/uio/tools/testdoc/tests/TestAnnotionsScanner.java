package no.uio.tools.testdoc.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;
import no.uio.tools.testdoc.data.TestDocPlanData;
import no.uio.tools.testdoc.data.TestDocTestData;
import no.uio.tools.testdoc.examples.CornerCaseExample;
import no.uio.tools.testdoc.main.AnnotationsScanner;

import org.apache.maven.reporting.MavenReportException;
import org.junit.Test;

public class TestAnnotionsScanner {

    // @Test
    public void findAnnotatedClasses() {
        List<Class<?>> annotatedClasses = AnnotationsScanner.findAllAnnotatedClasses();
        Assert.assertEquals("Should find all annotated classes", 4, annotatedClasses.size());
    }


    @Test
    public void findTestDocTask() throws ClassNotFoundException, MavenReportException {
        Class<?> clazz = CornerCaseExample.class;
        TestDocPlanData data = AnnotationsScanner.getAnnotationsFromClass(clazz, false);
        LinkedList<TestDocTestData> tests = data.getTests();
        assertEquals("Test with single task & check", tests.get(0).getTitle());
        assertTrue(tests.get(0).isImplemented());

        assertEquals("Test with many tasks & checks", tests.get(1).getTitle());
        assertTrue(tests.get(1).isImplemented());

        assertEquals("Unimplemented test", tests.get(2).getTitle());
        assertFalse(tests.get(2).isImplemented());
    }
}

package no.uio.tools.testdoc.tests;

import java.util.List;

import junit.framework.Assert;
import no.uio.tools.testdoc.data.TestDocPlanData;
import no.uio.tools.testdoc.main.AnnotationsScanner;

import org.junit.Test;

public class TestAnnotionsScanner {

    @Test
    public void findAnnotatedClasses() {
        List<Class<?>> annotatedClasses = AnnotationsScanner.findAllAnnotatedClasses();
        Assert.assertEquals("Should find all annotated classes", 4, annotatedClasses.size());
    }


    @Test
    public void findTestDocTask() throws ClassNotFoundException {
        Class clazz = no.uio.tools.testdoc.examples.CornerCaseExample.class;
        TestDocPlanData data = AnnotationsScanner.getAnnotationsFromClass(clazz);
        System.out.println("Title: " + data.getTests().get(0).getTitle());
        System.out.println("Title: " + (data.getTests().get(0).getTasks() == null));
    }

}

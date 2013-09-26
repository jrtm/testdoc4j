package no.uio.tools.testdoc.tests;

import no.uio.tools.testdoc.annotations.TestDocPlan;
import no.uio.tools.testdoc.annotations.TestDocTask;
import no.uio.tools.testdoc.annotations.TestDocTasks;
import no.uio.tools.testdoc.annotations.TestDocTest;

import org.junit.Ignore;
import org.junit.Test;

@TestDocPlan(title = "TestDocPlan title", description = "TestDocPlan description", sortOrder = 100)
public class TestWithAnnotations {

    @Test
    public void emptyTest() {

    }


    @Ignore
    @Test
    public void emptyIgnoredTest() {

    }


    @TestDocTest("TestDocTest value")
    @TestDocTasks({
            @TestDocTask("TestDocTask1 value"),
            @TestDocTask(value = "TestDocTask2 value", checks = "Check1"),
            @TestDocTask(value = { "TDT2.1", "TDT2.2", "TDT2.3" }, checks = "Check2"),
            @TestDocTask(value = { "TDT3.1", "TDT3.2", "TDT3.3" }, checks = { "Check3.1", "Check3.2" }),
            @TestDocTask({ "TDT4.1", "TDT4.2", "TDT4.3" })
    })
    public void testDocTest() {

    }


    @Test
    @TestDocTest("TestDocTest value")
    @TestDocTask("TestDocTask1 value")
    public void testedTestDocTest() {

    }


    @Ignore
    @Test
    @TestDocTest("TestDocTest value")
    @TestDocTasks({
            @TestDocTask("TestDocTask1 value"),
            @TestDocTask(value = "TestDocTask2 value", checks = "Check1"),
            @TestDocTask(value = { "TDT2.1", "TDT2.2", "TDT2.3" }, checks = "Check2"),
            @TestDocTask(value = { "TDT3.1", "TDT3.2", "TDT3.3" }, checks = { "Check3.1", "Check3.2" }),
            @TestDocTask({ "TDT4.1", "TDT4.2", "TDT4.3" })
    })
    public void ignoredTestDocTest() {

    }

}

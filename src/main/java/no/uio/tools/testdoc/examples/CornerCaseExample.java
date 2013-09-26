package no.uio.tools.testdoc.examples;

import no.uio.tools.testdoc.annotations.TestDocTask;
import no.uio.tools.testdoc.annotations.TestDocTasks;
import no.uio.tools.testdoc.annotations.TestDocTest;

import org.junit.Ignore;
import org.junit.Test;

public class CornerCaseExample {

    @Test
    @TestDocTest("Test with single task & check")
    @TestDocTask(value = "Go to login page", checks = "Check 1")
    public void testWithOneTask() {
    }


    @Test
    @TestDocTest("Test with many tasks & checks")
    @TestDocTask(value = "Single task", checks = "Single check")
    @TestDocTasks({ @TestDocTask(value = "First task", checks = "Single check"),
            @TestDocTask(value = "Second task", checks = { "First check", "Second check" }) })
    public void testWithManyTasks() {
    }


    @Test
    @Ignore
    @TestDocTest("Ignored test")
    @TestDocTask(value = "Ignore this!", checks = "Make sure the task is ignored")
    public void ignoredTest() {

    }


    @TestDocTest("Unimplemented test")
    @TestDocTask(value = "Single task", checks = "Single check")
    public void unimplementedTest() {
    }


    @TestDocTest("Test with no checks")
    @TestDocTask(value = "Nothing")
    public void testWithoutChecks() {

    }

}

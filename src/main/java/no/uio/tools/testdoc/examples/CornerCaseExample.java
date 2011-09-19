package no.uio.tools.testdoc.examples;

import no.uio.tools.testdoc.annotations.TestDocTask;
import no.uio.tools.testdoc.annotations.TestDocTasks;
import no.uio.tools.testdoc.annotations.TestDocTest;

public class CornerCaseExample {

    @TestDocTest("Test with single task & check")
    @TestDocTask(task = "Go to login page", checks = "Check 1")
    public void testWithOneTask() {
        assert (true);
    }


    @TestDocTest("Test with many tasks & checks")
    @TestDocTask(task = "Single task", checks = "Single check")
    @TestDocTasks({ @TestDocTask(task = "First task", checks = "Single check"),
            @TestDocTask(task = "Second task", checks = { "First check", "Second check" }) })
    public void testWithManyTasks() {
        assert (true);
    }

}

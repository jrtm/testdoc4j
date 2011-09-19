package no.uio.tools.testdoc.examples;

import no.uio.tools.testdoc.annotations.TestDocPlan;
import no.uio.tools.testdoc.annotations.TestDocTask;
import no.uio.tools.testdoc.annotations.TestDocTasks;
import no.uio.tools.testdoc.annotations.TestDocTest;

@TestDocPlan(title = "Basic TestDoc example with this title")
public class BasicExample {

    @TestDocTest("Test description")
    @TestDocTasks({ @TestDocTask(task = "Task description", checks = "Desired behaviour description") })
    public void testUserLogin() {
        // Testcode here
        assert (true);
    }

}

package no.uio.tools.testdoc.examples;

import no.uio.tools.testdoc.annotations.TestDocPlan;
import no.uio.tools.testdoc.annotations.TestDocTask;
import no.uio.tools.testdoc.annotations.TestDocTasks;
import no.uio.tools.testdoc.annotations.TestDocTest;

@TestDocPlan("Authentication tests")
public class BasicExample {

    @TestDocTest("Test user login")
    @TestDocTasks({ @TestDocTask(task = "Go to login page", checks = "Check 1") })
    public void testUserLogin() {
        // Testcode here
        assert (true);
    }

}

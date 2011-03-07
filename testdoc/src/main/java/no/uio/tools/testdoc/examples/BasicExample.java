package no.uio.tools.testdoc.examples;

import no.uio.tools.testdoc.TestDocPlan;
import no.uio.tools.testdoc.TestDocTask;
import no.uio.tools.testdoc.TestDocTasks;
import no.uio.tools.testdoc.TestDocTest;

@TestDocPlan("Authentication tests")
public class BasicExample {

    @TestDocTest("Test user login")
    @TestDocTasks({ @TestDocTask(task = "Go to login page", checks = "Check 1") })
    public void testUserLogin() {
        // Testcode here
        assert (true);
    }

}

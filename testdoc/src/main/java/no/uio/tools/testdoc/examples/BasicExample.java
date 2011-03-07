package no.uio.tools.testdoc.examples;

import no.uio.tools.testdoc.TestDocPlan;
import no.uio.tools.testdoc.TestDocTask;
import no.uio.tools.testdoc.TestDocTasks;
import no.uio.tools.testdoc.TestDocTest;

@TestDocPlan("Overview of test")
// @TestDocWebDavUrl("https://www-dav.uio.no/testplan.html")
public class BasicExample {

    public static void toBeImplemented() {
    }


    @TestDocTest("Test user login")
    @TestDocTasks({ @TestDocTask(task = "Go to login page", checks = "Check 1"),
            @TestDocTask(task = "Task 2", checks = { "Check1", "Check2" }) })
    public void testUserLogin() {
        int i = 10;
        i = i + 2;
    }


    @TestDocTest("Test user logout")
    @TestDocTasks({ @TestDocTask(task = "Click logout link", checks = "Look for goodbye message") })
    public void testUserLogout() {
        int i = 10;
        i = i + 2;
    }

}

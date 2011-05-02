package no.uio.tools.testdoc.examples;

import static org.junit.Assert.assertTrue;

import no.uio.tools.testdoc.annotations.TestDocPlan;
import no.uio.tools.testdoc.annotations.TestDocTask;
import no.uio.tools.testdoc.annotations.TestDocTasks;
import no.uio.tools.testdoc.annotations.TestDocTest;

import org.junit.Test;

@TestDocPlan("Advanced tests")
public class AdvancedExample {

    public static void toBeImplemented() {
    }


    @Test
    @TestDocTest("Test user login")
    @TestDocTasks({ @TestDocTask(task = "Go to login page", checks = "Everything looks ok"),
            @TestDocTask(task = "Type wrong password", checks = { "See error message", "Check2" }) })
    public void userLogin() {
        assertTrue(true);
    }


    @Test
    @TestDocTest("Test user logout")
    @TestDocTasks({ @TestDocTask(task = "Click logout link", checks = "Look for goodbye message") })
    public void userLogout() {
        int i = 10;
        i = i + 2;
    }


    @Test
    @TestDocTest("Test search- and detailspage")
    @TestDocTasks({
            @TestDocTask(task = "Go to search page", checks = "Searchform is present"),
            @TestDocTask(task = "Search for 'uio'", checks = { "See that you get 4 searchresults",
                    "See that 'usit' is one of the searchresults" }) })
    public void search() {
        assertTrue(true);
    }

}

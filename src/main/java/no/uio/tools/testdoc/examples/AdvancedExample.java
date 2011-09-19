package no.uio.tools.testdoc.examples;

import static org.junit.Assert.assertTrue;

import no.uio.tools.testdoc.annotations.TestDocPlan;
import no.uio.tools.testdoc.annotations.TestDocTask;
import no.uio.tools.testdoc.annotations.TestDocTasks;
import no.uio.tools.testdoc.annotations.TestDocTest;

import org.junit.Test;

@TestDocPlan(title = "Advanced TestDoc example", sortOrder = 2)
public class AdvancedExample {

    public static void toBeImplemented() {
    }


    @Test
    @TestDocTest("First test description")
    @TestDocTask(task = "Single task description", checks = "Desired behaviour")
    public void userLogout() {
        int i = 10;
        i = i + 2;
    }


    @Test
    @TestDocTest("Second test description")
    @TestDocTasks({ @TestDocTask(task = "First task", checks = "Single check"),
            @TestDocTask(task = "Second task", checks = { "First check", "Second check" }) })
    public void userLogin() {
        assertTrue(true);
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

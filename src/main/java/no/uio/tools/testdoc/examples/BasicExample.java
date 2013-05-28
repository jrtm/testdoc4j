package no.uio.tools.testdoc.examples;

import no.uio.tools.testdoc.annotations.TestDocPlan;
import no.uio.tools.testdoc.annotations.TestDocTask;
import no.uio.tools.testdoc.annotations.TestDocTest;

import org.junit.Test;

@TestDocPlan(title = "Basic TestDoc example with this title", description = "Description detailing setup conditions for the tests")
public class BasicExample {

    @Test
    @TestDocTest(value = "Test description")
    @TestDocTask(task = "Task description", checks = "Desired behaviour description")
    public void testUserLogin() {
        // Testcode goes here
    }

}

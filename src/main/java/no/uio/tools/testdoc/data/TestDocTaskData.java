package no.uio.tools.testdoc.data;

public class TestDocTaskData {

    private final String[] steps;

    private final String[] checks;


    public TestDocTaskData(final String[] steps, final String[] checks) {
        this.steps = steps;
        this.checks = checks;
    }


    public String[] getSteps() {
        return steps;
    }


    public String[] getChecks() {
        return checks;
    }


    public int getChecksCount() {
        return checks.length;
    }

}

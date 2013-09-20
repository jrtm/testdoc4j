package no.uio.tools.testdoc.data;

import java.util.List;

public class TestDocTaskData {

    private final String title;

    private final List<String> checks;


    public TestDocTaskData(final String title, final List<String> checks) {
        this.title = title;
        this.checks = checks;
    }


    public String getTitle() {
        return title;
    }


    public List<String> getChecks() {
        return checks;
    }


    public int getChecksCount() {
        return checks.size();
    }

}

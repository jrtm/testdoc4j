package no.uio.tools.testdoc.data;

import java.util.List;

import no.uio.tools.testdoc.util.MethodWrapper;

public class TestDocTestData implements MethodWrapper {

    private String title;

    private int number;
    private boolean implemented;

    private String methodName;

    private List<TestDocTaskData> tasks;


    public boolean isImplemented() {
        return implemented;
    }


    public void setImplemented(final boolean implemented) {
        this.implemented = implemented;
    }


    public void setTitle(final String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }


    public void setTasks(final List<TestDocTaskData> tasks) {
        this.tasks = tasks;
    }


    public List<TestDocTaskData> getTasks() {
        return tasks;
    }


    public void setNumber(final int number) {
        this.number = number;
    }


    public int getNumber() {
        return number;
    }


    @Override
    public String getMethodName() {
        return methodName;
    }


    public void setMethodName(final String methodName) {
        this.methodName = methodName;
    }


    /**
     * Number number of checks in all tasks.
     * 
     * @return
     */
    public int getChecksCount() {
        int count = 0;
        for (TestDocTaskData task : getTasks()) {
            count += task.getChecksCount();
        }
        return count;
    }

}

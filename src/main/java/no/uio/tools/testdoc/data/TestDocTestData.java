package no.uio.tools.testdoc.data;

import java.util.Iterator;
import java.util.LinkedList;

import no.uio.tools.testdoc.util.MethodWrapper;

public class TestDocTestData implements MethodWrapper {

    private String title;
    private int number;
    private boolean implemented;

    private String methodName;


    public boolean isImplemented() {
        return implemented;
    }


    public void setImplemented(final boolean implemented) {
        this.implemented = implemented;
    }

    private LinkedList<TestDocTaskData> tasks;


    public void setTitle(final String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }


    public void setTasks(final LinkedList<TestDocTaskData> tasks) {
        this.tasks = tasks;
    }


    public LinkedList<TestDocTaskData> getTasks() {
        return tasks;
    }


    public void setNumber(final int number) {
        this.number = number;
    }


    public int getNumber() {
        return number;
    }


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
        LinkedList<TestDocTaskData> tasks = getTasks();
        for (Iterator<TestDocTaskData> iterator = tasks.iterator(); iterator.hasNext();) {
            TestDocTaskData task = iterator.next();
            count = count + task.getChecks().size();
        }
        return count;
    }

}

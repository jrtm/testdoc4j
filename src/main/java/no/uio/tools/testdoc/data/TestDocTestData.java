package no.uio.tools.testdoc.data;

import java.util.Iterator;
import java.util.LinkedList;

import no.uio.tools.testdoc.util.MethodWrapper;

public class TestDocTestData implements MethodWrapper {

    private String methodName;
    private String title;
    private int number;

    private LinkedList<TestDocTaskData> tasks;


    public void setMethodName(final String methodName) {
        this.methodName = methodName;
    }


    public String getMethodName() {
        return methodName;
    }


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

package no.uio.tools.testdoc;

import java.util.LinkedList;

public class TestDocPlanData {

    private String title;
    private String className;
    private LinkedList<TestDocTestData> tests;


    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }


    public void setTests(LinkedList<TestDocTestData> tests) {
        this.tests = tests;
    }


    public LinkedList<TestDocTestData> getTests() {
        return tests;
    }


    public void setClassName(String className) {
        this.className = className;
    }


    public String getClassName() {
        return className;
    }

}

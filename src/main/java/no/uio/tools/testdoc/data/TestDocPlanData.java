package no.uio.tools.testdoc.data;

import java.util.LinkedList;

public class TestDocPlanData implements Comparable<TestDocPlanData> {

    private String title;
    private int sortOrder;
    private String className;
    private LinkedList<TestDocTestData> tests;


    public void setTitle(final String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }


    public void setTests(final LinkedList<TestDocTestData> tests) {
        this.tests = tests;
    }


    public LinkedList<TestDocTestData> getTests() {
        return tests;
    }


    public void setClassName(final String className) {
        this.className = className;
    }


    public String getClassName() {
        return className;
    }


    public void setSortOrder(final int sortOrder) {
        this.sortOrder = sortOrder;
    }


    public int getSortOrder() {
        return sortOrder;
    }


    public int compareTo(final TestDocPlanData other) {
        return this.sortOrder - other.getSortOrder();
    }

}

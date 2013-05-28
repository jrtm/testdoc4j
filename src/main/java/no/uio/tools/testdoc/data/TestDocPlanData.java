package no.uio.tools.testdoc.data;

import java.util.LinkedList;

public class TestDocPlanData implements Comparable<TestDocPlanData> {

    private String title;
    private String description;

    private int sortOrder;
    private Class<?> clazz;
    private LinkedList<TestDocTestData> tests;


    public void setTitle(final String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }


    public void setDescription(final String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }


    public void setTests(final LinkedList<TestDocTestData> tests) {
        this.tests = tests;
    }


    public LinkedList<TestDocTestData> getTests() {
        return tests;
    }


    public void setClazz(final Class<?> className) {
        this.clazz = className;
    }


    public Class<?> getClazz() {
        return clazz;
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

package no.uio.tools.testdoc.data;

import java.util.List;

public class TestDocPlanData implements Comparable<TestDocPlanData> {

    private String title;
    private String description;

    private int sortOrder;
    private Class<?> clazz;
    private List<TestDocTestData> tests;


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


    public void setTests(final List<TestDocTestData> tests) {
        this.tests = tests;
    }


    public List<TestDocTestData> getTests() {
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


    @Override
    public int compareTo(final TestDocPlanData other) {
        return this.sortOrder - other.getSortOrder();
    }

}

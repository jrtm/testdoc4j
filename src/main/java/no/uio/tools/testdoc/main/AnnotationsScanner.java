package no.uio.tools.testdoc.main;

import java.lang.reflect.Method;
import java.util.LinkedList;

import no.uio.tools.testdoc.annotations.TestDocPlan;
import no.uio.tools.testdoc.annotations.TestDocTask;
import no.uio.tools.testdoc.annotations.TestDocTasks;
import no.uio.tools.testdoc.annotations.TestDocTest;
import no.uio.tools.testdoc.data.TestDocPlanData;
import no.uio.tools.testdoc.data.TestDocTaskData;
import no.uio.tools.testdoc.data.TestDocTestData;

public class AnnotationsScanner {

    public static TestDocPlanData getAnnotationsFromClass(Class clazz) {
        TestDocPlanData testDocPlanData = new TestDocPlanData();
        LinkedList<TestDocTestData> testsList = new LinkedList<TestDocTestData>();
        TestDocPlan testdocPlan = (TestDocPlan) clazz.getAnnotation(TestDocPlan.class);
        if (testdocPlan != null && testdocPlan.value() != null) {
            testDocPlanData.setTitle(testdocPlan.value());
            testDocPlanData.setClassName(clazz.getName());
        } else {
            testDocPlanData.setTitle(null);
        }

        int testsCount = 0;

        Method[] methods = null;
        try {
            methods = clazz.getMethods();
        } catch (NoClassDefFoundError e) {
            // TODO: handle exception
        }
        if (methods == null) {
            return null;
        }

        for (Method m : methods) {
            TestDocTestData testDocTestData = new TestDocTestData();

            TestDocTest testDocTest = (TestDocTest) m.getAnnotation(TestDocTest.class);
            if (testDocTest != null) {
                testDocTestData.setTitle(testDocTest.value());
            }

            TestDocTasks testDocTasks = (TestDocTasks) m.getAnnotation(TestDocTasks.class);
            if (testDocTasks != null) {
                LinkedList<TestDocTaskData> tasksLists = new LinkedList<TestDocTaskData>();

                TestDocTask[] tasks = (TestDocTask[]) testDocTasks.value();

                for (int i = 0; i < tasks.length; i++) {
                    TestDocTaskData taskData = new TestDocTaskData();
                    taskData.setTitle(tasks[i].task());
                    String[] checks = tasks[i].checks();
                    LinkedList<String> checksData = new LinkedList<String>();
                    for (int j = 0; j < checks.length; j++) {
                        checksData.add(checks[j]);
                    }
                    if (checks.length > 0) {
                        taskData.setChecks(checksData);
                    }
                    tasksLists.add(taskData);

                }
                testDocTestData.setTasks(tasksLists);
            }
            if (testDocTestData.getTitle() != null || testDocTestData.getTasks() != null) {
                testsCount = testsCount + 1;
                testDocTestData.setNumber(testsCount);
                testDocPlanData.setClassName(clazz.getName());
                testsList.add(testDocTestData);
            }
        }
        if (testsList.size() > 0) {
            testDocPlanData.setTests(testsList);
            testDocPlanData.setClassName(clazz.getName());
        }
        if (testDocPlanData.getTitle() == null && testDocPlanData.getTests() == null) {
            return null;
        }
        return testDocPlanData;
    }

}

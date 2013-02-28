package no.uio.tools.testdoc.main;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import no.uio.tools.testdoc.annotations.TestDocPlan;
import no.uio.tools.testdoc.annotations.TestDocTask;
import no.uio.tools.testdoc.annotations.TestDocTasks;
import no.uio.tools.testdoc.annotations.TestDocTest;
import no.uio.tools.testdoc.data.TestDocPlanData;
import no.uio.tools.testdoc.data.TestDocTaskData;
import no.uio.tools.testdoc.data.TestDocTestData;
import no.uio.tools.testdoc.util.MethodOrderComparator;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class AnnotationsScanner {

    public static boolean debug = false;


    /* Returns a list of all classes annotated with the @TestDocTest or @TestDocPlan annotations. */
    public static List<Class<?>> findAllAnnotatedClasses() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().addUrls(ClasspathHelper.forPackage(""))
                .setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new MethodAnnotationsScanner()));
        List<Class<?>> annotatedClasses = findClassesAnnotatedWithTestDocTest(reflections);
        findClassesAnnotatedWithTestDocPlan(reflections, annotatedClasses);
        return annotatedClasses;
    }


    private static void findClassesAnnotatedWithTestDocPlan(final Reflections reflections,
            final List<Class<?>> annotatedClasses) {
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(no.uio.tools.testdoc.annotations.TestDocPlan.class);
        for (Class<?> clazz : annotated) {
            if (!annotatedClasses.contains(clazz)) {
                annotatedClasses.add(clazz);
            }
        }
    }


    private static List<Class<?>> findClassesAnnotatedWithTestDocTest(final Reflections reflections) {
        Set<Method> annotatedMethods = reflections
                .getMethodsAnnotatedWith(no.uio.tools.testdoc.annotations.TestDocTest.class);

        List<Class<?>> annotatedClasses = new ArrayList<Class<?>>();
        for (Method method : annotatedMethods) {
            Class<?> clazz = method.getDeclaringClass();
            if (!annotatedClasses.contains(clazz)) {
                annotatedClasses.add(clazz);
            }
        }
        return annotatedClasses;
    }


    public static TestDocPlanData getAnnotationsFromClass(final Class<?> clazz) throws ClassNotFoundException {

        TestDocPlanData testDocPlanData = new TestDocPlanData();
        LinkedList<TestDocTestData> testsList = new LinkedList<TestDocTestData>();
        TestDocPlan testdocPlan = clazz.getAnnotation(TestDocPlan.class);

        if (testdocPlan != null) {
            if (testdocPlan.title() != null) {
                testDocPlanData.setTitle(testdocPlan.title());
            }
            testDocPlanData.setSortOrder(testdocPlan.sortOrder());
            testDocPlanData.setClassName(clazz.getName());
        } else {
            testDocPlanData.setTitle(null);
        }

        Method[] methods = null;
        methods = clazz.getMethods();
        if (methods == null) {
            return null;
        }

        for (Method m : methods) {
            TestDocTestData testDocTestData = new TestDocTestData();
            testDocTestData.setMethodName(m.getName());

            TestDocTest testDocTest = m.getAnnotation(TestDocTest.class);
            if (testDocTest != null) {
                testDocTestData.setTitle(testDocTest.value());
            }

            TestDocTasks testDocTasks = m.getAnnotation(TestDocTasks.class);

            LinkedList<TestDocTaskData> tasksLists = new LinkedList<TestDocTaskData>();
            if (testDocTasks != null) {

                TestDocTask[] tasks = testDocTasks.value();

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

            }

            /* If unit test has a single TestDocTask annoation, then add it to the list. */
            TestDocTask testDocTask = m.getAnnotation(TestDocTask.class);
            if (testDocTask != null) {
                TestDocTaskData taskData = new TestDocTaskData();
                taskData.setTitle(testDocTask.task());

                String[] checks = testDocTask.checks();
                LinkedList<String> checksData = new LinkedList<String>();
                for (int j = 0; j < checks.length; j++) {
                    checksData.add(checks[j]);
                }
                if (checks.length > 0) {
                    taskData.setChecks(checksData);
                }
                tasksLists.add(taskData);

            }

            if (tasksLists.size() > 0) {
                testDocTestData.setTasks(tasksLists);
            }

            if (testDocTestData.getTitle() != null || testDocTestData.getTasks() != null) {
                testDocPlanData.setClassName(clazz.getName());
                testsList.add(testDocTestData);
            }

        }
        if (testsList.size() > 0) {
            sortTestsListForClass(clazz, testsList);
            testDocPlanData.setTests(testsList);
            testDocPlanData.setClassName(clazz.getName());
        }
        if (testDocPlanData.getTitle() == null && testDocPlanData.getTests() == null) {
            return null;
        }
        return testDocPlanData;
    }


    private static void sortTestsListForClass(final Class<?> clazz, final LinkedList<TestDocTestData> testsList) {
        MethodOrderComparator<TestDocTestData> comparator = new MethodOrderComparator<TestDocTestData>(clazz);
        if (comparator.canSort()) {
            Collections.sort(testsList, comparator);
        }

        int testsCount = 0;
        for (TestDocTestData test : testsList) {
            test.setNumber(++testsCount);
        }
    }
}

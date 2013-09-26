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

import org.apache.maven.reporting.MavenReportException;
import org.junit.Ignore;
import org.junit.Test;
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


    private static boolean hasTestDocAnnotations(final Method method) {
        TestDocTest testDocTest = method.getAnnotation(TestDocTest.class);
        TestDocTasks testDocTasks = method.getAnnotation(TestDocTasks.class);
        TestDocTask testDocTask = method.getAnnotation(TestDocTask.class);

        return (testDocTest != null && testDocTest.value() != null)
                || (testDocTasks != null && testDocTasks.value() != null)
                || (testDocTask != null && testDocTask.value() != null);
    }


    /* Read annotations from a class and return data objects. */
    public static TestDocPlanData getAnnotationsFromClass(final Class<?> clazz, final boolean failIfMissingTestPlanTitle)
            throws ClassNotFoundException, MavenReportException {

        TestDocPlanData testDocPlanData = new TestDocPlanData();
        List<TestDocTestData> testsList = new LinkedList<TestDocTestData>();
        TestDocPlan testdocPlan = clazz.getAnnotation(TestDocPlan.class);

        if (testdocPlan != null) {
            if (testdocPlan.title() != null) {
                testDocPlanData.setTitle(testdocPlan.title());
            }
            testDocPlanData.setDescription(testdocPlan.description());
            testDocPlanData.setSortOrder(testdocPlan.sortOrder());
            testDocPlanData.setClazz(clazz);
        } else {
            testDocPlanData.setTitle(null);
        }

        if (failIfMissingTestPlanTitle && (testDocPlanData.getTitle() == null || testDocPlanData.getTitle().equals(""))) {
            throw new MavenReportException("TestDoc Error: Missing tag @TestDocplan(title=...) in class "
                    + testDocPlanData.getClazz());
        }

        int testsCount = 0;

        Method[] methods = null;
        methods = clazz.getMethods();
        if (methods == null) {
            return null;
        }

        for (Method m : methods) {

            if (hasTestDocAnnotations(m)) {

                TestDocTest testDocTestAnnotations = m.getAnnotation(TestDocTest.class);
                TestDocTasks testDocTasksAnnotations = m.getAnnotation(TestDocTasks.class);
                TestDocTask testDocTaskAnnotations = m.getAnnotation(TestDocTask.class);

                TestDocTestData testDocTestData = new TestDocTestData();
                testDocTestData.setImplemented(isImplemented(m));

                testDocTestData.setMethodName(m.getName());

                /* Example: @TestDocTest("Test login page") */
                if (testDocTestAnnotations != null) {
                    testDocTestData.setTitle(testDocTestAnnotations.value());
                } else {
                    testDocTestData.setTitle("(no title!!!)"); // TODO Remove
                }

                /* @TestDocTasks(@TestDocTask(task = "Go to login page",check="Is there a login form?") */
                List<TestDocTaskData> tasksLists = new LinkedList<TestDocTaskData>();
                if (testDocTasksAnnotations != null) {
                    for (TestDocTask task : testDocTasksAnnotations.value()) {
                        TestDocTaskData taskData = getTaskData(task);
                        tasksLists.add(taskData);
                    }
                }

                /* If unit test has a single TestDocTask annoation, then add it to the list. */
                // testDocTask = (TestDocTask) m.getAnnotation(TestDocTask.class);
                if (testDocTaskAnnotations != null) {
                    TestDocTaskData taskData = getTaskData(testDocTaskAnnotations);
                    tasksLists.add(taskData);
                }

                if (tasksLists.size() > 0) {
                    testDocTestData.setTasks(tasksLists);
                }

                if (testDocTestData.getTitle() != null || testDocTestData.getTasks() != null) {
                    testsCount = testsCount + 1;
                    testDocTestData.setNumber(testsCount);
                    testDocPlanData.setClazz(clazz);
                    testsList.add(testDocTestData);
                }
            }
        }
        if (testsList.size() > 0) {
            System.out.println(clazz.getName() + ": " + testsList.size() + " tests");
            sortTestDocTestData(clazz, testsList);
            testDocPlanData.setTests(testsList);
            testDocPlanData.setClazz(clazz);
        }
        if (testDocPlanData.getTitle() == null && testDocPlanData.getTests() == null) {
            return null;
        }
        return testDocPlanData;
    }


    private static TestDocTaskData getTaskData(final TestDocTask task) {
        String[] steps = task.value();
        String[] checks = task.checks();

        TestDocTaskData taskData = new TestDocTaskData(steps, checks);
        return taskData;
    }


    private static boolean isImplemented(final Method m) {
        boolean hasTestAnnotation = m.getAnnotation(Test.class) != null;
        boolean hasIgnoreAnnotation = m.getAnnotation(Ignore.class) != null;

        return hasTestAnnotation && !hasIgnoreAnnotation;
    }


    private static void sortTestDocTestData(final Class<?> clazz, final List<TestDocTestData> testDataList) {
        MethodOrderComparator<TestDocTestData> comparator = new MethodOrderComparator<TestDocTestData>(clazz);
        Collections.sort(testDataList, comparator);

        int testNumber = 0;
        for (TestDocTestData testData : testDataList) {
            testData.setNumber(++testNumber);
        }
    }
}

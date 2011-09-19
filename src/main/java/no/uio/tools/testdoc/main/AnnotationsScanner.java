package no.uio.tools.testdoc.main;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
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


    private static void findClassesAnnotatedWithTestDocPlan(Reflections reflections, List<Class<?>> annotatedClasses) {
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(no.uio.tools.testdoc.annotations.TestDocPlan.class);
        for (Class<?> clazz : annotated) {
            if (!annotatedClasses.contains(clazz)) {
                annotatedClasses.add(clazz);
            }
        }
    }


    private static List<Class<?>> findClassesAnnotatedWithTestDocTest(Reflections reflections) {
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


    public static TestDocPlanData getAnnotationsFromClass(Class clazz) throws ClassNotFoundException {
        if (debug) {
            System.out.println("TestDoc: loading class explicitly...");
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            clazz = classLoader.loadClass("no.uio.tools.testdoc.examples.AdvancedExample");
            System.out.println("TestDoc: reading annotations... ");
        }

        TestDocPlanData testDocPlanData = new TestDocPlanData();
        LinkedList<TestDocTestData> testsList = new LinkedList<TestDocTestData>();
        TestDocPlan testdocPlan = (TestDocPlan) clazz.getAnnotation(TestDocPlan.class);

        if (debug) {
            try {

                System.out.println("testdoc: clazz.getAnnotations().toString(): ");
                Annotation[] annotations = clazz.getAnnotations();
                for (int i = 0; i < annotations.length; i++) {
                    System.out.println(i + " = " + annotations[i].toString());
                }
                if (testdocPlan == null) {
                    System.out.println("testdocPlan == null");
                }

                System.out.println("TestDoc reading title: '" + testdocPlan.title() + "'");
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }

        }

        if (testdocPlan != null) {
            if (testdocPlan.title() != null) {
                testDocPlanData.setTitle(testdocPlan.title());
            }
            testDocPlanData.setSortOrder(testdocPlan.sortOrder());
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

            LinkedList<TestDocTaskData> tasksLists = new LinkedList<TestDocTaskData>();
            if (testDocTasks != null) {

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

            }

            /* If unit test has a single TestDocTask annoation, then add it to the list. */
            TestDocTask testDocTask = (TestDocTask) m.getAnnotation(TestDocTask.class);
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

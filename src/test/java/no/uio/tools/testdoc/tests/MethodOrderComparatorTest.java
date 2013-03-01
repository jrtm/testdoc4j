package no.uio.tools.testdoc.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.uio.tools.testdoc.examples.AdvancedExample;
import no.uio.tools.testdoc.util.MethodOrderComparator;
import no.uio.tools.testdoc.util.MethodWrapper;

import org.junit.Test;

public class MethodOrderComparatorTest {

    @Test
    public void testMethodSorting() {
        String[] ordered = { "toBeImplemented", "userLogin", "userLogout", "search", "svadaTest", "loremIpsumTest" };
        List<MockMethod> methods = toMockMethods(ordered);
        MethodOrderComparator<MockMethod> comparator = new MethodOrderComparator<MockMethod>(AdvancedExample.class);

        Collections.shuffle(methods);
        Collections.sort(methods, comparator);

        for (int i = 0; i < ordered.length; i++) {
            assertEquals(ordered[i], methods.get(i).getMethodName());
        }

    }


    @Test
    public void testEmptyClassSorting() {
        String[] ordered = {};
        List<MockMethod> methods = toMockMethods(ordered);
        MethodOrderComparator<MockMethod> comparator = new MethodOrderComparator<MockMethod>(AdvancedExample.class);

        Collections.sort(methods, comparator);

    }


    private List<MockMethod> toMockMethods(final String... methodNames) {
        List<MockMethod> methods = new ArrayList<MockMethod>(methodNames.length);
        for (int i = 0; i < methodNames.length; i++) {
            methods.add(new MockMethod(methodNames[i]));
        }
        return methods;
    }

    class MockMethod implements MethodWrapper {
        String name;


        MockMethod(final String name) {
            this.name = name;
        }


        public String getMethodName() {
            return name;
        }
    }

}

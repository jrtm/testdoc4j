package no.uio.tools.testdoc.util;

import java.util.List;

public interface MethodLister {

    public List<String> getMethodsForClass(final Class<?> clazz);

}

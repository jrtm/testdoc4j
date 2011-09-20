package no.uio.tools.testdoc.main;

import java.io.IOException;
import java.util.List;

import freemarker.template.TemplateException;

public class TestDocRunner {

    public static void main(final String[] args) throws ClassNotFoundException, IOException, TemplateException {
        outputTestDocBanner();
        System.out.println("TestDoc: Generating documenation.");
        List<Class<?>> classesFound = AnnotationsScanner.findAllAnnotatedClasses();
        String html = ReportGenerator.generateTestDocForClasses(classesFound);
        String filename = "testplan_test.html";
        ReportGenerator.writeFile(filename, html);
        System.out.println("Output to '" + filename + "'.");
    }


    private static void outputTestDocBanner() {
        System.out.println("________________ ______________________  _______ _______  ");
        System.out.println("\\__   __/  ____ \\  ____ \\__   __/  __  \\(  ___  )  ____ \\ ");
        System.out.println("   ) (  | (    \\/ (    \\/  ) (  | (  \\  ) (   ) | (    \\/  ");
        System.out.println("   | |  | (__   | (_____   | |  | |   ) | |   | | |        ");
        System.out.println("   | |  |  __)  (_____  )  | |  | |   | | |   | | |        ");
        System.out.println("   | |  | (           ) |  | |  | |   ) | |   | | |        ");
        System.out.println("   | |  | (____/Y\\____) |  | |  | (__/  ) (___) | (____/\\  ");
        System.out.println("   )_(  (_______|_______)  )_(  (______/(_______)_______/  ");
        System.out.println("  TestDoc - Show the world what your tests do. Version 0.2.2");
    }

}

package no.uio.tools.testdoc.main;

import org.apache.maven.plugin.logging.Log;

public class TestDoc {

    public static void outputBanner(final Log log, final String name) {
        log.info("________________ ______________________  _______ _______  ");
        log.info("\\__   __/  ____ \\  ____ \\__   __/  __  \\(  ___  )  ____ \\ ");
        log.info("   ) (  | (    \\/ (    \\/  ) (  | (  \\  ) (   ) | (    \\/  ");
        log.info("   | |  | (__   | (_____   | |  | |   ) | |   | | |        ");
        log.info("   | |  |  __)  (_____  )  | |  | |   | | |   | | |        ");
        log.info("   | |  | (           ) |  | |  | |   ) | |   | | |        ");
        log.info("   | |  | (____/Y\\____) |  | |  | (__/  ) (___) | (____/\\  ");
        log.info("   )_(  (_______|_______)  )_(  (______/(_______)_______/  ");
        log.info("  TestDoc - Show the world what your tests do. Version 0.2.7 (" + name + ")");
    }

}

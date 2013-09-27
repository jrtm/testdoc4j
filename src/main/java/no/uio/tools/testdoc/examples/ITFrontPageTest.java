package no.uio.tools.testdoc.examples;

import no.uio.tools.testdoc.annotations.TestDocPlan;
import no.uio.tools.testdoc.annotations.TestDocTask;
import no.uio.tools.testdoc.annotations.TestDocTasks;
import no.uio.tools.testdoc.annotations.TestDocTest;

import org.junit.Before;
import org.junit.Test;

@TestDocPlan(title = "Test av førstesiden inkludert søking!", sortOrder = -1)
public class ITFrontPageTest {

    // hentet tester fra https://www.uio.no/tjenester/it/applikasjoner/meldeapp/drift-utvikling/test/testplan.html
    @Test
    @TestDocTest("Test at vi er kommet til riktig side")
    @TestDocTasks(@TestDocTask(value = "Gå til førstesiden", checks = "Sjekk at tittelen er riktig?"))
    public void testTitle() {
    }


    @Test
    @TestDocTest("Gå til søkesiden")
    @TestDocTask(value = "Gå til søkeside", checks = "Sjekk at tittelen er riktig")
    public void searchForUSIT() {
        simpleSearch();
        detailsPage();
    }


    @TestDocTest("Ikke implementert test")
    @TestDocTask(value = "Ikke implementert", checks = "Ikke implementert")
    public void testNotImplement() {
    }


    @Test
    @TestDocTest("Test enkeltøk")
    @TestDocTasks({ @TestDocTask(value = "Gå inn på startsiden", checks = "Søkefeltet er tilgjengelig og i fokus"),
            @TestDocTask(value = "Søk på 'USIT'", checks = { "Minst fire elementer i trefflisten", "Sjekk mer." }) })
    public void simpleSearch() {
    }


    @Test
    @TestDocTest("Test av detaljside")
    @TestDocTasks({
            @TestDocTask(value = "Klikk på 'cerebrum' i trefflisten", checks = "Detaljsiden kommer frem"),
            @TestDocTask(value = "Les detaljer om personopplsyninger utleveres til andre innen UiOs nett", checks = "Det står 'Ja' og til hvem"),
            @TestDocTask(value = "Sjekk kategorier", checks = "Det står 'studenter','ansatte' og 'andre'") })
    public void detailsPage() {
    }


    @Test
    @TestDocTest("Test av avansert søk")
    @TestDocTasks({
            @TestDocTask(value = "Klikk på linke 'Avansert søk'", checks = "Side for avansert søk vises"),
            @TestDocTask(value = "Se at du er kommert til 'avansert' søkeside", checks = "Personopplysninger utleveres til andre:") })
    public void seeAdvancedSearchPage() {
    }


    @Before
    public void init() {
    }


    @Test
    public void doesNotContainsErrors() {
    }


    @Test
    public void goToAdvancedSearch() {
        seeAdvancedSearchPage();
    }

}

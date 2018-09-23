package vinted

import spock.lang.Specification

class ApplicationIntegrationSpec extends Specification {

    def outContent = new ByteArrayOutputStream()

    def originalOut = System.out

    def 'setup'() {
        System.setOut(new PrintStream(outContent))
    }

    def 'cleanup'() {
        System.setOut(originalOut)
    }

    def 'should write parsed transactions with bills to system output'() {
        when:
            Application.main()
            def outputContent = outContent.toString()
            outputContent = outputContent.substring(outputContent.indexOf('2015-02-01'))
        then:
            outputContent == new File(getClass().getResource('/output.txt').toURI()).text
    }
}

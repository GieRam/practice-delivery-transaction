package vinted.delivery.transaction

import spock.lang.Specification
import vinted.delivery.transaction.entities.Transaction
import vinted.delivery.transaction.service.factory.SpaceSeparatedFactory
import vinted.delivery.transaction.service.input.FileTransactionParser
import vinted.delivery.transaction.service.input.Parser
import vinted.delivery.transaction.validation.SpaceSeparatedValidator

import java.nio.file.Paths

class TransactionParserSpec extends Specification {

    Parser parser = new FileTransactionParser(new SpaceSeparatedFactory(new SpaceSeparatedValidator()))

    def 'should parse File to transactions'() {
        given:
            def path = Paths.get(getClass().getResource('/input.txt').toURI())
        when:
            List<Transaction> transactions = parser.parse(path)
        then:
            transactions.size() == 21
    }
}

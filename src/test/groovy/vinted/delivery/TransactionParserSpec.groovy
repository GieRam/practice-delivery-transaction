package vinted.delivery

import spock.lang.Specification
import vinted.delivery.entities.Transaction
import vinted.delivery.service.factory.DefaultTransactionFactory
import vinted.delivery.service.input.FileTransactionParser
import vinted.delivery.service.input.TransactionParser
import vinted.delivery.validation.TransactionEntryValidator

import java.nio.file.Paths

class TransactionParserSpec extends Specification {

    TransactionParser parser = new FileTransactionParser(new DefaultTransactionFactory(new TransactionEntryValidator()))

    def 'should parse File to transactions'() {
        given:
            def path = Paths.get(getClass().getResource('/input.txt').toURI())
        when:
            List<Transaction> transactions = parser.parse(path)
        then:
            transactions.size() == 21
    }
}

package vinted.delivery

import spock.lang.Specification
import vinted.delivery.entities.Transaction
import vinted.delivery.service.TransactionFactory
import vinted.delivery.service.TransactionParser
import vinted.delivery.validation.EntryValidator

import java.nio.file.Paths


class TransactionParserSpec extends Specification {

    TransactionParser parser = new TransactionParser(new TransactionFactory(" ", new EntryValidator()))

    def 'should parse File to transactions'() {
        given:
            def path = Paths.get(getClass().getResource('/input.txt').toURI())
        when:
            List<Transaction> transactions = parser.parse(path)
        then:
            transactions.size() == 21
    }
}

package vinted.delivery

import spock.lang.Specification
import vinted.delivery.entities.Transaction
import vinted.delivery.service.TransactionFactory
import vinted.delivery.validation.Validator


class TransactionFactorySpec extends Specification {

    String separator

    Validator validator

    TransactionFactory transactionFactory

    def 'setup'() {
        separator = ' '
        validator = Mock(Validator)
        transactionFactory = new TransactionFactory(separator, validator)
    }

    def 'should create transaction'(String transactionEntry, boolean valid, boolean result) {
        given:
            validator.valid(transactionEntry.split(separator)) >> valid
        when:
            Transaction transaction = transactionFactory.create(transactionEntry)
        then:
            transaction.toString().contains('Ignored') == result
        where:
            transactionEntry   | valid || result
            '2015-01-02 S MR'  | true  || false
            '2015-02-29 CUSPS' | false || true
    }
}

package vinted.delivery.transaction

import spock.lang.Specification
import vinted.delivery.transaction.entities.Transaction
import vinted.delivery.transaction.service.factory.SpaceSeparatedFactory
import vinted.delivery.transaction.service.factory.Factory
import vinted.delivery.transaction.validation.TransactionValidator


class TransactionFactorySpec extends Specification {

    TransactionValidator validator

    Factory transactionFactory

    def 'setup'() {
        validator = Mock(TransactionValidator)
        transactionFactory = new SpaceSeparatedFactory(validator)
    }

    def 'should create transaction'(String transactionEntry, boolean valid, boolean result) {
        given:
            validator.isValid(transactionEntry) >> valid
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

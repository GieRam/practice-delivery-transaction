package vinted.delivery

import spock.lang.Specification
import vinted.delivery.entities.Transaction
import vinted.delivery.service.factory.DefaultTransactionFactory
import vinted.delivery.service.factory.TransactionFactory
import vinted.delivery.validation.EntryValidator


class TransactionFactorySpec extends Specification {

    EntryValidator validator

    TransactionFactory transactionFactory

    def 'setup'() {
        validator = Mock(EntryValidator)
        transactionFactory = new DefaultTransactionFactory(validator)
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

package vinted.delivery.transaction.service.factory

import spock.lang.Specification
import vinted.delivery.transaction.enums.PackageSize
import vinted.delivery.transaction.enums.Provider
import vinted.delivery.transaction.validation.TransactionValidator

import java.time.LocalDate


class SpaceSeparatedFactorySpec extends Specification {

    TransactionValidator validator

    TransactionFactory transactionFactory

    def 'setup'() {
        validator = Mock(TransactionValidator)
        transactionFactory = new SpaceSeparatedFactory(validator)
    }

    def 'should create valid transaction'() {
        given:
            def transactionEntry = '2015-01-02 S MR'
            validator.isValid(transactionEntry) >> true
        when:
            def transaction = transactionFactory.create(transactionEntry)
        then:
            !transaction.isIgnored()
            transaction.getOrigin() == transactionEntry
            transaction.getProvider() == Provider.MR
            transaction.getPackageSize() == PackageSize.S
            transaction.getDate() == LocalDate.parse('2015-01-02')
    }

    def 'should create ignored transaction'() {
        given:
            def transactionEntry = '2015-02-29 CUSPS'
            validator.isValid(transactionEntry) >> false
        when:
            def transaction = transactionFactory.create(transactionEntry)
        then:
            transaction.isIgnored()
            transaction.getOrigin() == transactionEntry
            transaction.getDate() == null
            transaction.getPackageSize() == null
            transaction.getProvider() == null
    }
}

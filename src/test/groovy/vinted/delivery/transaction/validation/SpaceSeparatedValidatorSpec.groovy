package vinted.delivery.transaction.validation

import spock.lang.Specification


class SpaceSeparatedValidatorSpec extends Specification {

    TransactionValidator validator

    def 'setup'() {
        validator = new SpaceSeparatedValidator()
    }

    def 'should return expected validation'(transactionEntry, expected) {
        expect:
            validator.isValid(transactionEntry) == expected
        where:
            transactionEntry      || expected
            '2015-03-01 S MR'     || true
            '2015-02-29 S MR'     || false
            '2015-03-01 XX MR'    || false
            '2015-03-01 S XX'     || false
            '2015-03-01 S'        || false
            '2015-03-01 S MR XX'  || false
    }
}

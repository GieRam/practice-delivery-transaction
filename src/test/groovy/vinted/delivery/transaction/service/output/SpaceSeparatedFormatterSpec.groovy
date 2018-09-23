package vinted.delivery.transaction.service.output

import spock.lang.Specification
import vinted.delivery.transaction.entities.Transaction

import java.time.LocalDate

import static vinted.delivery.transaction.enums.PackageSize.S
import static vinted.delivery.transaction.enums.Provider.LP


class SpaceSeparatedFormatterSpec extends Specification {

    TransactionFormatter formatter

    def 'setup'() {
        formatter = new SpaceSeparatedFormatter()
    }

    def 'should format valid with discount'() {
        given:
            def transaction = new Transaction(LocalDate.parse('2015-05-05'), S, LP, 'origin')
            transaction.setPrice(1.8)
            transaction.setDiscount(0.345)
        expect:
            formatter.format(transaction) == '2015-05-05 S LP 1.46 0.34'
    }

    def 'should format valid without discount'() {
        given:
            def transaction = new Transaction(LocalDate.parse('2015-05-05'), S, LP, 'origin')
            transaction.setPrice(1.8)
        expect:
            formatter.format(transaction) == '2015-05-05 S LP 1.80 -'
    }

    def 'should format ignored'() {
        given:
            def transaction = new Transaction('origin')
        expect:
            formatter.format(transaction) == 'origin Ignored'
    }
}

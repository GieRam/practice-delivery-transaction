package vinted.delivery.transaction.service.discount

import spock.lang.Specification
import vinted.delivery.transaction.entities.Transaction

import java.time.LocalDate

import static vinted.delivery.transaction.enums.PackageSize.*
import static vinted.delivery.transaction.enums.Provider.*


class PeriodicFreeShipmentRuleSpec extends Specification {

    def discountRule

    def now

    def nextMonth

    def 'setup'() {
        now = LocalDate.now()
        nextMonth = LocalDate.now().plusMonths(1)
    }

    def 'third L shipment via LP should be free once a calendar month'() {
        given:
            discountRule = new PeriodicFreeShipmentRule(
                    DiscountPredicates.&isLargePostPackage, DiscountPredicates.&oncePerMonthRule, 3, 10, true)
            def transactions = [
                    new Transaction(now, L, LP, 'origin'),
                    new Transaction(now, L, LP, 'origin'),
                    new Transaction(now, L, LP, 'origin'),
                    new Transaction(now, L, LP, 'origin'),
                    new Transaction(nextMonth, L, LP, 'origin'),
                    new Transaction(nextMonth, L, LP, 'origin'),
                    new Transaction(nextMonth, L, LP, 'origin'),
                    new Transaction(nextMonth, L, LP, 'origin'),
            ]
            transactions.each { it -> it.setPrice(6.9) }
        when:
            discountRule.apply(transactions)
            def discounts = transactions.collect { it -> it.getBill().getDiscount() }
        then:
            discounts == [0, 0, 6.9, 0, 0, 0, 6.9, 0]
    }
}

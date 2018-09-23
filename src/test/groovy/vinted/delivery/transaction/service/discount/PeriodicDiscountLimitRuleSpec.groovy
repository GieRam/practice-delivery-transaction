package vinted.delivery.transaction.service.discount

import spock.lang.Specification
import vinted.delivery.transaction.entities.Transaction
import vinted.delivery.transaction.enums.PackageSize
import vinted.delivery.transaction.enums.Provider

import java.time.LocalDate


class PeriodicDiscountLimitRuleSpec extends Specification {

    def now

    def nextMonth

    def discountRule

    def 'setup'() {
        now = LocalDate.now()
        nextMonth = LocalDate.now().plusMonths(1)
    }

    def 'should limit discounts to 10 per month'() {
        given:
            discountRule = new PeriodicDiscountLimitRule(10, DiscountPredicates.&oncePerMonthRule, 10, true)
            def transactions = [
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(nextMonth, PackageSize.L, Provider.LP, 'origin'),
            ]
            transactions.each { it -> it.setDiscount(6.9) }
        when:
            discountRule.apply(transactions)
            def discounts = transactions.collect { it -> it.getBill().getDiscount() }
        then:
            discounts == [6.9, 3.1, 0, 6.9]
    }
}

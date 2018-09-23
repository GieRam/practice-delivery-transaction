package vinted.delivery.transaction.service.discount

import spock.lang.Specification
import spock.lang.Unroll
import vinted.delivery.transaction.entities.Transaction
import vinted.delivery.transaction.enums.PackageSize
import vinted.delivery.transaction.enums.Provider

import static java.time.LocalDate.now

class DiscountPredicatesTest extends Specification {

    @Unroll
    def 'oncePerMonth should return expected'(currentDate, previousDate, result) {
        expect:
            DiscountPredicates.oncePerMonthRule(currentDate, previousDate) == result
        where:
            currentDate | previousDate                          || result
            now()       | now().minusMonths(1)  || true
            now()       | now()                                 || false
    }

    @Unroll
    def 'isLargePostPackage should return expected'(packageSize, provider, result) {
        expect:
            DiscountPredicates.isLargePostPackage(new Transaction(now(), packageSize, provider, 'origin')) == result
        where:
            packageSize     | provider      || result
            PackageSize.L   | Provider.LP   || true
            PackageSize.S   | Provider.LP   || false

    }
}

package vinted.delivery.transaction.repository

import spock.lang.Specification

import static vinted.delivery.transaction.enums.PackageSize.*
import static vinted.delivery.transaction.enums.Provider.LP
import static vinted.delivery.transaction.enums.Provider.MR


class ShippingPriceRepositorySpec extends Specification {

    def repository

    def 'setup'() {
        repository = new ShippingPriceRepository()
    }

    def 'should find expected price'(packageSize, provider, expected) {
        expect:
            repository.findPrice(packageSize, provider) == expected
        where:
            packageSize | provider    || expected
            S           | LP          || 1.5
            M           | LP          || 4.9
            L           | LP          || 6.9
            S           | MR          || 2.0
            M           | MR          || 3.0
            L           | MR          || 4.0
    }

    def 'should find lowest price for size'(packageSize, expected) {
        expect:
            repository.findLowestPrice(packageSize) == expected
        where:
            packageSize || expected
            S           || 1.5
            M           || 3.0
            L           || 4.0
    }
}

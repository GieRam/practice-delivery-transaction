package vinted.delivery.entities

import spock.lang.Specification


class BillSpec extends Specification {

    def 'should get final price'() {
        given:
            Bill bill = new Bill(BigDecimal.TEN, BigDecimal.ONE)
        expect:
            bill.getFinalPrice() == BigDecimal.valueOf(9)
    }
}

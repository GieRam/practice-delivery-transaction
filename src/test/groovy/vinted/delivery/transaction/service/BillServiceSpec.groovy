package vinted.delivery.transaction.service

import spock.lang.Specification
import vinted.delivery.transaction.entities.Transaction
import vinted.delivery.transaction.enums.PackageSize
import vinted.delivery.transaction.enums.Provider
import vinted.delivery.transaction.repository.PriceRepository
import vinted.delivery.transaction.service.discount.DiscountRule

import java.time.LocalDate

class BillServiceSpec extends Specification {

    PriceRepository priceRepository

    BillService billService

    LocalDate now

    DiscountRule enabledRule

    DiscountRule disabledRule

    def 'setup'() {
        priceRepository = Mock(PriceRepository)
        enabledRule = Mock(DiscountRule)
        enabledRule.isEnabled() >> true
        disabledRule = Mock(DiscountRule)
        disabledRule.isEnabled() >> false
        billService = new BillService(priceRepository, [enabledRule, disabledRule])
        now = LocalDate.now()
    }

    def 'should set price and apply enabled discount rule'() {
        given:
            def transactions = [
                    new Transaction(now, PackageSize.S, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.LP, 'origin'),
                    new Transaction('origin')
            ]
        when:
            billService.apply(transactions)
        then:
            transactions[0].getBill().getPrice() == 2
            transactions[1].getBill().getPrice() == 2
            2 * priceRepository.findPrice(_, _) >> 2
            1 * enabledRule.apply(_)
            0 * disabledRule.apply(_)
    }

    def 'should do nothing for no applicable transactions'(transactions) {
        when:
            billService.apply(transactions)
        then:
            0 * priceRepository.findPrice(_)
            0 * enabledRule.apply(_)
            0 * disabledRule.apply(_)
        where:
            transactions << [[], [new Transaction('ignored')]]

    }
}

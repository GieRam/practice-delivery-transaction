package vinted.delivery.transaction.service.discount

import spock.lang.Specification
import vinted.delivery.transaction.entities.Transaction
import vinted.delivery.transaction.repository.PriceRepository

import java.time.LocalDate

import static vinted.delivery.transaction.enums.PackageSize.S
import static vinted.delivery.transaction.enums.Provider.LP
import static vinted.delivery.transaction.enums.Provider.MR


class LowestPackagePriceRuleSpec extends Specification {

    PriceRepository priceRepository

    LocalDate now

    def 'setup'() {
        priceRepository = Mock(PriceRepository)
        now = LocalDate.now()
    }

    def 'all S shipments should always match lowest S package price among providers'() {
        given:
            priceRepository.findLowestPrice(S) >> 1.5
            def discountRule = new LowestPackagePriceRule(priceRepository, S, 1, true)

            def transactions = [
                    new Transaction(now, S, LP, 'origin'),
                    new Transaction(now, S, MR, 'origin')
            ]
            transactions[0].setPrice(1.5)
            transactions[1].setPrice(2)
        when:
            discountRule.apply(transactions)
            def discounts = transactions.collect { it -> it.getBill().getDiscount() }
        then:
            discounts == [0, 0.5]
    }
}

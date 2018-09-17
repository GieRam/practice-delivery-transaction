package vinted.delivery.service

import spock.lang.Specification
import vinted.delivery.entities.Transaction
import vinted.delivery.enums.PackageSize
import vinted.delivery.enums.Provider
import vinted.delivery.repository.PriceRepository

import java.time.LocalDate

class PricingServiceSpec extends Specification {

    PriceRepository repository

    PricingService pricingService

    LocalDate now

    LocalDate nextMonth

    def 'setup'() {
        repository = Mock(PriceRepository)
        repository.findLowestPrice(PackageSize.S) >> 1.5
        pricingService = new PricingService(repository)
        now = LocalDate.now()
        nextMonth = LocalDate.now().plusMonths(1)
    }

    def 'all S shipments should always match lowest S package price among providers'() {
        given:
            repository.findPrice(PackageSize.S, Provider.LP) >> 1.5
            repository.findPrice(PackageSize.S, Provider.MR) >> 2

            def transactions = [
                    new Transaction(now, PackageSize.S, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin')
            ]
        when:
            def result = pricingService.applyPricing(transactions).collect { it -> it.getBill() }
        then:
            result.size() == 2
            result[0].getPrice() == 1.5 && result[0].getDiscount() == 0
            result[1].getPrice() == 2 && result[1].getDiscount() == 0.5
    }

    def 'third L shipment via LP should be free once a calendar month'() {
        given:
            repository.findPrice(PackageSize.L, Provider.LP) >> 6.9
            repository.findPrice(PackageSize.L, Provider.MR) >> 4

            def transactions = [
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.L, Provider.MR, 'origin'),
                    new Transaction(nextMonth, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(nextMonth, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(nextMonth, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(nextMonth, PackageSize.L, Provider.LP, 'origin'),
            ]
        when:
            def result = pricingService.applyPricing(transactions).collect { it -> it.getBill() }
        then:
            result.size() == 9
            result[0].getPrice() == 6.9 && result[0].getDiscount() == 0
            result[1].getPrice() == 6.9 && result[1].getDiscount() == 0
            result[2].getPrice() == 6.9 && result[2].getDiscount() == 6.9
            result[3].getPrice() == 6.9 && result[3].getDiscount() == 0
            result[4].getPrice() == 4.0 && result[4].getDiscount() == 0
            result[5].getPrice() == 6.9 && result[5].getDiscount() == 0
            result[6].getPrice() == 6.9 && result[6].getDiscount() == 0
            result[7].getPrice() == 6.9 && result[7].getDiscount() == 6.9
            result[8].getPrice() == 6.9 && result[8].getDiscount() == 0

    }

    def 'should limit discounts to 10 eur per month'() {
        given:
            repository.findPrice(PackageSize.L, Provider.LP) >> 6.9
            repository.findPrice(PackageSize.S, Provider.MR) >> 2

            def transactions = [
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(nextMonth, PackageSize.S, Provider.MR, 'origin')
            ]
        when:
            def result = pricingService.applyPricing(transactions).collect { it -> it.getBill() }
        then:
            result.size() == 11
            result[0].getPrice() == 6.9 && result[0].getDiscount() == 0
            result[1].getPrice() == 6.9 && result[1].getDiscount() == 0
            result[2].getPrice() == 6.9 && result[2].getDiscount() == 6.9
            result[3].getPrice() == 2 && result[3].getDiscount() == 0.5
            result[4].getPrice() == 2 && result[4].getDiscount() == 0.5
            result[5].getPrice() == 2 && result[5].getDiscount() == 0.5
            result[6].getPrice() == 2 && result[6].getDiscount() == 0.5
            result[7].getPrice() == 2 && result[7].getDiscount() == 0.5
            result[8].getPrice() == 2 && result[8].getDiscount() == 0.5
            result[9].getPrice() == 2 && result[9].getDiscount() == 0.1
            result[10].getPrice() == 2 && result[10].getDiscount() == 0.5
    }
}

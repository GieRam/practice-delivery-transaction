package vinted.delivery.service

import spock.lang.Specification
import vinted.delivery.entities.Transaction
import vinted.delivery.enums.PackageSize
import vinted.delivery.enums.Provider
import vinted.delivery.repository.PriceRepository

import java.time.LocalDate

class BillServiceSpec extends Specification {

    PriceRepository repository

    BillService billService

    LocalDate now

    LocalDate nextMonth

    def 'setup'() {
        repository = Mock(PriceRepository)
        repository.findLowestPrice(PackageSize.S) >> 1.5
        billService = new BillService(repository)
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
            billService.apply(transactions)
            def bills = transactions.collect { it -> it.getBill() }
        then:
            bills.size() == 2
            bills[0].getPrice() == 1.5 && bills[0].getDiscount() == 0
            bills[1].getPrice() == 2 && bills[1].getDiscount() == 0.5
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
            billService.apply(transactions)
            def bills = transactions.collect { it -> it.getBill() }
        then:
            bills.size() == 9
            bills[0].getPrice() == 6.9 && bills[0].getDiscount() == 0
            bills[1].getPrice() == 6.9 && bills[1].getDiscount() == 0
            bills[2].getPrice() == 6.9 && bills[2].getDiscount() == 6.9
            bills[3].getPrice() == 6.9 && bills[3].getDiscount() == 0
            bills[4].getPrice() == 4.0 && bills[4].getDiscount() == 0
            bills[5].getPrice() == 6.9 && bills[5].getDiscount() == 0
            bills[6].getPrice() == 6.9 && bills[6].getDiscount() == 0
            bills[7].getPrice() == 6.9 && bills[7].getDiscount() == 6.9
            bills[8].getPrice() == 6.9 && bills[8].getDiscount() == 0

    }

    def 'should limit discounts to 10 eur per month'() {
        given:
            repository.findPrice(PackageSize.L, Provider.LP) >> 6.9
            repository.findPrice(PackageSize.S, Provider.MR) >> 2

            def transactions = [
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.L, Provider.LP, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(now, PackageSize.S, Provider.MR, 'origin'),
                    new Transaction(nextMonth, PackageSize.S, Provider.MR, 'origin')
            ]
        when:
            billService.apply(transactions)
            def bills = transactions.collect { it -> it.getBill() }
        then:
            bills.size() == 11
            bills[0].getPrice() == 6.9 && bills[0].getDiscount() == 0
            bills[1].getPrice() == 6.9 && bills[1].getDiscount() == 0
            bills[2].getPrice() == 2 && bills[2].getDiscount() == 0.5
            bills[3].getPrice() == 6.9 && bills[3].getDiscount() == 6.9
            bills[4].getPrice() == 2 && bills[4].getDiscount() == 0.5
            bills[5].getPrice() == 2 && bills[5].getDiscount() == 0.5
            bills[6].getPrice() == 2 && bills[6].getDiscount() == 0.5
            bills[7].getPrice() == 2 && bills[7].getDiscount() == 0.5
            bills[8].getPrice() == 2 && bills[8].getDiscount() == 0.5
            bills[9].getPrice() == 2 && bills[9].getDiscount() == 0.1
            bills[10].getPrice() == 2 && bills[10].getDiscount() == 0.5
    }
}

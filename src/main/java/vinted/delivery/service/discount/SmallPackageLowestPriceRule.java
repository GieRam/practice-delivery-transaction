package vinted.delivery.service.discount;

import vinted.delivery.entities.Transaction;
import vinted.delivery.enums.PackageSize;
import vinted.delivery.repository.PriceRepository;

import java.math.BigDecimal;
import java.util.List;

public class SmallPackageLowestPriceRule implements DiscountRule {

    private PriceRepository priceRepository;

    public SmallPackageLowestPriceRule(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public void apply(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getPackageSize() != PackageSize.S) {
                continue;
            }
            BigDecimal lowestPrice = priceRepository.findLowestPrice(transaction.getPackageSize());
            transaction.setDiscount(transaction.getBill().getPrice().subtract(lowestPrice));
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }

    @Override
    public boolean enabled() {
        return true;
    }
}

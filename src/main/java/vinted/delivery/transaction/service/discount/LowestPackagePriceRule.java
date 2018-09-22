package vinted.delivery.transaction.service.discount;

import vinted.delivery.transaction.entities.Transaction;
import vinted.delivery.transaction.enums.PackageSize;
import vinted.delivery.transaction.repository.PriceRepository;

import java.math.BigDecimal;
import java.util.List;

public class LowestPackagePriceRule implements DiscountRule {

    private final PriceRepository priceRepository;

    private final PackageSize packageSize;

    private final int order;

    private final boolean enabled;

    public LowestPackagePriceRule(PriceRepository priceRepository,
                                  PackageSize packageSize,
                                  int order,
                                  boolean enabled) {
        this.priceRepository = priceRepository;
        this.packageSize = packageSize;
        this.order = order;
        this.enabled = enabled;
    }

    @Override
    public void apply(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getPackageSize() != packageSize) {
                continue;
            }
            BigDecimal lowestPrice = priceRepository.findLowestPrice(transaction.getPackageSize());
            transaction.setDiscount(transaction.getBill().getPrice().subtract(lowestPrice));
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

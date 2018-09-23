package vinted.delivery.transaction.service.discount;

import vinted.delivery.transaction.entities.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiPredicate;

public class PeriodicDiscountLimitRule implements DiscountRule {

    private final BigDecimal discountLimit;

    private final BiPredicate<LocalDate, LocalDate> limitResetTest;

    private final int order;

    private final boolean enabled;

    public PeriodicDiscountLimitRule(BigDecimal discountLimit,
                                     BiPredicate<LocalDate, LocalDate> limitResetTest,
                                     int order,
                                     boolean enabled) {
        this.discountLimit = discountLimit;
        this.limitResetTest = limitResetTest;
        this.order = order;
        this.enabled = enabled;
    }

    @Override
    public void apply(List<Transaction> transactions) {
        LocalDate currentPeriod;
        BigDecimal accumulated = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            currentPeriod = transaction.getDate();
            if (limitResetTest.test(transaction.getDate(), currentPeriod)) {
                accumulated = BigDecimal.ZERO;
            }
            accumulated = accumulated.add(transaction.getBill().getDiscount());

            if (accumulated.compareTo(discountLimit) >= 0) {
                BigDecimal discountOverflow = accumulated.subtract(discountLimit);
                transaction.setDiscount(transaction.getBill().getDiscount().subtract(discountOverflow));
                break;
            }
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

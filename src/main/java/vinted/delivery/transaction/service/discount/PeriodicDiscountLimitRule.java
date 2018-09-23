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
        LocalDate currentPeriod = transactions.get(0).getDate();
        BigDecimal accumulated = BigDecimal.ZERO;
        boolean appliedForPeriod = false;

        for (Transaction transaction : transactions) {
            if (limitResetTest.test(transaction.getDate(), currentPeriod)) {
                accumulated = BigDecimal.ZERO;
                appliedForPeriod = false;
            }
            if (appliedForPeriod) {
                transaction.setDiscount(BigDecimal.ZERO);
            }
            currentPeriod = transaction.getDate();
            accumulated = accumulated.add(transaction.getBill().getDiscount());
            if (accumulated.compareTo(discountLimit) >= 0 && !appliedForPeriod) {
                BigDecimal discountOverflow = accumulated.subtract(discountLimit);
                transaction.setDiscount(transaction.getBill().getDiscount().subtract(discountOverflow));
                appliedForPeriod = true;
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

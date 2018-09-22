package vinted.delivery.service.discount;

import vinted.delivery.entities.Transaction;

import java.math.BigDecimal;
import java.util.List;

import static vinted.ApplicationContext.MAX_DISCOUNT_PER_MONTH;

public class AccumulatedDiscountRule implements DiscountRule {

    @Override
    public void apply(List<Transaction> transactions) {
        int currentMonth = 0;
        BigDecimal accumulated = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction.getDate().getMonthValue() != currentMonth) {
                currentMonth = transaction.getDate().getMonthValue();
                accumulated = BigDecimal.ZERO;
            }
            accumulated = accumulated.add(transaction.getBill().getDiscount());

            if (accumulated.compareTo(MAX_DISCOUNT_PER_MONTH) >= 0) {
                BigDecimal discountOverflow = accumulated.subtract(MAX_DISCOUNT_PER_MONTH);
                transaction.setDiscount(transaction.getBill().getDiscount().subtract(discountOverflow));
                break;
            }
        }
    }

    @Override
    public int getOrder() {
        return 30;
    }

    @Override
    public boolean enabled() {
        return true;
    }
}

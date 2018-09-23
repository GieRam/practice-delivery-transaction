package vinted.delivery.transaction.service.discount;

import vinted.delivery.transaction.entities.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class PeriodicFreeShipmentRule implements DiscountRule {

    private final int nextShipmentFreeLimit;

    private final Predicate<Transaction> appliesTo;

    private final BiPredicate<LocalDate, LocalDate> countResetTest;

    private final int order;

    private final boolean enabled;

    public PeriodicFreeShipmentRule(Predicate<Transaction> appliesTo,
                                    BiPredicate<LocalDate, LocalDate> countResetTest,
                                    int nextShipmentFreeLimit,
                                    int order,
                                    boolean enabled) {
        this.nextShipmentFreeLimit = nextShipmentFreeLimit;
        this.countResetTest = countResetTest;
        this.appliesTo = appliesTo;
        this.order = order;
        this.enabled = enabled;
    }

    @Override
    public void apply(List<Transaction> transactions) {
        LocalDate currentPeriod;
        int shipmentCount = 0;

        for (Transaction transaction : transactions) {
            if (!appliesTo.test(transaction)) {
                continue;
            }
            shipmentCount++;
            currentPeriod = transaction.getDate();
            if (countResetTest.test(transaction.getDate(), currentPeriod)) {
                shipmentCount = 0;
            }
            if (shipmentCount == nextShipmentFreeLimit) {
                transaction.setDiscount(transaction.getBill().getPrice());
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

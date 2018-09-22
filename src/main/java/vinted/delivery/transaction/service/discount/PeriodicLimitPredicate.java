package vinted.delivery.transaction.service.discount;

import java.time.LocalDate;

@FunctionalInterface
public interface PeriodicLimitPredicate {

    boolean test(LocalDate currentDate, LocalDate previousDate);
}

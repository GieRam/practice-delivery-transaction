package vinted.delivery.transaction.service.discount;

import vinted.delivery.transaction.entities.Transaction;
import vinted.delivery.transaction.enums.PackageSize;
import vinted.delivery.transaction.enums.Provider;

import java.time.LocalDate;

public class DiscountPredicates {

    public static boolean oncePerMonthRule(LocalDate currentDate, LocalDate previousDate) {
        return currentDate.getMonthValue() != previousDate.getMonthValue();
    }

    public static boolean isLargePostPackage(Transaction transaction) {
        return transaction.getPackageSize() == PackageSize.L && transaction.getProvider() == Provider.LP;
    }
}

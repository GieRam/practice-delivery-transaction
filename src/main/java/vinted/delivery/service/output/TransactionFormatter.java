package vinted.delivery.service.output;

import vinted.delivery.entities.Transaction;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class TransactionFormatter {

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public String format(Transaction transaction) {
        return transaction.isIgnored() ? formatIgnored(transaction) : formatValid(transaction);
    }

    private String formatValid(Transaction transaction) {
        return String.format(
            "%s %s %s %s %s",
            transaction.getDate().format(DateTimeFormatter.ISO_DATE),
            transaction.getPackageSize(),
            transaction.getProvider(),
            decimalFormat.format(transaction.getBill().getFinalPrice()),
            formatDiscount(transaction.getBill().getDiscount())
        );
    }

    private String formatDiscount(BigDecimal discount) {
        return discount.compareTo(BigDecimal.ZERO) > 0 ? decimalFormat.format(discount) : "-";
    }

    private String formatIgnored(Transaction transaction) {
        return String.format("%s Ignored", transaction.getOrigin());
    }
}

package vinted.delivery.transaction.service.output;

import vinted.delivery.transaction.entities.Transaction;

public interface TransactionFormatter {

    String format(Transaction transaction);
}

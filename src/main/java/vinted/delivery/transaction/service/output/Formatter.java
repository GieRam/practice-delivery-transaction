package vinted.delivery.transaction.service.output;

import vinted.delivery.transaction.entities.Transaction;

public interface Formatter {

    String format(Transaction transaction);
}

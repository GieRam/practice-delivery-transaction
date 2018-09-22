package vinted.delivery.transaction.service.factory;

import vinted.delivery.transaction.entities.Transaction;

public interface Factory {

    Transaction create(String transactionEntry);
}

package vinted.delivery.transaction.service.factory;

import vinted.delivery.transaction.entities.Transaction;

public interface TransactionFactory {

    Transaction create(String transactionEntry);
}

package vinted.delivery.service.factory;

import vinted.delivery.entities.Transaction;

public interface TransactionFactory {

    Transaction create(String transactionEntry);
}

package vinted.delivery.service.output;

import vinted.delivery.entities.Transaction;

import java.util.List;

public interface TransactionWriter {

    void write(List<Transaction> transactions);
}

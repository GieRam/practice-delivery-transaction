package vinted.delivery.transaction.service.output;

import vinted.delivery.transaction.entities.Transaction;

import java.util.List;

public interface Writer {

    void write(List<Transaction> transactions);
}

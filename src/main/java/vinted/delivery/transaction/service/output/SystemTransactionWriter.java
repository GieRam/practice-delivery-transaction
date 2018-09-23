package vinted.delivery.transaction.service.output;

import vinted.delivery.transaction.entities.Transaction;

import java.util.List;

public class SystemTransactionWriter implements TransactionWriter {

    private final TransactionFormatter formatter;

    public SystemTransactionWriter(TransactionFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void write(List<Transaction> transactions) {
        transactions.stream()
            .map(formatter::format)
            .forEach(System.out::println);
    }
}

package vinted.delivery.service;

import vinted.delivery.entities.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionParser {

    private final TransactionFactory transactionFactory;

    public TransactionParser(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public List<Transaction> parse(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                .map(transactionFactory::create)
                .collect(Collectors.toList());
        }
    }
}

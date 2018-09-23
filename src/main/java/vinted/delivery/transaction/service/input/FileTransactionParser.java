package vinted.delivery.transaction.service.input;

import vinted.delivery.transaction.entities.Transaction;
import vinted.delivery.transaction.service.factory.TransactionFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileTransactionParser implements Parser {

    private final TransactionFactory transactionFactory;

    public FileTransactionParser(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public List<Transaction> parse(Path path) throws IOException {
        System.out.println(String.format("Parsing transactions at path: %s", path.toString()));
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                .map(transactionFactory::create)
                .collect(Collectors.toList());
        }
    }
}

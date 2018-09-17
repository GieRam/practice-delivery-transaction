package vinted.delivery.service.input;

import vinted.delivery.entities.Transaction;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface TransactionParser {

    List<Transaction> parse(Path path) throws IOException;
}

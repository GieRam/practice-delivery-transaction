package vinted.delivery.transaction.service.input;

import vinted.delivery.transaction.entities.Transaction;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface Parser {

    List<Transaction> parse(Path path) throws IOException;
}

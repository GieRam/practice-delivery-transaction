package vinted.delivery.transaction.service.factory;

import vinted.delivery.transaction.entities.Transaction;
import vinted.delivery.transaction.enums.PackageSize;
import vinted.delivery.transaction.enums.Provider;
import vinted.delivery.transaction.validation.TransactionValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SpaceSeparatedFactory implements Factory {

    private final String SEPARATOR = " ";

    private final TransactionValidator validator;

    public SpaceSeparatedFactory(TransactionValidator validator) {
        this.validator = validator;
    }

    @Override
    public Transaction create(String transactionEntry) {
        return validator.isValid(transactionEntry) ? createTransaction(transactionEntry) : createIgnoredTransaction(transactionEntry);
    }

    private Transaction createTransaction(String transactionEntry) {
        String[] entryComponents = transactionEntry.split(SEPARATOR);

        return new Transaction(
            LocalDate.parse(entryComponents[0], DateTimeFormatter.ISO_DATE),
            PackageSize.valueOf(entryComponents[1]),
            Provider.valueOf(entryComponents[2]),
            String.join(SEPARATOR, entryComponents));
    }

    private Transaction createIgnoredTransaction(String transactionEntry) {
        return new Transaction(transactionEntry);
    }
}

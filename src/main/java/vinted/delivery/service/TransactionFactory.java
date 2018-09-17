package vinted.delivery.service;

import vinted.delivery.enums.PackageSize;
import vinted.delivery.enums.Provider;
import vinted.delivery.entities.Transaction;
import vinted.delivery.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// TODO: figure out this string split/join going all over the place.. will happen in output class too
public class TransactionFactory {

    private final String separator;

    private final Validator validator;

    public TransactionFactory(String separator, Validator validator) {
        this.separator = separator;
        this.validator = validator;
    }

    public Transaction create(String transactionEntry) {
        String[] entryComponents = transactionEntry.split(separator);

        return validator.valid(entryComponents) ? createTransaction(entryComponents) : createIgnoredTransaction(entryComponents);
    }

    private Transaction createTransaction(String[] entryComponents) {
        return new Transaction(
            LocalDate.parse(entryComponents[0], DateTimeFormatter.ISO_DATE),
            PackageSize.valueOf(entryComponents[1]),
            Provider.valueOf(entryComponents[2]),
            String.join(separator, entryComponents));
    }

    private Transaction createIgnoredTransaction(String[] entryComponents) {
        return new Transaction(String.join(separator, entryComponents));
    }
}

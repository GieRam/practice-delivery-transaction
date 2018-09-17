package vinted.delivery.service.factory;

import vinted.ApplicationContext;
import vinted.delivery.entities.Transaction;
import vinted.delivery.enums.PackageSize;
import vinted.delivery.enums.Provider;
import vinted.delivery.validation.EntryValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DefaultTransactionFactory implements TransactionFactory {

    private final EntryValidator validator;

    public DefaultTransactionFactory(EntryValidator validator) {
        this.validator = validator;
    }

    @Override
    public Transaction create(String transactionEntry) {
        return validator.isValid(transactionEntry) ? createTransaction(transactionEntry) : createIgnoredTransaction(transactionEntry);
    }

    private Transaction createTransaction(String transactionEntry) {
        String[] entryComponents = transactionEntry.split(ApplicationContext.SEPARATOR);

        return new Transaction(
            LocalDate.parse(entryComponents[0], DateTimeFormatter.ISO_DATE),
            PackageSize.valueOf(entryComponents[1]),
            Provider.valueOf(entryComponents[2]),
            String.join(ApplicationContext.SEPARATOR, entryComponents));
    }

    private Transaction createIgnoredTransaction(String transactionEntry) {
        return new Transaction(transactionEntry);
    }
}

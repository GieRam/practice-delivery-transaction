package vinted.delivery.validation;

import vinted.delivery.enums.PackageSize;
import vinted.delivery.enums.Provider;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EntryValidator implements Validator {

    private final List<Predicate<String[]>> validators;

    public EntryValidator() {
        this.validators = Arrays.asList(
            this::isValidLength,
            this::isValidDate,
            this::isValidPackageSize,
            this::isValidProvider
        );
    }

    public boolean valid(String[] entryComponents) {
        return validators.stream().allMatch(it -> it.test(entryComponents));
    }

    private Boolean isValidLength(String[] entryComponents) {
        return entryComponents.length > 2;
    }

    private Boolean isValidDate(String[] entryComponents) {
        return isValidArgument(() -> LocalDate.parse(entryComponents[0], DateTimeFormatter.ISO_DATE));
    }

    private Boolean isValidPackageSize(String[] entryComponents) {
        return isValidArgument(() -> PackageSize.valueOf((entryComponents[1])));
    }

    private Boolean isValidProvider(String[] entryComponents) {
        return isValidArgument(() -> Provider.valueOf((entryComponents[2])));
    }

    private Boolean isValidArgument(Supplier<Object> validator) {
        try {
            validator.get();
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }
}

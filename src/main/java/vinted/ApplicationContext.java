package vinted;

import vinted.delivery.repository.PriceRepository;
import vinted.delivery.repository.ShippingPriceRepository;
import vinted.delivery.service.BillService;
import vinted.delivery.service.factory.DefaultTransactionFactory;
import vinted.delivery.service.factory.TransactionFactory;
import vinted.delivery.service.input.FileTransactionParser;
import vinted.delivery.service.input.TransactionParser;
import vinted.delivery.service.output.SystemTransactionWriter;
import vinted.delivery.service.output.TransactionFormatter;
import vinted.delivery.service.output.TransactionWriter;
import vinted.delivery.validation.EntryValidator;
import vinted.delivery.validation.TransactionEntryValidator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    // TODO: Extract configuration
    public static final String SEPARATOR = " ";

    public static final BigDecimal MAX_DISCOUNT_PER_MONTH = BigDecimal.TEN;

    private Map<Class<?>, Object> beans;

    public ApplicationContext() {
        this.beans = new HashMap<>();
        wireApplication();
    }

    private void wireApplication() {
        beans.put(PriceRepository.class, new ShippingPriceRepository());
        beans.put(BillService.class, new BillService((getBean(PriceRepository.class))));
        beans.put(TransactionFormatter.class, new TransactionFormatter());
        beans.put(TransactionWriter.class, new SystemTransactionWriter(getBean(TransactionFormatter.class)));
        beans.put(EntryValidator.class, new TransactionEntryValidator());
        beans.put(TransactionFactory.class, new DefaultTransactionFactory(getBean(EntryValidator.class)));
        beans.put(TransactionParser.class, new FileTransactionParser(getBean(TransactionFactory.class)));
    }

    public <T> T getBean(Class<T> beanType) {
        return (T) beans.get(beanType);
    }
}

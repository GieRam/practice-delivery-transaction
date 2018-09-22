package vinted.common;

import vinted.delivery.transaction.entities.Transaction;
import vinted.delivery.transaction.enums.PackageSize;
import vinted.delivery.transaction.enums.Provider;
import vinted.delivery.transaction.repository.PriceRepository;
import vinted.delivery.transaction.repository.ShippingPriceRepository;
import vinted.delivery.transaction.service.BillService;
import vinted.delivery.transaction.service.discount.DiscountRule;
import vinted.delivery.transaction.service.discount.LowestPackagePriceRule;
import vinted.delivery.transaction.service.discount.PeriodicDiscountLimitRule;
import vinted.delivery.transaction.service.discount.PeriodicFreeShipmentRule;
import vinted.delivery.transaction.service.factory.SpaceSeparatedFactory;
import vinted.delivery.transaction.service.factory.Factory;
import vinted.delivery.transaction.service.input.FileTransactionParser;
import vinted.delivery.transaction.service.input.Parser;
import vinted.delivery.transaction.service.output.Formatter;
import vinted.delivery.transaction.service.output.SpaceSeparatedFormatter;
import vinted.delivery.transaction.service.output.SystemTransactionWriter;
import vinted.delivery.transaction.service.output.Writer;
import vinted.delivery.transaction.validation.SpaceSeparatedValidator;
import vinted.delivery.transaction.validation.TransactionValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {

    private Map<Class<?>, Object> beans;

    public ApplicationContext() {
        this.beans = new HashMap<>();
        wireApplication();
    }

    private void wireApplication() {
        beans.put(PriceRepository.class, new ShippingPriceRepository());
        beans.put(BillService.class, new BillService(getBean(PriceRepository.class), createDiscountRules()));
        beans.put(Formatter.class, new SpaceSeparatedFormatter());
        beans.put(Writer.class, new SystemTransactionWriter(getBean(Formatter.class)));
        beans.put(TransactionValidator.class, new SpaceSeparatedValidator());
        beans.put(Factory.class, new SpaceSeparatedFactory(getBean(TransactionValidator.class)));
        beans.put(Parser.class, new FileTransactionParser(getBean(Factory.class)));
    }

    private List<DiscountRule> createDiscountRules() {
        List<DiscountRule> discountRules = new ArrayList<>();
        discountRules.add(new LowestPackagePriceRule(getBean(PriceRepository.class), PackageSize.S, 10, true));
        discountRules.add(new PeriodicFreeShipmentRule(this::isLargePostPackage, this::oncePerMonthPredicate, 3, 20, true));
        discountRules.add(new PeriodicDiscountLimitRule(BigDecimal.TEN, this::oncePerMonthPredicate, 30, true));

        return discountRules;
    }

    private boolean isLargePostPackage(Transaction transaction) {
        return transaction.getPackageSize() == PackageSize.L && transaction.getProvider() == Provider.LP;
    }

    private boolean oncePerMonthPredicate(LocalDate currentDate, LocalDate previousDate) {
        return currentDate.getMonthValue() != previousDate.getMonthValue();
    }

    public <T> T getBean(Class<T> beanType) {
        return (T) beans.get(beanType);
    }
}

package vinted.common;

import vinted.delivery.transaction.enums.PackageSize;
import vinted.delivery.transaction.repository.PriceRepository;
import vinted.delivery.transaction.repository.ShippingPriceRepository;
import vinted.delivery.transaction.service.BillService;
import vinted.delivery.transaction.service.discount.*;
import vinted.delivery.transaction.service.factory.TransactionFactory;
import vinted.delivery.transaction.service.factory.SpaceSeparatedFactory;
import vinted.delivery.transaction.service.input.FileTransactionParser;
import vinted.delivery.transaction.service.input.Parser;
import vinted.delivery.transaction.service.output.SpaceSeparatedFormatter;
import vinted.delivery.transaction.service.output.SystemTransactionWriter;
import vinted.delivery.transaction.service.output.TransactionFormatter;
import vinted.delivery.transaction.service.output.TransactionWriter;
import vinted.delivery.transaction.validation.SpaceSeparatedValidator;
import vinted.delivery.transaction.validation.TransactionValidator;

import java.math.BigDecimal;
import java.util.*;

public class ApplicationContext {

    private static ApplicationContext instance;

    private Map<Class<?>, Object> beans;

    private ApplicationContext() {
        this.beans = new HashMap<>();
        wireApplication();
    }

    public static ApplicationContext getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    private void wireApplication() {
        beans.put(PriceRepository.class, new ShippingPriceRepository());
        beans.put(BillService.class, new BillService(getBean(PriceRepository.class), createDiscountRules()));
        beans.put(TransactionFormatter.class, new SpaceSeparatedFormatter());
        beans.put(TransactionWriter.class, new SystemTransactionWriter(getBean(TransactionFormatter.class)));
        beans.put(TransactionValidator.class, new SpaceSeparatedValidator());
        beans.put(TransactionFactory.class, new SpaceSeparatedFactory(getBean(TransactionValidator.class)));
        beans.put(Parser.class, new FileTransactionParser(getBean(TransactionFactory.class)));
    }

    private List<DiscountRule> createDiscountRules() {
        DiscountRule smallPackageLowestPrice = new LowestPackagePriceRule(getBean(PriceRepository.class), PackageSize.S, 10, true);
        DiscountRule thirdLargePostPackageFree = new PeriodicFreeShipmentRule(DiscountPredicates::isLargePostPackage, DiscountPredicates::oncePerMonthRule, 3, 20, true);
        DiscountRule maxTenDiscountPerMonth = new PeriodicDiscountLimitRule(BigDecimal.TEN, DiscountPredicates::oncePerMonthRule, 30, true);

        return Arrays.asList(
            smallPackageLowestPrice,
            thirdLargePostPackageFree,
            maxTenDiscountPerMonth
        );
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> beanType) {
        return (T) beans.get(beanType);
    }
}

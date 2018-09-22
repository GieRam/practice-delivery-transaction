package vinted.delivery.service;

import vinted.delivery.entities.Transaction;
import vinted.delivery.repository.PriceRepository;
import vinted.delivery.service.discount.DiscountRule;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BillService {

    private final PriceRepository priceRepository;

    private final List<DiscountRule> discountRules;

    public BillService(PriceRepository priceRepository, List<DiscountRule> discountRules) {
        this.priceRepository = priceRepository;
        this.discountRules = discountRules;
    }

    public void apply(List<Transaction> transactions) {
        List<Transaction> applicable = getApplicable(transactions);

        setPrices(applicable);
        discountRules.stream()
            .filter(DiscountRule::enabled)
            .sorted(Comparator.comparing(DiscountRule::getOrder))
            .forEach(it -> it.apply(applicable));
    }

    private List<Transaction> getApplicable(List<Transaction> transactions) {
        return transactions.stream()
            .filter(Transaction::isValid)
            .sorted(Comparator.comparing(Transaction::getDate))
            .collect(Collectors.toList());
    }

    private void setPrices(List<Transaction> transactions) {
        transactions.forEach(it -> it.setPrice(priceRepository.findPrice(it.getPackageSize(), it.getProvider())));
    }
}

package vinted.delivery.service.discount;

import vinted.delivery.entities.Transaction;

import java.util.List;

public interface DiscountRule {

    void apply(List<Transaction> transactions);

    int getOrder();

    boolean enabled();
}

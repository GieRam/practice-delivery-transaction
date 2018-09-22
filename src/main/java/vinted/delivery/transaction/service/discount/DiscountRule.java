package vinted.delivery.transaction.service.discount;

import vinted.delivery.transaction.entities.Transaction;

import java.util.List;

public interface DiscountRule {

    void apply(List<Transaction> transactions);

    int getOrder();

    boolean isEnabled();
}

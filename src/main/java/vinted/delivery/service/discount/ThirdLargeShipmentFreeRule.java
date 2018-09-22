package vinted.delivery.service.discount;

import vinted.delivery.entities.Transaction;
import vinted.delivery.enums.PackageSize;
import vinted.delivery.enums.Provider;

import java.util.List;

public class ThirdLargeShipmentFreeRule implements DiscountRule {

    @Override
    public void apply(List<Transaction> transactions) {
        int currentMonth = 0;
        int shipmentCount = 0;

        for (Transaction transaction : transactions) {
            if (!appliesTo(transaction)) {
                continue;
            }
            if (transaction.getDate().getMonthValue() != currentMonth) {
                currentMonth = transaction.getDate().getMonthValue();
                shipmentCount = 0;
            }
            if (shipmentCount == 2) {
                transaction.setDiscount(transaction.getBill().getPrice());
                break;
            }
            shipmentCount++;
        }
    }

    private boolean appliesTo(Transaction transaction) {
        return transaction.getProvider() == Provider.LP && transaction.getPackageSize() == PackageSize.L;
    }

    @Override
    public int getOrder() {
        return 20;
    }

    @Override
    public boolean enabled() {
        return true;
    }
}

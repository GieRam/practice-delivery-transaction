package vinted.delivery.service;

import vinted.delivery.entities.Transaction;
import vinted.delivery.enums.PackageSize;
import vinted.delivery.enums.Provider;
import vinted.delivery.repository.PriceRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static vinted.ApplicationContext.MAX_DISCOUNT_PER_MONTH;

public class BillService {

    private final PriceRepository priceRepository;

    private Function<Stream<Transaction>, Stream<Transaction>> setPrices = this::setPrices;

    public BillService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public List<Transaction> applyPricing(List<Transaction> transactions) {
        List<Transaction> sorted = transactions.stream()
            .filter(Transaction::isValid)
            .sorted(Comparator.comparing(Transaction::getDate))
            .collect(Collectors.toList());

        return setPrices
            .andThen(this::smallPackageLowestPrice)
            .andThen(this::freeThirdLargeShipment)
            .andThen(this::accumulatedDiscountConstraint)
            .apply(sorted.stream())
            .collect(Collectors.toList());
    }

    private Stream<Transaction> setPrices(Stream<Transaction> transactions) {
        return transactions
            .peek(transaction -> transaction.setPrice(priceRepository.findPrice(transaction.getPackageSize(), transaction.getProvider())));
    }

    private Stream<Transaction> smallPackageLowestPrice(Stream<Transaction> transactions) {
        return transactions
            .peek(transaction -> {
                if (transaction.getPackageSize() == PackageSize.S) {
                    BigDecimal lowestPrice = priceRepository.findLowestPrice(transaction.getPackageSize());
                    transaction.setDiscount(transaction.getBill().getPrice().subtract(lowestPrice));
                }
            });
    }

    private Stream<Transaction> freeThirdLargeShipment(Stream<Transaction> transactions) {
        final int[] currentMonth = {0};
        final int[] shipmentCount = {0};
        final boolean[] discountApplied = {false};

        return transactions
            .peek(transaction -> {
                if (transaction.getProvider() == Provider.LP && transaction.getPackageSize() == PackageSize.L) {
                    if (transaction.getDate().getMonthValue() != currentMonth[0]) {
                        currentMonth[0] = transaction.getDate().getMonthValue();
                        shipmentCount[0] = 0;
                        discountApplied[0] = false;
                    }
                    if (shipmentCount[0] == 2 && !discountApplied[0]) {
                        transaction.setDiscount(transaction.getBill().getPrice());
                        shipmentCount[0] = 0;
                        discountApplied[0] = true;
                        return;
                    }
                }
                shipmentCount[0] += 1;
            });
    }

    private Stream<Transaction> accumulatedDiscountConstraint(Stream<Transaction> transactions) {
        final int[] currentMonth = {0};
        final BigDecimal[] accumulatedDiscounts = {BigDecimal.ZERO};

        return transactions
            .peek(transaction -> {
                if (transaction.getDate().getMonthValue() != currentMonth[0]) {
                    currentMonth[0] = transaction.getDate().getMonthValue();
                    accumulatedDiscounts[0] = BigDecimal.ZERO;
                }
                if (accumulatedDiscounts[0].compareTo(MAX_DISCOUNT_PER_MONTH) < 0) {
                    accumulatedDiscounts[0] = accumulatedDiscounts[0].add(transaction.getBill().getDiscount());

                    if (accumulatedDiscounts[0].compareTo(MAX_DISCOUNT_PER_MONTH) >= 0) {
                        BigDecimal discountOverflow = accumulatedDiscounts[0].subtract(MAX_DISCOUNT_PER_MONTH);
                        transaction.setDiscount(transaction.getBill().getDiscount().subtract(discountOverflow));
                    }
                }
            });
    }
}

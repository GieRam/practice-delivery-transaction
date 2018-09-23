package vinted.delivery.transaction.repository;

import vinted.common.Tuple;
import vinted.delivery.transaction.entities.ShippingPrice;
import vinted.delivery.transaction.enums.PackageSize;
import vinted.delivery.transaction.enums.Provider;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static vinted.delivery.transaction.enums.PackageSize.*;
import static vinted.delivery.transaction.enums.Provider.*;

public class ShippingPriceRepository implements PriceRepository {

    private static final List<ShippingPrice> prices = Arrays.asList(
        ShippingPrice.createEuropeanShippingPrice(LP, S, BigDecimal.valueOf(1.5)),
        ShippingPrice.createEuropeanShippingPrice(LP, M, BigDecimal.valueOf(4.9)),
        ShippingPrice.createEuropeanShippingPrice(LP, L, BigDecimal.valueOf(6.9)),
        ShippingPrice.createEuropeanShippingPrice(MR, S, BigDecimal.valueOf(2.0)),
        ShippingPrice.createEuropeanShippingPrice(MR, M, BigDecimal.valueOf(3.0)),
        ShippingPrice.createEuropeanShippingPrice(MR, L, BigDecimal.valueOf(4.0))
    );

    private final Map<PackageSize, BigDecimal> lowestPriceCache;

    private final Map<Tuple<PackageSize, Provider>, BigDecimal> priceCache;

    public ShippingPriceRepository() {
        this.lowestPriceCache = new ConcurrentHashMap<>();
        this.priceCache = new ConcurrentHashMap<>();
    }

    public BigDecimal findPrice(PackageSize packageSize, Provider provider) {
        return priceCache.computeIfAbsent(new Tuple<>(packageSize, provider), (key) ->
            prices
                .stream()
                .filter(it -> it.getPackageSize() == packageSize && it.getProvider() == provider)
                .map(ShippingPrice::getPrice)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                    String.format(
                        "Shipping price configuration malformed. Price for package size: %s, provider: %s, not found",
                        packageSize.getName(),
                        provider.getName())
                )));
    }

    public BigDecimal findLowestPrice(PackageSize packageSize) {
        return lowestPriceCache.computeIfAbsent(packageSize, (key) ->
            prices
                .stream()
                .filter(it -> packageSize == it.getPackageSize())
                .map(ShippingPrice::getPrice)
                .sorted()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                    "Shipping price configuration malformed. Price field must not be empty")));
    }
}

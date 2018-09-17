package vinted.delivery.repository;

import vinted.delivery.enums.PackageSize;
import vinted.delivery.enums.Provider;
import vinted.delivery.entities.ShippingPrice;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShippingPriceRepository implements PriceRepository {

    private static final List<ShippingPrice> prices = Arrays.asList(
        ShippingPrice.createEuropeanShippingPrice(Provider.LP, PackageSize.S, BigDecimal.valueOf(1.5)),
        ShippingPrice.createEuropeanShippingPrice(Provider.LP, PackageSize.M, BigDecimal.valueOf(4.9)),
        ShippingPrice.createEuropeanShippingPrice(Provider.LP, PackageSize.L, BigDecimal.valueOf(6.9)),
        ShippingPrice.createEuropeanShippingPrice(Provider.MR, PackageSize.S, BigDecimal.valueOf(2.0)),
        ShippingPrice.createEuropeanShippingPrice(Provider.MR, PackageSize.M, BigDecimal.valueOf(3.0)),
        ShippingPrice.createEuropeanShippingPrice(Provider.MR, PackageSize.L, BigDecimal.valueOf(4.0))
    );

    private Map<PackageSize, BigDecimal> lowestPriceCache;

    public ShippingPriceRepository() {
        this.lowestPriceCache = new ConcurrentHashMap<>();
    }

    public BigDecimal findPrice(PackageSize packageSize, Provider provider) {
        return prices
            .stream()
            .filter(shippingPrice -> shippingPrice.getPackageSize() == packageSize && shippingPrice.getProvider() == provider)
            .map(ShippingPrice::getPrice)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                String.format(
                    "Shipping price configuration malformed. Price for package size: %s, provider: %s, not found",
                    packageSize.getName(),
                    provider.getName())
                )
            );
    }

    public BigDecimal findLowestPrice(PackageSize packageSize) {
        return lowestPriceCache.computeIfAbsent(packageSize, (key) ->
            prices
                .stream()
                .filter(shippingPrice -> key == shippingPrice.getPackageSize())
                .map(ShippingPrice::getPrice)
                .sorted()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Shipping price configuration malformed. Price field must not be null")));
    }
}

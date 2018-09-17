package vinted.delivery.entities;


import vinted.delivery.enums.PackageSize;
import vinted.delivery.enums.Provider;

import java.math.BigDecimal;
import java.util.Currency;

public class ShippingPrice {

    private static final Currency euro = Currency.getInstance("EUR");

    private final Provider provider;

    private final PackageSize packageSize;

    private final BigDecimal price;

    private final Currency currency;

    public ShippingPrice(Provider provider, PackageSize packageSize, BigDecimal price, Currency currency) {
        this.provider = provider;
        this.packageSize = packageSize;
        this.price = price;
        this.currency = currency;
    }

    public static ShippingPrice createEuropeanShippingPrice(Provider provider, PackageSize packageSize, BigDecimal price) {
        return new ShippingPrice(provider, packageSize, price, euro);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PackageSize getPackageSize() {
        return packageSize;
    }

    public Provider getProvider() {
        return provider;
    }
}

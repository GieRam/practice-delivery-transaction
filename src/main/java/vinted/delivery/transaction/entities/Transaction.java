package vinted.delivery.transaction.entities;

import vinted.delivery.transaction.enums.PackageSize;
import vinted.delivery.transaction.enums.Provider;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private LocalDate date;

    private PackageSize packageSize;

    private Provider provider;

    private Bill bill;

    private final String origin;

    private boolean ignored;

    public Transaction(String origin) {
        this.origin = origin;
        this.ignored = true;
    }

    public Transaction(LocalDate date, PackageSize packageSize, Provider provider, String origin) {
        this.date = date;
        this.packageSize = packageSize;
        this.provider = provider;
        this.origin = origin;
        this.bill = new Bill();
    }

    public LocalDate getDate() {
        return date;
    }

    public PackageSize getPackageSize() {
        return packageSize;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setPrice(BigDecimal price) {
        getBill().setPrice(price);
    }

    public void setDiscount(BigDecimal discount) {
        getBill().setDiscount(discount);
    }

    public String getOrigin() {
        return origin;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public boolean isValid() {
        return !ignored;
    }

    public Bill getBill() {
        return bill;
    }
}

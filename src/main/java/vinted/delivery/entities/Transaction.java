package vinted.delivery.entities;

import vinted.delivery.enums.PackageSize;
import vinted.delivery.enums.Provider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Transaction {

    private LocalDate date;

    private PackageSize packageSize;

    private Provider provider;

    private Bill bill;

    private String origin;

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

    public Bill getBill() {
        if (Objects.isNull(bill)) {
            bill = new Bill();
            return bill;
        }
        return bill;
    }

    // TODO: move this to output responsible class
    @Override
    public String toString() {
        return ignored ?
            String.format("%s Ignored", origin)
            : String.format("%s %s %s", date.toString(), packageSize, provider);
    }
}

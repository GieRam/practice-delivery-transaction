package vinted.delivery.entities;


import java.math.BigDecimal;

public class Bill {

    private BigDecimal price;

    private BigDecimal discount;

    public Bill() {
        this.price = BigDecimal.ZERO;
        this.discount = BigDecimal.ZERO;
    }

    public Bill(BigDecimal price, BigDecimal discount) {
        this.price = price;
        this.discount = discount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getFinalPrice() {
        return price.subtract(discount);
    }
}

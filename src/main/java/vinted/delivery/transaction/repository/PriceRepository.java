package vinted.delivery.transaction.repository;

import vinted.delivery.transaction.enums.PackageSize;
import vinted.delivery.transaction.enums.Provider;

import java.math.BigDecimal;

public interface PriceRepository {

    BigDecimal findPrice(PackageSize packageSize, Provider provider);

    BigDecimal findLowestPrice(PackageSize packageSize);
}

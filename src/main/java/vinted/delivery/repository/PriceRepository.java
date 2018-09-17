package vinted.delivery.repository;

import vinted.delivery.enums.PackageSize;
import vinted.delivery.enums.Provider;

import java.math.BigDecimal;

public interface PriceRepository {

    BigDecimal findPrice(PackageSize packageSize, Provider provider);

    BigDecimal findLowestPrice(PackageSize packageSize);
}

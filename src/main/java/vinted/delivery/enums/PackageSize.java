package vinted.delivery.enums;

public enum PackageSize {

    S("Small"),
    M("Medium"),
    L("Large");

    private final String name;

    PackageSize(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

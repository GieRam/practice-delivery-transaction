package vinted.delivery.transaction.enums;

public enum Provider {

    LP("La Poste"),
    MR("Mondial Relay");

    private final String name;

    Provider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

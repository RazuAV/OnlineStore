package wantsome.online_store.db.products;

public enum ProductType {
    PC("Pc"),
    GAMING("Gaming"),
    BOOKS("Books"),
    PHONES("Phones"),
    FASHION("Fashion"),
    GARDEN("Garden"),
    HOUSE("House"),
    SPORT("Sport"),
    AUTO("Auto"),
    TOYS("Toys"),
    COSMETICS("Cosmetics");

    private String label;

    ProductType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

public enum buildings {

    METAL(1),
    CRYSTAL(2),
    DEUTER(3),
    SOLARPLANT(4),
    FUSIONREACTOR(5),
    SOLARSATELITE(6),
    CRAWLER(7),
    METALSTORE(8),
    CRYSTALSTORE(9),
    DEUTERSTORE(10);

    private final int value;


    buildings(int value) {
        this.value = value;
    }

    public int toInt() {
        return this.value;
    }
}

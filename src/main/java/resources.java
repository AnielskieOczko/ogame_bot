public enum resources {

    RESOURCES_METAL("resources_metal"),
    RESOURCES_CRYSTAL("resources_crystal"),
    RESOURCES_DEUTERIUM("resources_deuterium"),
    RESOURCES_ENERGY("resources_energy");

    private String value;
    resources(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

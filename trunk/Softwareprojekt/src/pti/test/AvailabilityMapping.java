package pti.test;

public class AvailabilityMapping {
    public int operation;
    public int machine;
    public int product;
    public int occupied;
    public long minutes;
    public int worker;

    public AvailabilityMapping(int operation, int machine, int product, int occupied, long minutes) {
        this.operation = operation;
        this.machine = machine;
        this.product = product;
        this.occupied = occupied;
        this.minutes = minutes;
    }
}

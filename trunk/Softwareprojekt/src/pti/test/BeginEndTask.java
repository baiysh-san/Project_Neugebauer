package pti.test;

public class BeginEndTask implements TaskType {
    public long start;
    public long end;
    public int product;
    public int operation;
    public int machine;

    public BeginEndTask(int product, int operation, int machine, long start, long end) {
        this.product = product;
        this.operation = operation;
        this.start = start;
        this.end = end;
        this.machine = machine;
    }

    @Override
    public String getType() {
        return "task";
    }
}

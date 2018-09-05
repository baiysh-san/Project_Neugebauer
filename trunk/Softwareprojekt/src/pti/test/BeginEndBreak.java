package pti.test;

public class BeginEndBreak implements TaskType {
    public long start;
    public long end;

    public BeginEndBreak(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String getType() {
        return "break";
    }
}

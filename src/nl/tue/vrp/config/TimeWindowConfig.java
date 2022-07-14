package nl.tue.vrp.config;

public class TimeWindowConfig {
    protected int start;
    protected int end;

    public TimeWindowConfig() {
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}

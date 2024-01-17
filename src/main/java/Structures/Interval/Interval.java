package Structures.Interval;

public class Interval {
    Double start;
    Double end;
    Integer count;

    public Interval(Double start, Double end, Integer count) {
        this.start = start;
        this.end = end;
        this.count = count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getStart() {
        return start;
    }

    public Double getEnd() {
        return end;
    }

    public Integer getCount() {
        return count;
    }
}

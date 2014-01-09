package org.albertyang2007.graphite.json;

public class GraphiteJsonDataPointVO {
    private String value;
    private String timestamp;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "[" + this.value + ", " + this.timestamp + "]";
    }
}

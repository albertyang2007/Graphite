package org.albertyang2007.graphite.json;

import java.util.ArrayList;
import java.util.List;

public class GraphiteJsonDataPointVO3 {
    private List<Number> datapoint = new ArrayList<Number>();

    public GraphiteJsonDataPointVO3(List<String> dps) {
        if (dps != null && dps.size() == 2) {
            if (dps.get(0) != null && dps.get(1) != null) {
                Number value = new Double(dps.get(0));
                Number timestamp = new Double(dps.get(1));
                datapoint.add(value);
                datapoint.add(timestamp);
            }
        }
    }

    public List<Number> getDatapoint() {
        return datapoint;
    }

    public void setDatapoint(List<Number> datapoint) {
        this.datapoint = datapoint;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (Number s : datapoint) {
            sb.append(s);
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }
}

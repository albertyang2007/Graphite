package org.albertyang2007.graphite.json;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
        
        Timestamp ts = new Timestamp(Long.parseLong(this.timestamp+"000"));
        
        String tsStr = "";  
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
        try {  
            tsStr = sdf.format(ts);  
            System.out.println(tsStr);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return "[" + this.value + ", " + tsStr + "]";
    }
}

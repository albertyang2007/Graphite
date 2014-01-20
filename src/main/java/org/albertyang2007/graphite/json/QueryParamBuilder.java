package org.albertyang2007.graphite.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class QueryParamBuilder {

    private List<HashMap<String, String>> params = new ArrayList<HashMap<String, String>>();

    public void putParam(String key, String value) {
        HashMap<String, String> parm = new HashMap<String, String>();
        parm.put(key, value);
        params.add(parm);
    }

    public List<HashMap<String, String>> getParams() {
        return this.params;
    }

    public String getRenderURL() {
        StringBuffer buff = new StringBuffer();
        for (HashMap<String, String> mp : params) {

            Iterator<String> keys = mp.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = mp.get(key);
                buff.append(key);
                buff.append("=");
                buff.append(value);
                buff.append("&");
            }
        }
        return buff.toString();
    }

    public static void main(String[] args) {
        QueryParamBuilder b = new QueryParamBuilder();
        b.putParam("target", "v1");
        b.putParam("target", "v2");
        System.out.println(b.getRenderURL());
    }
}

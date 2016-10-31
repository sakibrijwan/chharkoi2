package com.chharkoi.app.domain.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static String toJson(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(o);
        } catch (Exception e) {
            log.warn("JSON error.", e);
            // ignore
        }
        return json;
    }

    public static Object fromJson(String json, Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
        Object o = null;
        try {
            o = mapper.readValue(json, clazz);
        } catch (Exception e) {
            // ignore
        }
        return o;
    }

    public static String join(String [] a) {
        return String.join(",", a);
    }

    public static Object [] initializeArray(Object [] a, Object v) {
        for(int i=0; i<a.length; i++) {
            a[i] = v;
        }
        return a;
    }
}

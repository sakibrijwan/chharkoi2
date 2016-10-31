package com.chharkoi.app.datamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class QueryHelper {
    private static final Logger log = LoggerFactory.getLogger(QueryHelper.class);
    private StringBuffer sb;
    private List<Object> params;

    public QueryHelper() {
        this.sb = new StringBuffer();
        this.params = new ArrayList<Object>();
    }

    public void append(String s, Object... params) {
        this.sb.append(s + " ");
        this.params.addAll(Arrays.asList(params));
    }

    public void append(Object[] values) {
        append(Arrays.asList(values));
    }

    public void append(List values) {
        boolean first = true;
        for (Object value : values) {
            if (first) {
                this.append("?", value);
                first = false;
            } else {
                this.append(",?", value);
            }
        }
    }

    public Query createQuery(EntityManager em) {
        Query q = em.createQuery(this.sb.toString());
        int pos = 1;
        for (Object param : this.params) {
            q.setParameter(pos, param);
            pos++;
        }
        return q;
    }

    public Object convertParamsForNativeQuery(Object param) {
        if (param instanceof Enum) {
            return String.valueOf(param);
        }
        return param;
    }


    public Query createNativeQuery(EntityManager em, Class clazz) {
        Query q;
        if (clazz == null) {
            q = em.createNativeQuery(this.sb.toString());
        } else {
            q = em.createNativeQuery(this.sb.toString(), clazz);
        }
        int pos = 1;
        for (Object param : this.params) {
            q.setParameter(pos, convertParamsForNativeQuery(param));
            pos++;
        }
        return q;
    }

    public Query createNativeQuery(EntityManager em) {
        return createNativeQuery(em, null);
    }

    public Object getOne(EntityManager em, Object defaultValue) {
        Query q = createQuery(em);
        Object r = defaultValue;
        try {
            r = q.getSingleResult();
        } catch (Exception ex) {
            // ignore
        }
        return r == null ? defaultValue : r;
    }

    public Object getOne(EntityManager em) {
        return getOne(em, null);
    }

    public long getOneAsLong(EntityManager em, long defaultValue) {
        return (long) getOne(em, defaultValue);
    }

    public long getOneAsLong(EntityManager em) {
        return getOneAsLong(em, 0L);
    }

    public int getOneAsInteger(EntityManager em, int defaultValue) {
        return (int) getOne(em, defaultValue);
    }

    public int getOneAsInteger(EntityManager em) {
        return getOneAsInteger(em, 0);
    }

    public Date getOneAsDate(EntityManager em) {
        return getOneAsDate(em, null);
    }

    public Date getOneAsDate(EntityManager em, Date defaultValue) {
        return (Date) getOne(em, defaultValue);
    }

    public void logQuery(String logMessage) {
        log.debug(logMessage + ": " + this.sb.toString());
        int pos = 1;
        for (Object param : this.params) {
            log.debug("Parameter " + pos + ": " + param);
            pos++;
        }
    }

    @Override
    public String toString() {
        return this.sb.toString();
    }
}

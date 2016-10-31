package com.chharkoi.app.datamodel;



import javax.persistence.EntityManager;
import java.util.Date;

public class DMUpgrade0 implements DMUpgrade {
    @Override
    public void execute(DMManager dm) {
        insertAuthorities(dm.getEntityManager());
        insertUsers(dm.getEntityManager());
        insertUserAuthorities(dm.getEntityManager());
    }

    public void insertAuthorities(EntityManager em) {
        insertAuthority(em, "ROLE_ADMIN");
        insertAuthority(em, "ROLE_USER");
    }

    public void insertAuthority(EntityManager em, String authority) {
        QueryHelper qh = new QueryHelper();
        qh.append("INSERT INTO jhi_authority (name) VALUES(?)", authority);
        qh.createNativeQuery(em).executeUpdate();
    }

    public void insertUsers(EntityManager em) {
        Date now = new Date();
        insertUser(em, 1, "system", "$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG", "System", "System", "system@localhost", true, "en", "system", now);
        insertUser(em, 2, "anonymoususer", "$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO", "Anonymous", "User", "anonymous@localhost", true, "en", "system", now);
        insertUser(em, 3, "admin", "$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC", "Administrator", "Administrator", "admin@localhost", true, "en", "system", now);
        insertUser(em, 4, "user", "$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K", "User", "User", "user@localhost", true, "en", "system", now);
    }

    public void insertUser(EntityManager em, Object ... params) {
        QueryHelper qh = new QueryHelper();
        qh.append("INSERT INTO jhi_user (id,login,password_hash,first_name,last_name,email,activated,lang_key,created_by,created_date) VALUES(");
        qh.append(params);
        qh.append(")");
        qh.createNativeQuery(em).executeUpdate();
    }

    public void insertUserAuthorities(EntityManager em) {
        insertUserAuthority(em, 1, "ROLE_ADMIN");
        insertUserAuthority(em, 1, "ROLE_USER");
        insertUserAuthority(em, 3, "ROLE_ADMIN");
        insertUserAuthority(em, 3, "ROLE_USER");
        insertUserAuthority(em, 4, "ROLE_USER");
    }

    public void insertUserAuthority(EntityManager em, long userId, String authority) {
        QueryHelper qh = new QueryHelper();
        qh.append("INSERT INTO jhi_user_authority (user_id, authority_name) VALUES(?, ?)", userId, authority);
        qh.createNativeQuery(em).executeUpdate();
    }

}

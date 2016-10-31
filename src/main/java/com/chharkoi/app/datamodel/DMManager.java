package com.chharkoi.app.datamodel;


import com.chharkoi.app.domain.util.AppSetting;
import com.chharkoi.app.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class DMManager {
    private static final Logger log = LoggerFactory.getLogger(DMManager.class);

    @Inject
    private SettingService settingService;

    @PersistenceContext
    private EntityManager entityManager;

    private DMUpgrade[] upgrades = new DMUpgrade[] {
        new DMUpgrade0()
    };

    @Transactional
    public void run() {
        int v = getDmVersion();
        while (v < upgrades.length) {
            log.info("Data model upgrade " + v + " running...");
            upgrades[v].execute(this);
            log.info("Data model upgrade " + v + " complete.");
            v++;
            setDmVersion(v);
        }
    }

    public int getDmVersion() {
        return (Integer) AppSetting.DM_VERSION.getValue(settingService);
    }

    public void setDmVersion(int version) {
        AppSetting.DM_VERSION.saveValue(settingService, version);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}

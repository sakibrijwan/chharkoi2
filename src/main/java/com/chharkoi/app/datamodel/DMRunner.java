package com.chharkoi.app.datamodel;

import com.chharkoi.app.datamodel.DMManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DMRunner {
    @Autowired
    private DMManager dmManager;

    @PostConstruct
    public void run() {
        dmManager.run();
    }
}

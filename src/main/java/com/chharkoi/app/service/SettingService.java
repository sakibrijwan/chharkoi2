package com.chharkoi.app.service;

import com.chharkoi.app.domain.Setting;
import com.chharkoi.app.repository.SettingRepository;
import com.chharkoi.app.repository.search.SettingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Setting.
 */
@Service
@Transactional
public class SettingService {

    private final Logger log = LoggerFactory.getLogger(SettingService.class);

    @Inject
    private SettingRepository settingRepository;

    @Inject
    private SettingSearchRepository settingSearchRepository;

    /**
     * Save a setting.
     *
     * @param setting the entity to save
     * @return the persisted entity
     */
    public Setting save(Setting setting) {
        log.debug("Request to save Setting : {}", setting);
        Setting result = settingRepository.save(setting);
        settingSearchRepository.save(result);
        return result;
    }


    public Setting findOneByName(String name) {
        return settingRepository.findOneByName(name);
    }

    /**
     *  Get all the settings.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Setting> findAll() {
        log.debug("Request to get all Settings");
        List<Setting> result = settingRepository.findAll();

        return result;
    }

    /**
     *  Get one setting by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Setting findOne(Long id) {
        log.debug("Request to get Setting : {}", id);
        Setting setting = settingRepository.findOne(id);
        return setting;
    }

    /**
     *  Delete the  setting by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Setting : {}", id);
        settingRepository.delete(id);
        settingSearchRepository.delete(id);
    }

    /**
     * Search for the setting corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Setting> search(String query) {
        log.debug("Request to search Settings for query {}", query);
        return StreamSupport
            .stream(settingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public Setting save(String name, String value) {
        Setting setting = findOneByName(name);
        if (setting == null) {
            setting = new Setting();
            setting.setName(name);
        }
        setting.setValue(value);
        Setting result = settingRepository.save(setting);
        return result;
    }

}

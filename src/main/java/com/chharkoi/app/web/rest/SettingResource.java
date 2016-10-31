package com.chharkoi.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.chharkoi.app.domain.Setting;
import com.chharkoi.app.service.SettingService;
import com.chharkoi.app.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Setting.
 */
@RestController
@RequestMapping("/api")
public class SettingResource {

    private final Logger log = LoggerFactory.getLogger(SettingResource.class);
        
    @Inject
    private SettingService settingService;

    /**
     * POST  /settings : Create a new setting.
     *
     * @param setting the setting to create
     * @return the ResponseEntity with status 201 (Created) and with body the new setting, or with status 400 (Bad Request) if the setting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/settings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Setting> createSetting(@RequestBody Setting setting) throws URISyntaxException {
        log.debug("REST request to save Setting : {}", setting);
        if (setting.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("setting", "idexists", "A new setting cannot already have an ID")).body(null);
        }
        Setting result = settingService.save(setting);
        return ResponseEntity.created(new URI("/api/settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("setting", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /settings : Updates an existing setting.
     *
     * @param setting the setting to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated setting,
     * or with status 400 (Bad Request) if the setting is not valid,
     * or with status 500 (Internal Server Error) if the setting couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/settings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Setting> updateSetting(@RequestBody Setting setting) throws URISyntaxException {
        log.debug("REST request to update Setting : {}", setting);
        if (setting.getId() == null) {
            return createSetting(setting);
        }
        Setting result = settingService.save(setting);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("setting", setting.getId().toString()))
            .body(result);
    }

    /**
     * GET  /settings : get all the settings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of settings in body
     */
    @RequestMapping(value = "/settings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Setting> getAllSettings() {
        log.debug("REST request to get all Settings");
        return settingService.findAll();
    }

    /**
     * GET  /settings/:id : get the "id" setting.
     *
     * @param id the id of the setting to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the setting, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/settings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Setting> getSetting(@PathVariable Long id) {
        log.debug("REST request to get Setting : {}", id);
        Setting setting = settingService.findOne(id);
        return Optional.ofNullable(setting)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /settings/:id : delete the "id" setting.
     *
     * @param id the id of the setting to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/settings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSetting(@PathVariable Long id) {
        log.debug("REST request to delete Setting : {}", id);
        settingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("setting", id.toString())).build();
    }

    /**
     * SEARCH  /_search/settings?query=:query : search for the setting corresponding
     * to the query.
     *
     * @param query the query of the setting search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/settings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Setting> searchSettings(@RequestParam String query) {
        log.debug("REST request to search Settings for query {}", query);
        return settingService.search(query);
    }


}

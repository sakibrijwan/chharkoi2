package com.chharkoi.app.web.rest;

import com.chharkoi.app.domain.User;
import com.chharkoi.app.repository.UserRepository;
import com.chharkoi.app.security.SecurityUtils;
import com.codahale.metrics.annotation.Timed;
import com.chharkoi.app.domain.Shopkeeper;

import com.chharkoi.app.repository.ShopkeeperRepository;
import com.chharkoi.app.repository.search.ShopkeeperSearchRepository;
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
 * REST controller for managing Shopkeeper.
 */
@RestController
@RequestMapping("/api")
public class ShopkeeperResource {

    private final Logger log = LoggerFactory.getLogger(ShopkeeperResource.class);

    @Inject
    private ShopkeeperRepository shopkeeperRepository;

    @Inject
    private ShopkeeperSearchRepository shopkeeperSearchRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /shopkeepers : Create a new shopkeeper.
     *
     * @param shopkeeper the shopkeeper to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shopkeeper, or with status 400 (Bad Request) if the shopkeeper has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shopkeepers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shopkeeper> createShopkeeper(@RequestBody Shopkeeper shopkeeper) throws URISyntaxException {
        log.debug("REST request to save Shopkeeper : {}", shopkeeper);
        if (shopkeeper.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shopkeeper", "idexists", "A new shopkeeper cannot already have an ID")).body(null);
        }
        shopkeeper.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        Shopkeeper result = shopkeeperRepository.save(shopkeeper);
        shopkeeperSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/shopkeepers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shopkeeper", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shopkeepers : Updates an existing shopkeeper.
     *
     * @param shopkeeper the shopkeeper to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shopkeeper,
     * or with status 400 (Bad Request) if the shopkeeper is not valid,
     * or with status 500 (Internal Server Error) if the shopkeeper couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shopkeepers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shopkeeper> updateShopkeeper(@RequestBody Shopkeeper shopkeeper) throws URISyntaxException {
        log.debug("REST request to update Shopkeeper : {}", shopkeeper);
        if (shopkeeper.getId() == null) {
            return createShopkeeper(shopkeeper);
        }
        Shopkeeper result = shopkeeperRepository.save(shopkeeper);
        shopkeeperSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shopkeeper", shopkeeper.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shopkeepers : get all the shopkeepers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shopkeepers in body
     */
    @RequestMapping(value = "/shopkeepers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Shopkeeper> getAllShopkeepers() {
        log.debug("REST request to get all Shopkeepers");
        List<Shopkeeper> shopkeepers = shopkeeperRepository.findAll();
        return shopkeepers;
    }

    /**
     * GET  /shopkeepers/:id : get the "id" shopkeeper.
     *
     * @param id the id of the shopkeeper to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shopkeeper, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/shopkeepers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shopkeeper> getShopkeeper(@PathVariable Long id) {
        log.debug("REST request to get Shopkeeper : {}", id);
        Shopkeeper shopkeeper = shopkeeperRepository.findOne(id);
        return Optional.ofNullable(shopkeeper)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shopkeepers/:id : delete the "id" shopkeeper.
     *
     * @param id the id of the shopkeeper to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/shopkeepers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShopkeeper(@PathVariable Long id) {
        log.debug("REST request to delete Shopkeeper : {}", id);
        shopkeeperRepository.delete(id);
        shopkeeperSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shopkeeper", id.toString())).build();
    }

    /**
     * SEARCH  /_search/shopkeepers?query=:query : search for the shopkeeper corresponding
     * to the query.
     *
     * @param query the query of the shopkeeper search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/shopkeepers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Shopkeeper> searchShopkeepers(@RequestParam String query) {
        log.debug("REST request to search Shopkeepers for query {}", query);
        return StreamSupport
            .stream(shopkeeperSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * GET  /shopkeepers/:id : get the "id" shopkeeper.
     *
     * @param id the id of the shopkeeper to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shopkeeper, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/shopkeeper-self",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shopkeeper> getShopkeeperSelf() {
        log.debug("REST request to get Shopkeeper : {}");
        Shopkeeper shopkeeper = shopkeeperRepository.findByUserIsCurrentUser();
        return Optional.ofNullable(shopkeeper)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}

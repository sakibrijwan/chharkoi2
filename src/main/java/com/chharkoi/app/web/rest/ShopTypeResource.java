package com.chharkoi.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.chharkoi.app.domain.ShopType;

import com.chharkoi.app.repository.ShopTypeRepository;
import com.chharkoi.app.repository.search.ShopTypeSearchRepository;
import com.chharkoi.app.web.rest.util.HeaderUtil;
import com.chharkoi.app.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ShopType.
 */
@RestController
@RequestMapping("/api")
public class ShopTypeResource {

    private final Logger log = LoggerFactory.getLogger(ShopTypeResource.class);
        
    @Inject
    private ShopTypeRepository shopTypeRepository;

    @Inject
    private ShopTypeSearchRepository shopTypeSearchRepository;

    /**
     * POST  /shop-types : Create a new shopType.
     *
     * @param shopType the shopType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shopType, or with status 400 (Bad Request) if the shopType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shop-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShopType> createShopType(@Valid @RequestBody ShopType shopType) throws URISyntaxException {
        log.debug("REST request to save ShopType : {}", shopType);
        if (shopType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shopType", "idexists", "A new shopType cannot already have an ID")).body(null);
        }
        ShopType result = shopTypeRepository.save(shopType);
        shopTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/shop-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shopType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shop-types : Updates an existing shopType.
     *
     * @param shopType the shopType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shopType,
     * or with status 400 (Bad Request) if the shopType is not valid,
     * or with status 500 (Internal Server Error) if the shopType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shop-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShopType> updateShopType(@Valid @RequestBody ShopType shopType) throws URISyntaxException {
        log.debug("REST request to update ShopType : {}", shopType);
        if (shopType.getId() == null) {
            return createShopType(shopType);
        }
        ShopType result = shopTypeRepository.save(shopType);
        shopTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shopType", shopType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shop-types : get all the shopTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of shopTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/shop-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ShopType>> getAllShopTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ShopTypes");
        Page<ShopType> page = shopTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shop-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /shop-types/:id : get the "id" shopType.
     *
     * @param id the id of the shopType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shopType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/shop-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShopType> getShopType(@PathVariable Long id) {
        log.debug("REST request to get ShopType : {}", id);
        ShopType shopType = shopTypeRepository.findOne(id);
        return Optional.ofNullable(shopType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shop-types/:id : delete the "id" shopType.
     *
     * @param id the id of the shopType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/shop-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShopType(@PathVariable Long id) {
        log.debug("REST request to delete ShopType : {}", id);
        shopTypeRepository.delete(id);
        shopTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shopType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/shop-types?query=:query : search for the shopType corresponding
     * to the query.
     *
     * @param query the query of the shopType search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/shop-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ShopType>> searchShopTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ShopTypes for query {}", query);
        Page<ShopType> page = shopTypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/shop-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

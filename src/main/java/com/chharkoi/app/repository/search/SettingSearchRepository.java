package com.chharkoi.app.repository.search;

import com.chharkoi.app.domain.Setting;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Setting entity.
 */
public interface SettingSearchRepository extends ElasticsearchRepository<Setting, Long> {
}

package com.chharkoi.app.web.rest;

import com.chharkoi.app.ChharkoiApp;

import com.chharkoi.app.domain.Setting;
import com.chharkoi.app.repository.SettingRepository;
import com.chharkoi.app.service.SettingService;
import com.chharkoi.app.repository.search.SettingSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SettingResource REST controller.
 *
 * @see SettingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChharkoiApp.class)
public class SettingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private SettingRepository settingRepository;

    @Inject
    private SettingService settingService;

    @Inject
    private SettingSearchRepository settingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSettingMockMvc;

    private Setting setting;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SettingResource settingResource = new SettingResource();
        ReflectionTestUtils.setField(settingResource, "settingService", settingService);
        this.restSettingMockMvc = MockMvcBuilders.standaloneSetup(settingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Setting createEntity(EntityManager em) {
        Setting setting = new Setting()
                .name(DEFAULT_NAME)
                .value(DEFAULT_VALUE);
        return setting;
    }

    @Before
    public void initTest() {
        settingSearchRepository.deleteAll();
        setting = createEntity(em);
    }

    @Test
    @Transactional
    public void createSetting() throws Exception {
        int databaseSizeBeforeCreate = settingRepository.findAll().size();

        // Create the Setting

        restSettingMockMvc.perform(post("/api/settings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(setting)))
                .andExpect(status().isCreated());

        // Validate the Setting in the database
        List<Setting> settings = settingRepository.findAll();
        assertThat(settings).hasSize(databaseSizeBeforeCreate + 1);
        Setting testSetting = settings.get(settings.size() - 1);
        assertThat(testSetting.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSetting.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the Setting in ElasticSearch
        Setting settingEs = settingSearchRepository.findOne(testSetting.getId());
        assertThat(settingEs).isEqualToComparingFieldByField(testSetting);
    }

    @Test
    @Transactional
    public void getAllSettings() throws Exception {
        // Initialize the database
        settingRepository.saveAndFlush(setting);

        // Get all the settings
        restSettingMockMvc.perform(get("/api/settings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(setting.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getSetting() throws Exception {
        // Initialize the database
        settingRepository.saveAndFlush(setting);

        // Get the setting
        restSettingMockMvc.perform(get("/api/settings/{id}", setting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(setting.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSetting() throws Exception {
        // Get the setting
        restSettingMockMvc.perform(get("/api/settings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSetting() throws Exception {
        // Initialize the database
        settingService.save(setting);

        int databaseSizeBeforeUpdate = settingRepository.findAll().size();

        // Update the setting
        Setting updatedSetting = settingRepository.findOne(setting.getId());
        updatedSetting
                .name(UPDATED_NAME)
                .value(UPDATED_VALUE);

        restSettingMockMvc.perform(put("/api/settings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSetting)))
                .andExpect(status().isOk());

        // Validate the Setting in the database
        List<Setting> settings = settingRepository.findAll();
        assertThat(settings).hasSize(databaseSizeBeforeUpdate);
        Setting testSetting = settings.get(settings.size() - 1);
        assertThat(testSetting.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSetting.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the Setting in ElasticSearch
        Setting settingEs = settingSearchRepository.findOne(testSetting.getId());
        assertThat(settingEs).isEqualToComparingFieldByField(testSetting);
    }

    @Test
    @Transactional
    public void deleteSetting() throws Exception {
        // Initialize the database
        settingService.save(setting);

        int databaseSizeBeforeDelete = settingRepository.findAll().size();

        // Get the setting
        restSettingMockMvc.perform(delete("/api/settings/{id}", setting.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean settingExistsInEs = settingSearchRepository.exists(setting.getId());
        assertThat(settingExistsInEs).isFalse();

        // Validate the database is empty
        List<Setting> settings = settingRepository.findAll();
        assertThat(settings).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSetting() throws Exception {
        // Initialize the database
        settingService.save(setting);

        // Search the setting
        restSettingMockMvc.perform(get("/api/_search/settings?query=id:" + setting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(setting.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }
}

package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.Telephone;
import com.emmaprojects.creativehub.repository.TelephoneRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TelephoneResource REST controller.
 *
 * @see TelephoneResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TelephoneResourceTest {


    private static final Integer DEFAULT_AREA_CODE = 1;
    private static final Integer UPDATED_AREA_CODE = 2;

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    @Inject
    private TelephoneRepository telephoneRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTelephoneMockMvc;

    private Telephone telephone;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TelephoneResource telephoneResource = new TelephoneResource();
        ReflectionTestUtils.setField(telephoneResource, "telephoneRepository", telephoneRepository);
        this.restTelephoneMockMvc = MockMvcBuilders.standaloneSetup(telephoneResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        telephone = new Telephone();
        telephone.setAreaCode(DEFAULT_AREA_CODE);
        telephone.setNumber(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    public void createTelephone() throws Exception {
        int databaseSizeBeforeCreate = telephoneRepository.findAll().size();

        // Create the Telephone

        restTelephoneMockMvc.perform(post("/api/telephones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telephone)))
                .andExpect(status().isCreated());

        // Validate the Telephone in the database
        List<Telephone> telephones = telephoneRepository.findAll();
        assertThat(telephones).hasSize(databaseSizeBeforeCreate + 1);
        Telephone testTelephone = telephones.get(telephones.size() - 1);
        assertThat(testTelephone.getAreaCode()).isEqualTo(DEFAULT_AREA_CODE);
        assertThat(testTelephone.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTelephones() throws Exception {
        // Initialize the database
        telephoneRepository.saveAndFlush(telephone);

        // Get all the telephones
        restTelephoneMockMvc.perform(get("/api/telephones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(telephone.getId().intValue())))
                .andExpect(jsonPath("$.[*].areaCode").value(hasItem(DEFAULT_AREA_CODE)))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @Test
    @Transactional
    public void getTelephone() throws Exception {
        // Initialize the database
        telephoneRepository.saveAndFlush(telephone);

        // Get the telephone
        restTelephoneMockMvc.perform(get("/api/telephones/{id}", telephone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(telephone.getId().intValue()))
            .andExpect(jsonPath("$.areaCode").value(DEFAULT_AREA_CODE))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingTelephone() throws Exception {
        // Get the telephone
        restTelephoneMockMvc.perform(get("/api/telephones/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelephone() throws Exception {
        // Initialize the database
        telephoneRepository.saveAndFlush(telephone);

		int databaseSizeBeforeUpdate = telephoneRepository.findAll().size();

        // Update the telephone
        telephone.setAreaCode(UPDATED_AREA_CODE);
        telephone.setNumber(UPDATED_NUMBER);

        restTelephoneMockMvc.perform(put("/api/telephones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telephone)))
                .andExpect(status().isOk());

        // Validate the Telephone in the database
        List<Telephone> telephones = telephoneRepository.findAll();
        assertThat(telephones).hasSize(databaseSizeBeforeUpdate);
        Telephone testTelephone = telephones.get(telephones.size() - 1);
        assertThat(testTelephone.getAreaCode()).isEqualTo(UPDATED_AREA_CODE);
        assertThat(testTelephone.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void deleteTelephone() throws Exception {
        // Initialize the database
        telephoneRepository.saveAndFlush(telephone);

		int databaseSizeBeforeDelete = telephoneRepository.findAll().size();

        // Get the telephone
        restTelephoneMockMvc.perform(delete("/api/telephones/{id}", telephone.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Telephone> telephones = telephoneRepository.findAll();
        assertThat(telephones).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.Award;
import com.emmaprojects.creativehub.repository.AwardRepository;

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
 * Test class for the AwardResource REST controller.
 *
 * @see AwardResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AwardResourceTest {


    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ISSUER = "AAAAA";
    private static final String UPDATED_ISSUER = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private AwardRepository awardRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAwardMockMvc;

    private Award award;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AwardResource awardResource = new AwardResource();
        ReflectionTestUtils.setField(awardResource, "awardRepository", awardRepository);
        this.restAwardMockMvc = MockMvcBuilders.standaloneSetup(awardResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        award = new Award();
        award.setYear(DEFAULT_YEAR);
        award.setName(DEFAULT_NAME);
        award.setIssuer(DEFAULT_ISSUER);
        award.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createAward() throws Exception {
        int databaseSizeBeforeCreate = awardRepository.findAll().size();

        // Create the Award

        restAwardMockMvc.perform(post("/api/awards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(award)))
                .andExpect(status().isCreated());

        // Validate the Award in the database
        List<Award> awards = awardRepository.findAll();
        assertThat(awards).hasSize(databaseSizeBeforeCreate + 1);
        Award testAward = awards.get(awards.size() - 1);
        assertThat(testAward.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testAward.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAward.getIssuer()).isEqualTo(DEFAULT_ISSUER);
        assertThat(testAward.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAwards() throws Exception {
        // Initialize the database
        awardRepository.saveAndFlush(award);

        // Get all the awards
        restAwardMockMvc.perform(get("/api/awards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(award.getId().intValue())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].issuer").value(hasItem(DEFAULT_ISSUER.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getAward() throws Exception {
        // Initialize the database
        awardRepository.saveAndFlush(award);

        // Get the award
        restAwardMockMvc.perform(get("/api/awards/{id}", award.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(award.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.issuer").value(DEFAULT_ISSUER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAward() throws Exception {
        // Get the award
        restAwardMockMvc.perform(get("/api/awards/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAward() throws Exception {
        // Initialize the database
        awardRepository.saveAndFlush(award);

		int databaseSizeBeforeUpdate = awardRepository.findAll().size();

        // Update the award
        award.setYear(UPDATED_YEAR);
        award.setName(UPDATED_NAME);
        award.setIssuer(UPDATED_ISSUER);
        award.setDescription(UPDATED_DESCRIPTION);

        restAwardMockMvc.perform(put("/api/awards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(award)))
                .andExpect(status().isOk());

        // Validate the Award in the database
        List<Award> awards = awardRepository.findAll();
        assertThat(awards).hasSize(databaseSizeBeforeUpdate);
        Award testAward = awards.get(awards.size() - 1);
        assertThat(testAward.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testAward.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAward.getIssuer()).isEqualTo(UPDATED_ISSUER);
        assertThat(testAward.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteAward() throws Exception {
        // Initialize the database
        awardRepository.saveAndFlush(award);

		int databaseSizeBeforeDelete = awardRepository.findAll().size();

        // Get the award
        restAwardMockMvc.perform(delete("/api/awards/{id}", award.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Award> awards = awardRepository.findAll();
        assertThat(awards).hasSize(databaseSizeBeforeDelete - 1);
    }
}

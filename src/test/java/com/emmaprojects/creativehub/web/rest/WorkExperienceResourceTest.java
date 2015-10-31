package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.WorkExperience;
import com.emmaprojects.creativehub.repository.WorkExperienceRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the WorkExperienceResource REST controller.
 *
 * @see WorkExperienceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WorkExperienceResourceTest {


    private static final LocalDate DEFAULT_STARTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STARTING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FINISHING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FINISHING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_ORGANIZATION = "AAAAA";
    private static final String UPDATED_ORGANIZATION = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    private static final Boolean DEFAULT_CURRENT = false;
    private static final Boolean UPDATED_CURRENT = true;
    private static final String DEFAULT_JOB_DESCRIPTION = "AAAAA";
    private static final String UPDATED_JOB_DESCRIPTION = "BBBBB";

    @Inject
    private WorkExperienceRepository workExperienceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkExperienceMockMvc;

    private WorkExperience workExperience;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkExperienceResource workExperienceResource = new WorkExperienceResource();
        ReflectionTestUtils.setField(workExperienceResource, "workExperienceRepository", workExperienceRepository);
        this.restWorkExperienceMockMvc = MockMvcBuilders.standaloneSetup(workExperienceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workExperience = new WorkExperience();
        workExperience.setStartingDate(DEFAULT_STARTING_DATE);
        workExperience.setFinishingDate(DEFAULT_FINISHING_DATE);
        workExperience.setOrganization(DEFAULT_ORGANIZATION);
        workExperience.setTitle(DEFAULT_TITLE);
        workExperience.setLocation(DEFAULT_LOCATION);
        workExperience.setCurrent(DEFAULT_CURRENT);
        workExperience.setJobDescription(DEFAULT_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createWorkExperience() throws Exception {
        int databaseSizeBeforeCreate = workExperienceRepository.findAll().size();

        // Create the WorkExperience

        restWorkExperienceMockMvc.perform(post("/api/workExperiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workExperience)))
                .andExpect(status().isCreated());

        // Validate the WorkExperience in the database
        List<WorkExperience> workExperiences = workExperienceRepository.findAll();
        assertThat(workExperiences).hasSize(databaseSizeBeforeCreate + 1);
        WorkExperience testWorkExperience = workExperiences.get(workExperiences.size() - 1);
        assertThat(testWorkExperience.getStartingDate()).isEqualTo(DEFAULT_STARTING_DATE);
        assertThat(testWorkExperience.getFinishingDate()).isEqualTo(DEFAULT_FINISHING_DATE);
        assertThat(testWorkExperience.getOrganization()).isEqualTo(DEFAULT_ORGANIZATION);
        assertThat(testWorkExperience.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkExperience.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testWorkExperience.getCurrent()).isEqualTo(DEFAULT_CURRENT);
        assertThat(testWorkExperience.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllWorkExperiences() throws Exception {
        // Initialize the database
        workExperienceRepository.saveAndFlush(workExperience);

        // Get all the workExperiences
        restWorkExperienceMockMvc.perform(get("/api/workExperiences"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workExperience.getId().intValue())))
                .andExpect(jsonPath("$.[*].startingDate").value(hasItem(DEFAULT_STARTING_DATE.toString())))
                .andExpect(jsonPath("$.[*].finishingDate").value(hasItem(DEFAULT_FINISHING_DATE.toString())))
                .andExpect(jsonPath("$.[*].organization").value(hasItem(DEFAULT_ORGANIZATION.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].current").value(hasItem(DEFAULT_CURRENT.booleanValue())))
                .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getWorkExperience() throws Exception {
        // Initialize the database
        workExperienceRepository.saveAndFlush(workExperience);

        // Get the workExperience
        restWorkExperienceMockMvc.perform(get("/api/workExperiences/{id}", workExperience.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workExperience.getId().intValue()))
            .andExpect(jsonPath("$.startingDate").value(DEFAULT_STARTING_DATE.toString()))
            .andExpect(jsonPath("$.finishingDate").value(DEFAULT_FINISHING_DATE.toString()))
            .andExpect(jsonPath("$.organization").value(DEFAULT_ORGANIZATION.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.current").value(DEFAULT_CURRENT.booleanValue()))
            .andExpect(jsonPath("$.jobDescription").value(DEFAULT_JOB_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkExperience() throws Exception {
        // Get the workExperience
        restWorkExperienceMockMvc.perform(get("/api/workExperiences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkExperience() throws Exception {
        // Initialize the database
        workExperienceRepository.saveAndFlush(workExperience);

		int databaseSizeBeforeUpdate = workExperienceRepository.findAll().size();

        // Update the workExperience
        workExperience.setStartingDate(UPDATED_STARTING_DATE);
        workExperience.setFinishingDate(UPDATED_FINISHING_DATE);
        workExperience.setOrganization(UPDATED_ORGANIZATION);
        workExperience.setTitle(UPDATED_TITLE);
        workExperience.setLocation(UPDATED_LOCATION);
        workExperience.setCurrent(UPDATED_CURRENT);
        workExperience.setJobDescription(UPDATED_JOB_DESCRIPTION);

        restWorkExperienceMockMvc.perform(put("/api/workExperiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workExperience)))
                .andExpect(status().isOk());

        // Validate the WorkExperience in the database
        List<WorkExperience> workExperiences = workExperienceRepository.findAll();
        assertThat(workExperiences).hasSize(databaseSizeBeforeUpdate);
        WorkExperience testWorkExperience = workExperiences.get(workExperiences.size() - 1);
        assertThat(testWorkExperience.getStartingDate()).isEqualTo(UPDATED_STARTING_DATE);
        assertThat(testWorkExperience.getFinishingDate()).isEqualTo(UPDATED_FINISHING_DATE);
        assertThat(testWorkExperience.getOrganization()).isEqualTo(UPDATED_ORGANIZATION);
        assertThat(testWorkExperience.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkExperience.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testWorkExperience.getCurrent()).isEqualTo(UPDATED_CURRENT);
        assertThat(testWorkExperience.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteWorkExperience() throws Exception {
        // Initialize the database
        workExperienceRepository.saveAndFlush(workExperience);

		int databaseSizeBeforeDelete = workExperienceRepository.findAll().size();

        // Get the workExperience
        restWorkExperienceMockMvc.perform(delete("/api/workExperiences/{id}", workExperience.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkExperience> workExperiences = workExperienceRepository.findAll();
        assertThat(workExperiences).hasSize(databaseSizeBeforeDelete - 1);
    }
}

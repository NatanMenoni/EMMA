package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.WorkCollection;
import com.emmaprojects.creativehub.repository.WorkCollectionRepository;

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
 * Test class for the WorkCollectionResource REST controller.
 *
 * @see WorkCollectionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WorkCollectionResourceTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_COVER_IMAGE_PATH = "AAAAA";
    private static final String UPDATED_COVER_IMAGE_PATH = "BBBBB";

    @Inject
    private WorkCollectionRepository workCollectionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkCollectionMockMvc;

    private WorkCollection workCollection;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkCollectionResource workCollectionResource = new WorkCollectionResource();
        ReflectionTestUtils.setField(workCollectionResource, "workCollectionRepository", workCollectionRepository);
        this.restWorkCollectionMockMvc = MockMvcBuilders.standaloneSetup(workCollectionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workCollection = new WorkCollection();
        workCollection.setDescription(DEFAULT_DESCRIPTION);
        workCollection.setDate(DEFAULT_DATE);
        workCollection.setCoverImagePath(DEFAULT_COVER_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void createWorkCollection() throws Exception {
        int databaseSizeBeforeCreate = workCollectionRepository.findAll().size();

        // Create the WorkCollection

        restWorkCollectionMockMvc.perform(post("/api/workCollections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workCollection)))
                .andExpect(status().isCreated());

        // Validate the WorkCollection in the database
        List<WorkCollection> workCollections = workCollectionRepository.findAll();
        assertThat(workCollections).hasSize(databaseSizeBeforeCreate + 1);
        WorkCollection testWorkCollection = workCollections.get(workCollections.size() - 1);
        assertThat(testWorkCollection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkCollection.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testWorkCollection.getCoverImagePath()).isEqualTo(DEFAULT_COVER_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void getAllWorkCollections() throws Exception {
        // Initialize the database
        workCollectionRepository.saveAndFlush(workCollection);

        // Get all the workCollections
        restWorkCollectionMockMvc.perform(get("/api/workCollections"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workCollection.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].coverImagePath").value(hasItem(DEFAULT_COVER_IMAGE_PATH.toString())));
    }

    @Test
    @Transactional
    public void getWorkCollection() throws Exception {
        // Initialize the database
        workCollectionRepository.saveAndFlush(workCollection);

        // Get the workCollection
        restWorkCollectionMockMvc.perform(get("/api/workCollections/{id}", workCollection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workCollection.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.coverImagePath").value(DEFAULT_COVER_IMAGE_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkCollection() throws Exception {
        // Get the workCollection
        restWorkCollectionMockMvc.perform(get("/api/workCollections/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkCollection() throws Exception {
        // Initialize the database
        workCollectionRepository.saveAndFlush(workCollection);

		int databaseSizeBeforeUpdate = workCollectionRepository.findAll().size();

        // Update the workCollection
        workCollection.setDescription(UPDATED_DESCRIPTION);
        workCollection.setDate(UPDATED_DATE);
        workCollection.setCoverImagePath(UPDATED_COVER_IMAGE_PATH);

        restWorkCollectionMockMvc.perform(put("/api/workCollections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workCollection)))
                .andExpect(status().isOk());

        // Validate the WorkCollection in the database
        List<WorkCollection> workCollections = workCollectionRepository.findAll();
        assertThat(workCollections).hasSize(databaseSizeBeforeUpdate);
        WorkCollection testWorkCollection = workCollections.get(workCollections.size() - 1);
        assertThat(testWorkCollection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkCollection.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testWorkCollection.getCoverImagePath()).isEqualTo(UPDATED_COVER_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void deleteWorkCollection() throws Exception {
        // Initialize the database
        workCollectionRepository.saveAndFlush(workCollection);

		int databaseSizeBeforeDelete = workCollectionRepository.findAll().size();

        // Get the workCollection
        restWorkCollectionMockMvc.perform(delete("/api/workCollections/{id}", workCollection.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkCollection> workCollections = workCollectionRepository.findAll();
        assertThat(workCollections).hasSize(databaseSizeBeforeDelete - 1);
    }
}

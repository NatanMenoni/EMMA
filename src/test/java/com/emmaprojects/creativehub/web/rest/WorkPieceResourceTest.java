package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.WorkPiece;
import com.emmaprojects.creativehub.repository.WorkPieceRepository;

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
 * Test class for the WorkPieceResource REST controller.
 *
 * @see WorkPieceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WorkPieceResourceTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_IMAGE_PATH = "AAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBB";

    private static final Integer DEFAULT_YEAR_OF_CREATION = 1;
    private static final Integer UPDATED_YEAR_OF_CREATION = 2;

    private static final LocalDate DEFAULT_INPUT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INPUT_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private WorkPieceRepository workPieceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkPieceMockMvc;

    private WorkPiece workPiece;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkPieceResource workPieceResource = new WorkPieceResource();
        ReflectionTestUtils.setField(workPieceResource, "workPieceRepository", workPieceRepository);
        this.restWorkPieceMockMvc = MockMvcBuilders.standaloneSetup(workPieceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workPiece = new WorkPiece();
        workPiece.setDescription(DEFAULT_DESCRIPTION);
        workPiece.setImagePath(DEFAULT_IMAGE_PATH);
        workPiece.setYearOfCreation(DEFAULT_YEAR_OF_CREATION);
        workPiece.setInputDate(DEFAULT_INPUT_DATE);
    }

    @Test
    @Transactional
    public void createWorkPiece() throws Exception {
        int databaseSizeBeforeCreate = workPieceRepository.findAll().size();

        // Create the WorkPiece

        restWorkPieceMockMvc.perform(post("/api/workPieces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workPiece)))
                .andExpect(status().isCreated());

        // Validate the WorkPiece in the database
        List<WorkPiece> workPieces = workPieceRepository.findAll();
        assertThat(workPieces).hasSize(databaseSizeBeforeCreate + 1);
        WorkPiece testWorkPiece = workPieces.get(workPieces.size() - 1);
        assertThat(testWorkPiece.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkPiece.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);
        assertThat(testWorkPiece.getYearOfCreation()).isEqualTo(DEFAULT_YEAR_OF_CREATION);
        assertThat(testWorkPiece.getInputDate()).isEqualTo(DEFAULT_INPUT_DATE);
    }

    @Test
    @Transactional
    public void getAllWorkPieces() throws Exception {
        // Initialize the database
        workPieceRepository.saveAndFlush(workPiece);

        // Get all the workPieces
        restWorkPieceMockMvc.perform(get("/api/workPieces"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workPiece.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH.toString())))
                .andExpect(jsonPath("$.[*].yearOfCreation").value(hasItem(DEFAULT_YEAR_OF_CREATION)))
                .andExpect(jsonPath("$.[*].inputDate").value(hasItem(DEFAULT_INPUT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getWorkPiece() throws Exception {
        // Initialize the database
        workPieceRepository.saveAndFlush(workPiece);

        // Get the workPiece
        restWorkPieceMockMvc.perform(get("/api/workPieces/{id}", workPiece.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workPiece.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imagePath").value(DEFAULT_IMAGE_PATH.toString()))
            .andExpect(jsonPath("$.yearOfCreation").value(DEFAULT_YEAR_OF_CREATION))
            .andExpect(jsonPath("$.inputDate").value(DEFAULT_INPUT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkPiece() throws Exception {
        // Get the workPiece
        restWorkPieceMockMvc.perform(get("/api/workPieces/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkPiece() throws Exception {
        // Initialize the database
        workPieceRepository.saveAndFlush(workPiece);

		int databaseSizeBeforeUpdate = workPieceRepository.findAll().size();

        // Update the workPiece
        workPiece.setDescription(UPDATED_DESCRIPTION);
        workPiece.setImagePath(UPDATED_IMAGE_PATH);
        workPiece.setYearOfCreation(UPDATED_YEAR_OF_CREATION);
        workPiece.setInputDate(UPDATED_INPUT_DATE);

        restWorkPieceMockMvc.perform(put("/api/workPieces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workPiece)))
                .andExpect(status().isOk());

        // Validate the WorkPiece in the database
        List<WorkPiece> workPieces = workPieceRepository.findAll();
        assertThat(workPieces).hasSize(databaseSizeBeforeUpdate);
        WorkPiece testWorkPiece = workPieces.get(workPieces.size() - 1);
        assertThat(testWorkPiece.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkPiece.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testWorkPiece.getYearOfCreation()).isEqualTo(UPDATED_YEAR_OF_CREATION);
        assertThat(testWorkPiece.getInputDate()).isEqualTo(UPDATED_INPUT_DATE);
    }

    @Test
    @Transactional
    public void deleteWorkPiece() throws Exception {
        // Initialize the database
        workPieceRepository.saveAndFlush(workPiece);

		int databaseSizeBeforeDelete = workPieceRepository.findAll().size();

        // Get the workPiece
        restWorkPieceMockMvc.perform(delete("/api/workPieces/{id}", workPiece.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkPiece> workPieces = workPieceRepository.findAll();
        assertThat(workPieces).hasSize(databaseSizeBeforeDelete - 1);
    }
}

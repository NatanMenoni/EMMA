package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.ArtWorkPiece;
import com.emmaprojects.creativehub.repository.ArtWorkPieceRepository;

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

import com.emmaprojects.creativehub.domain.enumeration.CommercialState;

/**
 * Test class for the ArtWorkPieceResource REST controller.
 *
 * @see ArtWorkPieceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ArtWorkPieceResourceTest {


    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final Integer DEFAULT_DEPTH = 1;
    private static final Integer UPDATED_DEPTH = 2;


private static final CommercialState DEFAULT_COMMERCIAL_STATE = CommercialState.ExhibitionOnly;
    private static final CommercialState UPDATED_COMMERCIAL_STATE = CommercialState.ForSale;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    @Inject
    private ArtWorkPieceRepository artWorkPieceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restArtWorkPieceMockMvc;

    private ArtWorkPiece artWorkPiece;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArtWorkPieceResource artWorkPieceResource = new ArtWorkPieceResource();
        ReflectionTestUtils.setField(artWorkPieceResource, "artWorkPieceRepository", artWorkPieceRepository);
        this.restArtWorkPieceMockMvc = MockMvcBuilders.standaloneSetup(artWorkPieceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        artWorkPiece = new ArtWorkPiece();
        artWorkPiece.setWidth(DEFAULT_WIDTH);
        artWorkPiece.setHeight(DEFAULT_HEIGHT);
        artWorkPiece.setDepth(DEFAULT_DEPTH);
        artWorkPiece.setCommercialState(DEFAULT_COMMERCIAL_STATE);
        artWorkPiece.setPrice(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createArtWorkPiece() throws Exception {
        int databaseSizeBeforeCreate = artWorkPieceRepository.findAll().size();

        // Create the ArtWorkPiece

        restArtWorkPieceMockMvc.perform(post("/api/artWorkPieces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artWorkPiece)))
                .andExpect(status().isCreated());

        // Validate the ArtWorkPiece in the database
        List<ArtWorkPiece> artWorkPieces = artWorkPieceRepository.findAll();
        assertThat(artWorkPieces).hasSize(databaseSizeBeforeCreate + 1);
        ArtWorkPiece testArtWorkPiece = artWorkPieces.get(artWorkPieces.size() - 1);
        assertThat(testArtWorkPiece.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testArtWorkPiece.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testArtWorkPiece.getDepth()).isEqualTo(DEFAULT_DEPTH);
        assertThat(testArtWorkPiece.getCommercialState()).isEqualTo(DEFAULT_COMMERCIAL_STATE);
        assertThat(testArtWorkPiece.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void getAllArtWorkPieces() throws Exception {
        // Initialize the database
        artWorkPieceRepository.saveAndFlush(artWorkPiece);

        // Get all the artWorkPieces
        restArtWorkPieceMockMvc.perform(get("/api/artWorkPieces"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(artWorkPiece.getId().intValue())))
                .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
                .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
                .andExpect(jsonPath("$.[*].depth").value(hasItem(DEFAULT_DEPTH)))
                .andExpect(jsonPath("$.[*].commercialState").value(hasItem(DEFAULT_COMMERCIAL_STATE.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getArtWorkPiece() throws Exception {
        // Initialize the database
        artWorkPieceRepository.saveAndFlush(artWorkPiece);

        // Get the artWorkPiece
        restArtWorkPieceMockMvc.perform(get("/api/artWorkPieces/{id}", artWorkPiece.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(artWorkPiece.getId().intValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.depth").value(DEFAULT_DEPTH))
            .andExpect(jsonPath("$.commercialState").value(DEFAULT_COMMERCIAL_STATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingArtWorkPiece() throws Exception {
        // Get the artWorkPiece
        restArtWorkPieceMockMvc.perform(get("/api/artWorkPieces/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtWorkPiece() throws Exception {
        // Initialize the database
        artWorkPieceRepository.saveAndFlush(artWorkPiece);

		int databaseSizeBeforeUpdate = artWorkPieceRepository.findAll().size();

        // Update the artWorkPiece
        artWorkPiece.setWidth(UPDATED_WIDTH);
        artWorkPiece.setHeight(UPDATED_HEIGHT);
        artWorkPiece.setDepth(UPDATED_DEPTH);
        artWorkPiece.setCommercialState(UPDATED_COMMERCIAL_STATE);
        artWorkPiece.setPrice(UPDATED_PRICE);

        restArtWorkPieceMockMvc.perform(put("/api/artWorkPieces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artWorkPiece)))
                .andExpect(status().isOk());

        // Validate the ArtWorkPiece in the database
        List<ArtWorkPiece> artWorkPieces = artWorkPieceRepository.findAll();
        assertThat(artWorkPieces).hasSize(databaseSizeBeforeUpdate);
        ArtWorkPiece testArtWorkPiece = artWorkPieces.get(artWorkPieces.size() - 1);
        assertThat(testArtWorkPiece.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testArtWorkPiece.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testArtWorkPiece.getDepth()).isEqualTo(UPDATED_DEPTH);
        assertThat(testArtWorkPiece.getCommercialState()).isEqualTo(UPDATED_COMMERCIAL_STATE);
        assertThat(testArtWorkPiece.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void deleteArtWorkPiece() throws Exception {
        // Initialize the database
        artWorkPieceRepository.saveAndFlush(artWorkPiece);

		int databaseSizeBeforeDelete = artWorkPieceRepository.findAll().size();

        // Get the artWorkPiece
        restArtWorkPieceMockMvc.perform(delete("/api/artWorkPieces/{id}", artWorkPiece.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ArtWorkPiece> artWorkPieces = artWorkPieceRepository.findAll();
        assertThat(artWorkPieces).hasSize(databaseSizeBeforeDelete - 1);
    }
}

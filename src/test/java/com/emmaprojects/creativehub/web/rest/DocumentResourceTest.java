package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.Document;
import com.emmaprojects.creativehub.repository.DocumentRepository;

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
 * Test class for the DocumentResource REST controller.
 *
 * @see DocumentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DocumentResourceTest {

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBB";
    private static final String DEFAULT_DOCUMENT_NUMBER = "AAAAA";
    private static final String UPDATED_DOCUMENT_NUMBER = "BBBBB";
    private static final String DEFAULT_DOCUMENT_IMAGE_PATH = "AAAAA";
    private static final String UPDATED_DOCUMENT_IMAGE_PATH = "BBBBB";

    @Inject
    private DocumentRepository documentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDocumentMockMvc;

    private Document document;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DocumentResource documentResource = new DocumentResource();
        ReflectionTestUtils.setField(documentResource, "documentRepository", documentRepository);
        this.restDocumentMockMvc = MockMvcBuilders.standaloneSetup(documentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        document = new Document();
        document.setDocumentType(DEFAULT_DOCUMENT_TYPE);
        document.setDocumentNumber(DEFAULT_DOCUMENT_NUMBER);
        document.setDocumentImagePath(DEFAULT_DOCUMENT_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document

        restDocumentMockMvc.perform(post("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(document)))
                .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documents.get(documents.size() - 1);
        assertThat(testDocument.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testDocument.getDocumentNumber()).isEqualTo(DEFAULT_DOCUMENT_NUMBER);
        assertThat(testDocument.getDocumentImagePath()).isEqualTo(DEFAULT_DOCUMENT_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documents
        restDocumentMockMvc.perform(get("/api/documents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
                .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].documentImagePath").value(hasItem(DEFAULT_DOCUMENT_IMAGE_PATH.toString())));
    }

    @Test
    @Transactional
    public void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE.toString()))
            .andExpect(jsonPath("$.documentNumber").value(DEFAULT_DOCUMENT_NUMBER.toString()))
            .andExpect(jsonPath("$.documentImagePath").value(DEFAULT_DOCUMENT_IMAGE_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

		int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        document.setDocumentType(UPDATED_DOCUMENT_TYPE);
        document.setDocumentNumber(UPDATED_DOCUMENT_NUMBER);
        document.setDocumentImagePath(UPDATED_DOCUMENT_IMAGE_PATH);

        restDocumentMockMvc.perform(put("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(document)))
                .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documents.get(documents.size() - 1);
        assertThat(testDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testDocument.getDocumentNumber()).isEqualTo(UPDATED_DOCUMENT_NUMBER);
        assertThat(testDocument.getDocumentImagePath()).isEqualTo(UPDATED_DOCUMENT_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void deleteDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

		int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Get the document
        restDocumentMockMvc.perform(delete("/api/documents/{id}", document.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeDelete - 1);
    }
}

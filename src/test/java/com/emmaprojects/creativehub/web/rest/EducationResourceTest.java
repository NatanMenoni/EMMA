package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.Education;
import com.emmaprojects.creativehub.repository.EducationRepository;

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

import com.emmaprojects.creativehub.domain.enumeration.DegreeType;

/**
 * Test class for the EducationResource REST controller.
 *
 * @see EducationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EducationResourceTest {

    private static final String DEFAULT_INSTITUTION_NAME = "AAAAA";
    private static final String UPDATED_INSTITUTION_NAME = "BBBBB";

    private static final Integer DEFAULT_STARTING_YEAR = 1;
    private static final Integer UPDATED_STARTING_YEAR = 2;

    private static final Integer DEFAULT_FINISHING_YEAR = 1;
    private static final Integer UPDATED_FINISHING_YEAR = 2;


private static final DegreeType DEFAULT_DEGREE = DegreeType.HighSchool;
    private static final DegreeType UPDATED_DEGREE = DegreeType.BachelorDegree;
    private static final String DEFAULT_DEGREE_OTHER_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DEGREE_OTHER_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_FIELD_OF_STUDY = "AAAAA";
    private static final String UPDATED_FIELD_OF_STUDY = "BBBBB";
    private static final String DEFAULT_CAREER_NAME = "AAAAA";
    private static final String UPDATED_CAREER_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private EducationRepository educationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEducationMockMvc;

    private Education education;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EducationResource educationResource = new EducationResource();
        ReflectionTestUtils.setField(educationResource, "educationRepository", educationRepository);
        this.restEducationMockMvc = MockMvcBuilders.standaloneSetup(educationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        education = new Education();
        education.setInstitutionName(DEFAULT_INSTITUTION_NAME);
        education.setStartingYear(DEFAULT_STARTING_YEAR);
        education.setFinishingYear(DEFAULT_FINISHING_YEAR);
        education.setDegree(DEFAULT_DEGREE);
        education.setDegreeOtherDescription(DEFAULT_DEGREE_OTHER_DESCRIPTION);
        education.setFieldOfStudy(DEFAULT_FIELD_OF_STUDY);
        education.setCareerName(DEFAULT_CAREER_NAME);
        education.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createEducation() throws Exception {
        int databaseSizeBeforeCreate = educationRepository.findAll().size();

        // Create the Education

        restEducationMockMvc.perform(post("/api/educations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(education)))
                .andExpect(status().isCreated());

        // Validate the Education in the database
        List<Education> educations = educationRepository.findAll();
        assertThat(educations).hasSize(databaseSizeBeforeCreate + 1);
        Education testEducation = educations.get(educations.size() - 1);
        assertThat(testEducation.getInstitutionName()).isEqualTo(DEFAULT_INSTITUTION_NAME);
        assertThat(testEducation.getStartingYear()).isEqualTo(DEFAULT_STARTING_YEAR);
        assertThat(testEducation.getFinishingYear()).isEqualTo(DEFAULT_FINISHING_YEAR);
        assertThat(testEducation.getDegree()).isEqualTo(DEFAULT_DEGREE);
        assertThat(testEducation.getDegreeOtherDescription()).isEqualTo(DEFAULT_DEGREE_OTHER_DESCRIPTION);
        assertThat(testEducation.getFieldOfStudy()).isEqualTo(DEFAULT_FIELD_OF_STUDY);
        assertThat(testEducation.getCareerName()).isEqualTo(DEFAULT_CAREER_NAME);
        assertThat(testEducation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEducations() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educations
        restEducationMockMvc.perform(get("/api/educations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(education.getId().intValue())))
                .andExpect(jsonPath("$.[*].institutionName").value(hasItem(DEFAULT_INSTITUTION_NAME.toString())))
                .andExpect(jsonPath("$.[*].startingYear").value(hasItem(DEFAULT_STARTING_YEAR)))
                .andExpect(jsonPath("$.[*].finishingYear").value(hasItem(DEFAULT_FINISHING_YEAR)))
                .andExpect(jsonPath("$.[*].degree").value(hasItem(DEFAULT_DEGREE.toString())))
                .andExpect(jsonPath("$.[*].degreeOtherDescription").value(hasItem(DEFAULT_DEGREE_OTHER_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].fieldOfStudy").value(hasItem(DEFAULT_FIELD_OF_STUDY.toString())))
                .andExpect(jsonPath("$.[*].careerName").value(hasItem(DEFAULT_CAREER_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get the education
        restEducationMockMvc.perform(get("/api/educations/{id}", education.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(education.getId().intValue()))
            .andExpect(jsonPath("$.institutionName").value(DEFAULT_INSTITUTION_NAME.toString()))
            .andExpect(jsonPath("$.startingYear").value(DEFAULT_STARTING_YEAR))
            .andExpect(jsonPath("$.finishingYear").value(DEFAULT_FINISHING_YEAR))
            .andExpect(jsonPath("$.degree").value(DEFAULT_DEGREE.toString()))
            .andExpect(jsonPath("$.degreeOtherDescription").value(DEFAULT_DEGREE_OTHER_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fieldOfStudy").value(DEFAULT_FIELD_OF_STUDY.toString()))
            .andExpect(jsonPath("$.careerName").value(DEFAULT_CAREER_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEducation() throws Exception {
        // Get the education
        restEducationMockMvc.perform(get("/api/educations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

		int databaseSizeBeforeUpdate = educationRepository.findAll().size();

        // Update the education
        education.setInstitutionName(UPDATED_INSTITUTION_NAME);
        education.setStartingYear(UPDATED_STARTING_YEAR);
        education.setFinishingYear(UPDATED_FINISHING_YEAR);
        education.setDegree(UPDATED_DEGREE);
        education.setDegreeOtherDescription(UPDATED_DEGREE_OTHER_DESCRIPTION);
        education.setFieldOfStudy(UPDATED_FIELD_OF_STUDY);
        education.setCareerName(UPDATED_CAREER_NAME);
        education.setDescription(UPDATED_DESCRIPTION);

        restEducationMockMvc.perform(put("/api/educations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(education)))
                .andExpect(status().isOk());

        // Validate the Education in the database
        List<Education> educations = educationRepository.findAll();
        assertThat(educations).hasSize(databaseSizeBeforeUpdate);
        Education testEducation = educations.get(educations.size() - 1);
        assertThat(testEducation.getInstitutionName()).isEqualTo(UPDATED_INSTITUTION_NAME);
        assertThat(testEducation.getStartingYear()).isEqualTo(UPDATED_STARTING_YEAR);
        assertThat(testEducation.getFinishingYear()).isEqualTo(UPDATED_FINISHING_YEAR);
        assertThat(testEducation.getDegree()).isEqualTo(UPDATED_DEGREE);
        assertThat(testEducation.getDegreeOtherDescription()).isEqualTo(UPDATED_DEGREE_OTHER_DESCRIPTION);
        assertThat(testEducation.getFieldOfStudy()).isEqualTo(UPDATED_FIELD_OF_STUDY);
        assertThat(testEducation.getCareerName()).isEqualTo(UPDATED_CAREER_NAME);
        assertThat(testEducation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

		int databaseSizeBeforeDelete = educationRepository.findAll().size();

        // Get the education
        restEducationMockMvc.perform(delete("/api/educations/{id}", education.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Education> educations = educationRepository.findAll();
        assertThat(educations).hasSize(databaseSizeBeforeDelete - 1);
    }
}

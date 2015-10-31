package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.UserType;
import com.emmaprojects.creativehub.repository.UserTypeRepository;

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
 * Test class for the UserTypeResource REST controller.
 *
 * @see UserTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserTypeResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private UserTypeRepository userTypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserTypeMockMvc;

    private UserType userType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserTypeResource userTypeResource = new UserTypeResource();
        ReflectionTestUtils.setField(userTypeResource, "userTypeRepository", userTypeRepository);
        this.restUserTypeMockMvc = MockMvcBuilders.standaloneSetup(userTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userType = new UserType();
        userType.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createUserType() throws Exception {
        int databaseSizeBeforeCreate = userTypeRepository.findAll().size();

        // Create the UserType

        restUserTypeMockMvc.perform(post("/api/userTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userType)))
                .andExpect(status().isCreated());

        // Validate the UserType in the database
        List<UserType> userTypes = userTypeRepository.findAll();
        assertThat(userTypes).hasSize(databaseSizeBeforeCreate + 1);
        UserType testUserType = userTypes.get(userTypes.size() - 1);
        assertThat(testUserType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllUserTypes() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypes
        restUserTypeMockMvc.perform(get("/api/userTypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getUserType() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get the userType
        restUserTypeMockMvc.perform(get("/api/userTypes/{id}", userType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserType() throws Exception {
        // Get the userType
        restUserTypeMockMvc.perform(get("/api/userTypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserType() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

		int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();

        // Update the userType
        userType.setName(UPDATED_NAME);

        restUserTypeMockMvc.perform(put("/api/userTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userType)))
                .andExpect(status().isOk());

        // Validate the UserType in the database
        List<UserType> userTypes = userTypeRepository.findAll();
        assertThat(userTypes).hasSize(databaseSizeBeforeUpdate);
        UserType testUserType = userTypes.get(userTypes.size() - 1);
        assertThat(testUserType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteUserType() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

		int databaseSizeBeforeDelete = userTypeRepository.findAll().size();

        // Get the userType
        restUserTypeMockMvc.perform(delete("/api/userTypes/{id}", userType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserType> userTypes = userTypeRepository.findAll();
        assertThat(userTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}

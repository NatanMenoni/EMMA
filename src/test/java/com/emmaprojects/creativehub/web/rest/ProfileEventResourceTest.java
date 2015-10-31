package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.ProfileEvent;
import com.emmaprojects.creativehub.repository.ProfileEventRepository;

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
 * Test class for the ProfileEventResource REST controller.
 *
 * @see ProfileEventResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProfileEventResourceTest {

    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";
    private static final String DEFAULT_VENUE = "AAAAA";
    private static final String UPDATED_VENUE = "BBBBB";

    @Inject
    private ProfileEventRepository profileEventRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProfileEventMockMvc;

    private ProfileEvent profileEvent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProfileEventResource profileEventResource = new ProfileEventResource();
        ReflectionTestUtils.setField(profileEventResource, "profileEventRepository", profileEventRepository);
        this.restProfileEventMockMvc = MockMvcBuilders.standaloneSetup(profileEventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        profileEvent = new ProfileEvent();
        profileEvent.setType(DEFAULT_TYPE);
        profileEvent.setName(DEFAULT_NAME);
        profileEvent.setDate(DEFAULT_DATE);
        profileEvent.setDescription(DEFAULT_DESCRIPTION);
        profileEvent.setLocation(DEFAULT_LOCATION);
        profileEvent.setVenue(DEFAULT_VENUE);
    }

    @Test
    @Transactional
    public void createProfileEvent() throws Exception {
        int databaseSizeBeforeCreate = profileEventRepository.findAll().size();

        // Create the ProfileEvent

        restProfileEventMockMvc.perform(post("/api/profileEvents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(profileEvent)))
                .andExpect(status().isCreated());

        // Validate the ProfileEvent in the database
        List<ProfileEvent> profileEvents = profileEventRepository.findAll();
        assertThat(profileEvents).hasSize(databaseSizeBeforeCreate + 1);
        ProfileEvent testProfileEvent = profileEvents.get(profileEvents.size() - 1);
        assertThat(testProfileEvent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProfileEvent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProfileEvent.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProfileEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProfileEvent.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testProfileEvent.getVenue()).isEqualTo(DEFAULT_VENUE);
    }

    @Test
    @Transactional
    public void getAllProfileEvents() throws Exception {
        // Initialize the database
        profileEventRepository.saveAndFlush(profileEvent);

        // Get all the profileEvents
        restProfileEventMockMvc.perform(get("/api/profileEvents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(profileEvent.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].venue").value(hasItem(DEFAULT_VENUE.toString())));
    }

    @Test
    @Transactional
    public void getProfileEvent() throws Exception {
        // Initialize the database
        profileEventRepository.saveAndFlush(profileEvent);

        // Get the profileEvent
        restProfileEventMockMvc.perform(get("/api/profileEvents/{id}", profileEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(profileEvent.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.venue").value(DEFAULT_VENUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProfileEvent() throws Exception {
        // Get the profileEvent
        restProfileEventMockMvc.perform(get("/api/profileEvents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfileEvent() throws Exception {
        // Initialize the database
        profileEventRepository.saveAndFlush(profileEvent);

		int databaseSizeBeforeUpdate = profileEventRepository.findAll().size();

        // Update the profileEvent
        profileEvent.setType(UPDATED_TYPE);
        profileEvent.setName(UPDATED_NAME);
        profileEvent.setDate(UPDATED_DATE);
        profileEvent.setDescription(UPDATED_DESCRIPTION);
        profileEvent.setLocation(UPDATED_LOCATION);
        profileEvent.setVenue(UPDATED_VENUE);

        restProfileEventMockMvc.perform(put("/api/profileEvents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(profileEvent)))
                .andExpect(status().isOk());

        // Validate the ProfileEvent in the database
        List<ProfileEvent> profileEvents = profileEventRepository.findAll();
        assertThat(profileEvents).hasSize(databaseSizeBeforeUpdate);
        ProfileEvent testProfileEvent = profileEvents.get(profileEvents.size() - 1);
        assertThat(testProfileEvent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProfileEvent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProfileEvent.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProfileEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProfileEvent.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testProfileEvent.getVenue()).isEqualTo(UPDATED_VENUE);
    }

    @Test
    @Transactional
    public void deleteProfileEvent() throws Exception {
        // Initialize the database
        profileEventRepository.saveAndFlush(profileEvent);

		int databaseSizeBeforeDelete = profileEventRepository.findAll().size();

        // Get the profileEvent
        restProfileEventMockMvc.perform(delete("/api/profileEvents/{id}", profileEvent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProfileEvent> profileEvents = profileEventRepository.findAll();
        assertThat(profileEvents).hasSize(databaseSizeBeforeDelete - 1);
    }
}

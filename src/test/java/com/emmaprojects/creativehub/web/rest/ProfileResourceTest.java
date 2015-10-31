package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.Profile;
import com.emmaprojects.creativehub.repository.ProfileRepository;

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
 * Test class for the ProfileResource REST controller.
 *
 * @see ProfileResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProfileResourceTest {

    private static final String DEFAULT_ABOUT_ME = "AAAAA";
    private static final String UPDATED_ABOUT_ME = "BBBBB";
    private static final String DEFAULT_FACEBOOK_LINK = "AAAAA";
    private static final String UPDATED_FACEBOOK_LINK = "BBBBB";
    private static final String DEFAULT_LINKEDIN_LINK = "AAAAA";
    private static final String UPDATED_LINKEDIN_LINK = "BBBBB";
    private static final String DEFAULT_PROFILE_PICTURE_PATH = "AAAAA";
    private static final String UPDATED_PROFILE_PICTURE_PATH = "BBBBB";
    private static final String DEFAULT_COVER_PICTURE_PATH = "AAAAA";
    private static final String UPDATED_COVER_PICTURE_PATH = "BBBBB";
    private static final String DEFAULT_PROMOTION_PICTURE_PATH = "AAAAA";
    private static final String UPDATED_PROMOTION_PICTURE_PATH = "BBBBB";

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProfileMockMvc;

    private Profile profile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProfileResource profileResource = new ProfileResource();
        ReflectionTestUtils.setField(profileResource, "profileRepository", profileRepository);
        this.restProfileMockMvc = MockMvcBuilders.standaloneSetup(profileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        profile = new Profile();
        profile.setAboutMe(DEFAULT_ABOUT_ME);
        profile.setFacebookLink(DEFAULT_FACEBOOK_LINK);
        profile.setLinkedinLink(DEFAULT_LINKEDIN_LINK);
        profile.setProfilePicturePath(DEFAULT_PROFILE_PICTURE_PATH);
        profile.setCoverPicturePath(DEFAULT_COVER_PICTURE_PATH);
        profile.setPromotionPicturePath(DEFAULT_PROMOTION_PICTURE_PATH);
    }

    @Test
    @Transactional
    public void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile

        restProfileMockMvc.perform(post("/api/profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(profile)))
                .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profiles = profileRepository.findAll();
        assertThat(profiles).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profiles.get(profiles.size() - 1);
        assertThat(testProfile.getAboutMe()).isEqualTo(DEFAULT_ABOUT_ME);
        assertThat(testProfile.getFacebookLink()).isEqualTo(DEFAULT_FACEBOOK_LINK);
        assertThat(testProfile.getLinkedinLink()).isEqualTo(DEFAULT_LINKEDIN_LINK);
        assertThat(testProfile.getProfilePicturePath()).isEqualTo(DEFAULT_PROFILE_PICTURE_PATH);
        assertThat(testProfile.getCoverPicturePath()).isEqualTo(DEFAULT_COVER_PICTURE_PATH);
        assertThat(testProfile.getPromotionPicturePath()).isEqualTo(DEFAULT_PROMOTION_PICTURE_PATH);
    }

    @Test
    @Transactional
    public void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profiles
        restProfileMockMvc.perform(get("/api/profiles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
                .andExpect(jsonPath("$.[*].aboutMe").value(hasItem(DEFAULT_ABOUT_ME.toString())))
                .andExpect(jsonPath("$.[*].facebookLink").value(hasItem(DEFAULT_FACEBOOK_LINK.toString())))
                .andExpect(jsonPath("$.[*].linkedinLink").value(hasItem(DEFAULT_LINKEDIN_LINK.toString())))
                .andExpect(jsonPath("$.[*].profilePicturePath").value(hasItem(DEFAULT_PROFILE_PICTURE_PATH.toString())))
                .andExpect(jsonPath("$.[*].coverPicturePath").value(hasItem(DEFAULT_COVER_PICTURE_PATH.toString())))
                .andExpect(jsonPath("$.[*].promotionPicturePath").value(hasItem(DEFAULT_PROMOTION_PICTURE_PATH.toString())));
    }

    @Test
    @Transactional
    public void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.aboutMe").value(DEFAULT_ABOUT_ME.toString()))
            .andExpect(jsonPath("$.facebookLink").value(DEFAULT_FACEBOOK_LINK.toString()))
            .andExpect(jsonPath("$.linkedinLink").value(DEFAULT_LINKEDIN_LINK.toString()))
            .andExpect(jsonPath("$.profilePicturePath").value(DEFAULT_PROFILE_PICTURE_PATH.toString()))
            .andExpect(jsonPath("$.coverPicturePath").value(DEFAULT_COVER_PICTURE_PATH.toString()))
            .andExpect(jsonPath("$.promotionPicturePath").value(DEFAULT_PROMOTION_PICTURE_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

		int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        profile.setAboutMe(UPDATED_ABOUT_ME);
        profile.setFacebookLink(UPDATED_FACEBOOK_LINK);
        profile.setLinkedinLink(UPDATED_LINKEDIN_LINK);
        profile.setProfilePicturePath(UPDATED_PROFILE_PICTURE_PATH);
        profile.setCoverPicturePath(UPDATED_COVER_PICTURE_PATH);
        profile.setPromotionPicturePath(UPDATED_PROMOTION_PICTURE_PATH);

        restProfileMockMvc.perform(put("/api/profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(profile)))
                .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profiles = profileRepository.findAll();
        assertThat(profiles).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profiles.get(profiles.size() - 1);
        assertThat(testProfile.getAboutMe()).isEqualTo(UPDATED_ABOUT_ME);
        assertThat(testProfile.getFacebookLink()).isEqualTo(UPDATED_FACEBOOK_LINK);
        assertThat(testProfile.getLinkedinLink()).isEqualTo(UPDATED_LINKEDIN_LINK);
        assertThat(testProfile.getProfilePicturePath()).isEqualTo(UPDATED_PROFILE_PICTURE_PATH);
        assertThat(testProfile.getCoverPicturePath()).isEqualTo(UPDATED_COVER_PICTURE_PATH);
        assertThat(testProfile.getPromotionPicturePath()).isEqualTo(UPDATED_PROMOTION_PICTURE_PATH);
    }

    @Test
    @Transactional
    public void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

		int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Get the profile
        restProfileMockMvc.perform(delete("/api/profiles/{id}", profile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Profile> profiles = profileRepository.findAll();
        assertThat(profiles).hasSize(databaseSizeBeforeDelete - 1);
    }
}

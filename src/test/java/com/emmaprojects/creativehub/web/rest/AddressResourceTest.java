package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.Application;
import com.emmaprojects.creativehub.domain.Address;
import com.emmaprojects.creativehub.repository.AddressRepository;

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
 * Test class for the AddressResource REST controller.
 *
 * @see AddressResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AddressResourceTest {

    private static final String DEFAULT_PROVINCE = "AAAAA";
    private static final String UPDATED_PROVINCE = "BBBBB";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_STREET = "AAAAA";
    private static final String UPDATED_STREET = "BBBBB";

    private static final Integer DEFAULT_STREET_NUMBER = 1;
    private static final Integer UPDATED_STREET_NUMBER = 2;
    private static final String DEFAULT_APARTMENT = "AAAAA";
    private static final String UPDATED_APARTMENT = "BBBBB";

    private static final Integer DEFAULT_ZIP_POSTAL_CODE = 1;
    private static final Integer UPDATED_ZIP_POSTAL_CODE = 2;

    @Inject
    private AddressRepository addressRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAddressMockMvc;

    private Address address;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AddressResource addressResource = new AddressResource();
        ReflectionTestUtils.setField(addressResource, "addressRepository", addressRepository);
        this.restAddressMockMvc = MockMvcBuilders.standaloneSetup(addressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        address = new Address();
        address.setProvince(DEFAULT_PROVINCE);
        address.setCity(DEFAULT_CITY);
        address.setStreet(DEFAULT_STREET);
        address.setStreetNumber(DEFAULT_STREET_NUMBER);
        address.setApartment(DEFAULT_APARTMENT);
        address.setZipPostalCode(DEFAULT_ZIP_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address

        restAddressMockMvc.perform(post("/api/addresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addresss.get(addresss.size() - 1);
        assertThat(testAddress.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAddress.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testAddress.getStreetNumber()).isEqualTo(DEFAULT_STREET_NUMBER);
        assertThat(testAddress.getApartment()).isEqualTo(DEFAULT_APARTMENT);
        assertThat(testAddress.getZipPostalCode()).isEqualTo(DEFAULT_ZIP_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllAddresss() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addresss
        restAddressMockMvc.perform(get("/api/addresss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
                .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
                .andExpect(jsonPath("$.[*].streetNumber").value(hasItem(DEFAULT_STREET_NUMBER)))
                .andExpect(jsonPath("$.[*].apartment").value(hasItem(DEFAULT_APARTMENT.toString())))
                .andExpect(jsonPath("$.[*].zipPostalCode").value(hasItem(DEFAULT_ZIP_POSTAL_CODE)));
    }

    @Test
    @Transactional
    public void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc.perform(get("/api/addresss/{id}", address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.streetNumber").value(DEFAULT_STREET_NUMBER))
            .andExpect(jsonPath("$.apartment").value(DEFAULT_APARTMENT.toString()))
            .andExpect(jsonPath("$.zipPostalCode").value(DEFAULT_ZIP_POSTAL_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get("/api/addresss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

		int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        address.setProvince(UPDATED_PROVINCE);
        address.setCity(UPDATED_CITY);
        address.setStreet(UPDATED_STREET);
        address.setStreetNumber(UPDATED_STREET_NUMBER);
        address.setApartment(UPDATED_APARTMENT);
        address.setZipPostalCode(UPDATED_ZIP_POSTAL_CODE);

        restAddressMockMvc.perform(put("/api/addresss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addresss.get(addresss.size() - 1);
        assertThat(testAddress.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testAddress.getStreetNumber()).isEqualTo(UPDATED_STREET_NUMBER);
        assertThat(testAddress.getApartment()).isEqualTo(UPDATED_APARTMENT);
        assertThat(testAddress.getZipPostalCode()).isEqualTo(UPDATED_ZIP_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

		int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Get the address
        restAddressMockMvc.perform(delete("/api/addresss/{id}", address.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Address> addresss = addressRepository.findAll();
        assertThat(addresss).hasSize(databaseSizeBeforeDelete - 1);
    }
}

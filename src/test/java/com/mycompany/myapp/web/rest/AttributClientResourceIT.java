package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AttributClient;
import com.mycompany.myapp.repository.AttributClientRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link AttributClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttributClientResourceIT {

    private static final String DEFAULT_NOM_ATTRIBUT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ATTRIBUT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attribut-clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AttributClientRepository attributClientRepository;

    @Autowired
    private MockMvc restAttributClientMockMvc;

    private AttributClient attributClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributClient createEntity() {
        AttributClient attributClient = new AttributClient().nomAttribut(DEFAULT_NOM_ATTRIBUT).contenu(DEFAULT_CONTENU);
        return attributClient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributClient createUpdatedEntity() {
        AttributClient attributClient = new AttributClient().nomAttribut(UPDATED_NOM_ATTRIBUT).contenu(UPDATED_CONTENU);
        return attributClient;
    }

    @BeforeEach
    public void initTest() {
        attributClientRepository.deleteAll();
        attributClient = createEntity();
    }

    @Test
    void createAttributClient() throws Exception {
        int databaseSizeBeforeCreate = attributClientRepository.findAll().size();
        // Create the AttributClient
        restAttributClientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributClient))
            )
            .andExpect(status().isCreated());

        // Validate the AttributClient in the database
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeCreate + 1);
        AttributClient testAttributClient = attributClientList.get(attributClientList.size() - 1);
        assertThat(testAttributClient.getNomAttribut()).isEqualTo(DEFAULT_NOM_ATTRIBUT);
        assertThat(testAttributClient.getContenu()).isEqualTo(DEFAULT_CONTENU);
    }

    @Test
    void createAttributClientWithExistingId() throws Exception {
        // Create the AttributClient with an existing ID
        attributClient.setId("existing_id");

        int databaseSizeBeforeCreate = attributClientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributClientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributClient in the database
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomAttributIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributClientRepository.findAll().size();
        // set the field null
        attributClient.setNomAttribut(null);

        // Create the AttributClient, which fails.

        restAttributClientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributClient))
            )
            .andExpect(status().isBadRequest());

        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkContenuIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributClientRepository.findAll().size();
        // set the field null
        attributClient.setContenu(null);

        // Create the AttributClient, which fails.

        restAttributClientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributClient))
            )
            .andExpect(status().isBadRequest());

        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAttributClients() throws Exception {
        // Initialize the database
        attributClientRepository.save(attributClient);

        // Get all the attributClientList
        restAttributClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributClient.getId())))
            .andExpect(jsonPath("$.[*].nomAttribut").value(hasItem(DEFAULT_NOM_ATTRIBUT)))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)));
    }

    @Test
    void getAttributClient() throws Exception {
        // Initialize the database
        attributClientRepository.save(attributClient);

        // Get the attributClient
        restAttributClientMockMvc
            .perform(get(ENTITY_API_URL_ID, attributClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attributClient.getId()))
            .andExpect(jsonPath("$.nomAttribut").value(DEFAULT_NOM_ATTRIBUT))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU));
    }

    @Test
    void getNonExistingAttributClient() throws Exception {
        // Get the attributClient
        restAttributClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewAttributClient() throws Exception {
        // Initialize the database
        attributClientRepository.save(attributClient);

        int databaseSizeBeforeUpdate = attributClientRepository.findAll().size();

        // Update the attributClient
        AttributClient updatedAttributClient = attributClientRepository.findById(attributClient.getId()).get();
        updatedAttributClient.nomAttribut(UPDATED_NOM_ATTRIBUT).contenu(UPDATED_CONTENU);

        restAttributClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttributClient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttributClient))
            )
            .andExpect(status().isOk());

        // Validate the AttributClient in the database
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeUpdate);
        AttributClient testAttributClient = attributClientList.get(attributClientList.size() - 1);
        assertThat(testAttributClient.getNomAttribut()).isEqualTo(UPDATED_NOM_ATTRIBUT);
        assertThat(testAttributClient.getContenu()).isEqualTo(UPDATED_CONTENU);
    }

    @Test
    void putNonExistingAttributClient() throws Exception {
        int databaseSizeBeforeUpdate = attributClientRepository.findAll().size();
        attributClient.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributClient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributClient in the database
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAttributClient() throws Exception {
        int databaseSizeBeforeUpdate = attributClientRepository.findAll().size();
        attributClient.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributClient in the database
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAttributClient() throws Exception {
        int databaseSizeBeforeUpdate = attributClientRepository.findAll().size();
        attributClient.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributClient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributClient in the database
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAttributClientWithPatch() throws Exception {
        // Initialize the database
        attributClientRepository.save(attributClient);

        int databaseSizeBeforeUpdate = attributClientRepository.findAll().size();

        // Update the attributClient using partial update
        AttributClient partialUpdatedAttributClient = new AttributClient();
        partialUpdatedAttributClient.setId(attributClient.getId());

        partialUpdatedAttributClient.contenu(UPDATED_CONTENU);

        restAttributClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributClient))
            )
            .andExpect(status().isOk());

        // Validate the AttributClient in the database
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeUpdate);
        AttributClient testAttributClient = attributClientList.get(attributClientList.size() - 1);
        assertThat(testAttributClient.getNomAttribut()).isEqualTo(DEFAULT_NOM_ATTRIBUT);
        assertThat(testAttributClient.getContenu()).isEqualTo(UPDATED_CONTENU);
    }

    @Test
    void fullUpdateAttributClientWithPatch() throws Exception {
        // Initialize the database
        attributClientRepository.save(attributClient);

        int databaseSizeBeforeUpdate = attributClientRepository.findAll().size();

        // Update the attributClient using partial update
        AttributClient partialUpdatedAttributClient = new AttributClient();
        partialUpdatedAttributClient.setId(attributClient.getId());

        partialUpdatedAttributClient.nomAttribut(UPDATED_NOM_ATTRIBUT).contenu(UPDATED_CONTENU);

        restAttributClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributClient))
            )
            .andExpect(status().isOk());

        // Validate the AttributClient in the database
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeUpdate);
        AttributClient testAttributClient = attributClientList.get(attributClientList.size() - 1);
        assertThat(testAttributClient.getNomAttribut()).isEqualTo(UPDATED_NOM_ATTRIBUT);
        assertThat(testAttributClient.getContenu()).isEqualTo(UPDATED_CONTENU);
    }

    @Test
    void patchNonExistingAttributClient() throws Exception {
        int databaseSizeBeforeUpdate = attributClientRepository.findAll().size();
        attributClient.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attributClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributClient in the database
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAttributClient() throws Exception {
        int databaseSizeBeforeUpdate = attributClientRepository.findAll().size();
        attributClient.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributClient))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributClient in the database
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAttributClient() throws Exception {
        int databaseSizeBeforeUpdate = attributClientRepository.findAll().size();
        attributClient.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributClientMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(attributClient))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributClient in the database
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAttributClient() throws Exception {
        // Initialize the database
        attributClientRepository.save(attributClient);

        int databaseSizeBeforeDelete = attributClientRepository.findAll().size();

        // Delete the attributClient
        restAttributClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, attributClient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttributClient> attributClientList = attributClientRepository.findAll();
        assertThat(attributClientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

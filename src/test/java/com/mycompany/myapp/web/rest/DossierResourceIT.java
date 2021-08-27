package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Dossier;
import com.mycompany.myapp.repository.DossierRepository;
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
 * Integration tests for the {@link DossierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DossierResourceIT {

    private static final String DEFAULT_NUMERO_DOSSIER = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_DOSSIER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dossiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private MockMvc restDossierMockMvc;

    private Dossier dossier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dossier createEntity() {
        Dossier dossier = new Dossier().numeroDossier(DEFAULT_NUMERO_DOSSIER);
        return dossier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dossier createUpdatedEntity() {
        Dossier dossier = new Dossier().numeroDossier(UPDATED_NUMERO_DOSSIER);
        return dossier;
    }

    @BeforeEach
    public void initTest() {
        dossierRepository.deleteAll();
        dossier = createEntity();
    }

    @Test
    void createDossier() throws Exception {
        int databaseSizeBeforeCreate = dossierRepository.findAll().size();
        // Create the Dossier
        restDossierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isCreated());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate + 1);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNumeroDossier()).isEqualTo(DEFAULT_NUMERO_DOSSIER);
    }

    @Test
    void createDossierWithExistingId() throws Exception {
        // Create the Dossier with an existing ID
        dossier.setId("existing_id");

        int databaseSizeBeforeCreate = dossierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNumeroDossierIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setNumeroDossier(null);

        // Create the Dossier, which fails.

        restDossierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isBadRequest());

        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDossiers() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        // Get all the dossierList
        restDossierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossier.getId())))
            .andExpect(jsonPath("$.[*].numeroDossier").value(hasItem(DEFAULT_NUMERO_DOSSIER)));
    }

    @Test
    void getDossier() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        // Get the dossier
        restDossierMockMvc
            .perform(get(ENTITY_API_URL_ID, dossier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dossier.getId()))
            .andExpect(jsonPath("$.numeroDossier").value(DEFAULT_NUMERO_DOSSIER));
    }

    @Test
    void getNonExistingDossier() throws Exception {
        // Get the dossier
        restDossierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewDossier() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier
        Dossier updatedDossier = dossierRepository.findById(dossier.getId()).get();
        updatedDossier.numeroDossier(UPDATED_NUMERO_DOSSIER);

        restDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDossier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDossier))
            )
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNumeroDossier()).isEqualTo(UPDATED_NUMERO_DOSSIER);
    }

    @Test
    void putNonExistingDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dossier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDossierWithPatch() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier using partial update
        Dossier partialUpdatedDossier = new Dossier();
        partialUpdatedDossier.setId(dossier.getId());

        partialUpdatedDossier.numeroDossier(UPDATED_NUMERO_DOSSIER);

        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossier))
            )
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNumeroDossier()).isEqualTo(UPDATED_NUMERO_DOSSIER);
    }

    @Test
    void fullUpdateDossierWithPatch() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier using partial update
        Dossier partialUpdatedDossier = new Dossier();
        partialUpdatedDossier.setId(dossier.getId());

        partialUpdatedDossier.numeroDossier(UPDATED_NUMERO_DOSSIER);

        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossier))
            )
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNumeroDossier()).isEqualTo(UPDATED_NUMERO_DOSSIER);
    }

    @Test
    void patchNonExistingDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dossier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDossier() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        int databaseSizeBeforeDelete = dossierRepository.findAll().size();

        // Delete the dossier
        restDossierMockMvc
            .perform(delete(ENTITY_API_URL_ID, dossier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

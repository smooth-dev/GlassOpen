package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Annexe;
import com.mycompany.myapp.repository.AnnexeRepository;
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
 * Integration tests for the {@link AnnexeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnnexeResourceIT {

    private static final String DEFAULT_NOM_ANNEXE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ANNEXE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTETE_ANNEXE = "AAAAAAAAAA";
    private static final String UPDATED_ENTETE_ANNEXE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/annexes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AnnexeRepository annexeRepository;

    @Autowired
    private MockMvc restAnnexeMockMvc;

    private Annexe annexe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annexe createEntity() {
        Annexe annexe = new Annexe().nomAnnexe(DEFAULT_NOM_ANNEXE).enteteAnnexe(DEFAULT_ENTETE_ANNEXE);
        return annexe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annexe createUpdatedEntity() {
        Annexe annexe = new Annexe().nomAnnexe(UPDATED_NOM_ANNEXE).enteteAnnexe(UPDATED_ENTETE_ANNEXE);
        return annexe;
    }

    @BeforeEach
    public void initTest() {
        annexeRepository.deleteAll();
        annexe = createEntity();
    }

    @Test
    void createAnnexe() throws Exception {
        int databaseSizeBeforeCreate = annexeRepository.findAll().size();
        // Create the Annexe
        restAnnexeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexe)))
            .andExpect(status().isCreated());

        // Validate the Annexe in the database
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeCreate + 1);
        Annexe testAnnexe = annexeList.get(annexeList.size() - 1);
        assertThat(testAnnexe.getNomAnnexe()).isEqualTo(DEFAULT_NOM_ANNEXE);
        assertThat(testAnnexe.getEnteteAnnexe()).isEqualTo(DEFAULT_ENTETE_ANNEXE);
    }

    @Test
    void createAnnexeWithExistingId() throws Exception {
        // Create the Annexe with an existing ID
        annexe.setId("existing_id");

        int databaseSizeBeforeCreate = annexeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnexeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexe)))
            .andExpect(status().isBadRequest());

        // Validate the Annexe in the database
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomAnnexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = annexeRepository.findAll().size();
        // set the field null
        annexe.setNomAnnexe(null);

        // Create the Annexe, which fails.

        restAnnexeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexe)))
            .andExpect(status().isBadRequest());

        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEnteteAnnexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = annexeRepository.findAll().size();
        // set the field null
        annexe.setEnteteAnnexe(null);

        // Create the Annexe, which fails.

        restAnnexeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexe)))
            .andExpect(status().isBadRequest());

        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAnnexes() throws Exception {
        // Initialize the database
        annexeRepository.save(annexe);

        // Get all the annexeList
        restAnnexeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annexe.getId())))
            .andExpect(jsonPath("$.[*].nomAnnexe").value(hasItem(DEFAULT_NOM_ANNEXE)))
            .andExpect(jsonPath("$.[*].enteteAnnexe").value(hasItem(DEFAULT_ENTETE_ANNEXE)));
    }

    @Test
    void getAnnexe() throws Exception {
        // Initialize the database
        annexeRepository.save(annexe);

        // Get the annexe
        restAnnexeMockMvc
            .perform(get(ENTITY_API_URL_ID, annexe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annexe.getId()))
            .andExpect(jsonPath("$.nomAnnexe").value(DEFAULT_NOM_ANNEXE))
            .andExpect(jsonPath("$.enteteAnnexe").value(DEFAULT_ENTETE_ANNEXE));
    }

    @Test
    void getNonExistingAnnexe() throws Exception {
        // Get the annexe
        restAnnexeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewAnnexe() throws Exception {
        // Initialize the database
        annexeRepository.save(annexe);

        int databaseSizeBeforeUpdate = annexeRepository.findAll().size();

        // Update the annexe
        Annexe updatedAnnexe = annexeRepository.findById(annexe.getId()).get();
        updatedAnnexe.nomAnnexe(UPDATED_NOM_ANNEXE).enteteAnnexe(UPDATED_ENTETE_ANNEXE);

        restAnnexeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnnexe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAnnexe))
            )
            .andExpect(status().isOk());

        // Validate the Annexe in the database
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeUpdate);
        Annexe testAnnexe = annexeList.get(annexeList.size() - 1);
        assertThat(testAnnexe.getNomAnnexe()).isEqualTo(UPDATED_NOM_ANNEXE);
        assertThat(testAnnexe.getEnteteAnnexe()).isEqualTo(UPDATED_ENTETE_ANNEXE);
    }

    @Test
    void putNonExistingAnnexe() throws Exception {
        int databaseSizeBeforeUpdate = annexeRepository.findAll().size();
        annexe.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnexeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, annexe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annexe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annexe in the database
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAnnexe() throws Exception {
        int databaseSizeBeforeUpdate = annexeRepository.findAll().size();
        annexe.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annexe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annexe in the database
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAnnexe() throws Exception {
        int databaseSizeBeforeUpdate = annexeRepository.findAll().size();
        annexe.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Annexe in the database
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAnnexeWithPatch() throws Exception {
        // Initialize the database
        annexeRepository.save(annexe);

        int databaseSizeBeforeUpdate = annexeRepository.findAll().size();

        // Update the annexe using partial update
        Annexe partialUpdatedAnnexe = new Annexe();
        partialUpdatedAnnexe.setId(annexe.getId());

        partialUpdatedAnnexe.enteteAnnexe(UPDATED_ENTETE_ANNEXE);

        restAnnexeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnexe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnexe))
            )
            .andExpect(status().isOk());

        // Validate the Annexe in the database
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeUpdate);
        Annexe testAnnexe = annexeList.get(annexeList.size() - 1);
        assertThat(testAnnexe.getNomAnnexe()).isEqualTo(DEFAULT_NOM_ANNEXE);
        assertThat(testAnnexe.getEnteteAnnexe()).isEqualTo(UPDATED_ENTETE_ANNEXE);
    }

    @Test
    void fullUpdateAnnexeWithPatch() throws Exception {
        // Initialize the database
        annexeRepository.save(annexe);

        int databaseSizeBeforeUpdate = annexeRepository.findAll().size();

        // Update the annexe using partial update
        Annexe partialUpdatedAnnexe = new Annexe();
        partialUpdatedAnnexe.setId(annexe.getId());

        partialUpdatedAnnexe.nomAnnexe(UPDATED_NOM_ANNEXE).enteteAnnexe(UPDATED_ENTETE_ANNEXE);

        restAnnexeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnexe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnexe))
            )
            .andExpect(status().isOk());

        // Validate the Annexe in the database
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeUpdate);
        Annexe testAnnexe = annexeList.get(annexeList.size() - 1);
        assertThat(testAnnexe.getNomAnnexe()).isEqualTo(UPDATED_NOM_ANNEXE);
        assertThat(testAnnexe.getEnteteAnnexe()).isEqualTo(UPDATED_ENTETE_ANNEXE);
    }

    @Test
    void patchNonExistingAnnexe() throws Exception {
        int databaseSizeBeforeUpdate = annexeRepository.findAll().size();
        annexe.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnexeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, annexe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(annexe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annexe in the database
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAnnexe() throws Exception {
        int databaseSizeBeforeUpdate = annexeRepository.findAll().size();
        annexe.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(annexe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annexe in the database
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAnnexe() throws Exception {
        int databaseSizeBeforeUpdate = annexeRepository.findAll().size();
        annexe.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(annexe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Annexe in the database
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAnnexe() throws Exception {
        // Initialize the database
        annexeRepository.save(annexe);

        int databaseSizeBeforeDelete = annexeRepository.findAll().size();

        // Delete the annexe
        restAnnexeMockMvc
            .perform(delete(ENTITY_API_URL_ID, annexe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Annexe> annexeList = annexeRepository.findAll();
        assertThat(annexeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

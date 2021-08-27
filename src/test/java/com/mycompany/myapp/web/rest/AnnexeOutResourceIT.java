package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AnnexeOut;
import com.mycompany.myapp.repository.AnnexeOutRepository;
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
 * Integration tests for the {@link AnnexeOutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnnexeOutResourceIT {

    private static final String DEFAULT_NOM_ANNEXE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ANNEXE = "BBBBBBBBBB";

    private static final String DEFAULT_ID_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_ID_CLIENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/annexe-outs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AnnexeOutRepository annexeOutRepository;

    @Autowired
    private MockMvc restAnnexeOutMockMvc;

    private AnnexeOut annexeOut;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnexeOut createEntity() {
        AnnexeOut annexeOut = new AnnexeOut().nomAnnexe(DEFAULT_NOM_ANNEXE).idClient(DEFAULT_ID_CLIENT);
        return annexeOut;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnexeOut createUpdatedEntity() {
        AnnexeOut annexeOut = new AnnexeOut().nomAnnexe(UPDATED_NOM_ANNEXE).idClient(UPDATED_ID_CLIENT);
        return annexeOut;
    }

    @BeforeEach
    public void initTest() {
        annexeOutRepository.deleteAll();
        annexeOut = createEntity();
    }

    @Test
    void createAnnexeOut() throws Exception {
        int databaseSizeBeforeCreate = annexeOutRepository.findAll().size();
        // Create the AnnexeOut
        restAnnexeOutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexeOut)))
            .andExpect(status().isCreated());

        // Validate the AnnexeOut in the database
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeCreate + 1);
        AnnexeOut testAnnexeOut = annexeOutList.get(annexeOutList.size() - 1);
        assertThat(testAnnexeOut.getNomAnnexe()).isEqualTo(DEFAULT_NOM_ANNEXE);
        assertThat(testAnnexeOut.getIdClient()).isEqualTo(DEFAULT_ID_CLIENT);
    }

    @Test
    void createAnnexeOutWithExistingId() throws Exception {
        // Create the AnnexeOut with an existing ID
        annexeOut.setId("existing_id");

        int databaseSizeBeforeCreate = annexeOutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnexeOutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexeOut)))
            .andExpect(status().isBadRequest());

        // Validate the AnnexeOut in the database
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomAnnexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = annexeOutRepository.findAll().size();
        // set the field null
        annexeOut.setNomAnnexe(null);

        // Create the AnnexeOut, which fails.

        restAnnexeOutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexeOut)))
            .andExpect(status().isBadRequest());

        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIdClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = annexeOutRepository.findAll().size();
        // set the field null
        annexeOut.setIdClient(null);

        // Create the AnnexeOut, which fails.

        restAnnexeOutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexeOut)))
            .andExpect(status().isBadRequest());

        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAnnexeOuts() throws Exception {
        // Initialize the database
        annexeOutRepository.save(annexeOut);

        // Get all the annexeOutList
        restAnnexeOutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annexeOut.getId())))
            .andExpect(jsonPath("$.[*].nomAnnexe").value(hasItem(DEFAULT_NOM_ANNEXE)))
            .andExpect(jsonPath("$.[*].idClient").value(hasItem(DEFAULT_ID_CLIENT)));
    }

    @Test
    void getAnnexeOut() throws Exception {
        // Initialize the database
        annexeOutRepository.save(annexeOut);

        // Get the annexeOut
        restAnnexeOutMockMvc
            .perform(get(ENTITY_API_URL_ID, annexeOut.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annexeOut.getId()))
            .andExpect(jsonPath("$.nomAnnexe").value(DEFAULT_NOM_ANNEXE))
            .andExpect(jsonPath("$.idClient").value(DEFAULT_ID_CLIENT));
    }

    @Test
    void getNonExistingAnnexeOut() throws Exception {
        // Get the annexeOut
        restAnnexeOutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewAnnexeOut() throws Exception {
        // Initialize the database
        annexeOutRepository.save(annexeOut);

        int databaseSizeBeforeUpdate = annexeOutRepository.findAll().size();

        // Update the annexeOut
        AnnexeOut updatedAnnexeOut = annexeOutRepository.findById(annexeOut.getId()).get();
        updatedAnnexeOut.nomAnnexe(UPDATED_NOM_ANNEXE).idClient(UPDATED_ID_CLIENT);

        restAnnexeOutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnnexeOut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAnnexeOut))
            )
            .andExpect(status().isOk());

        // Validate the AnnexeOut in the database
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeUpdate);
        AnnexeOut testAnnexeOut = annexeOutList.get(annexeOutList.size() - 1);
        assertThat(testAnnexeOut.getNomAnnexe()).isEqualTo(UPDATED_NOM_ANNEXE);
        assertThat(testAnnexeOut.getIdClient()).isEqualTo(UPDATED_ID_CLIENT);
    }

    @Test
    void putNonExistingAnnexeOut() throws Exception {
        int databaseSizeBeforeUpdate = annexeOutRepository.findAll().size();
        annexeOut.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnexeOutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, annexeOut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annexeOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexeOut in the database
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAnnexeOut() throws Exception {
        int databaseSizeBeforeUpdate = annexeOutRepository.findAll().size();
        annexeOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeOutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annexeOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexeOut in the database
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAnnexeOut() throws Exception {
        int databaseSizeBeforeUpdate = annexeOutRepository.findAll().size();
        annexeOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeOutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexeOut)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnexeOut in the database
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAnnexeOutWithPatch() throws Exception {
        // Initialize the database
        annexeOutRepository.save(annexeOut);

        int databaseSizeBeforeUpdate = annexeOutRepository.findAll().size();

        // Update the annexeOut using partial update
        AnnexeOut partialUpdatedAnnexeOut = new AnnexeOut();
        partialUpdatedAnnexeOut.setId(annexeOut.getId());

        partialUpdatedAnnexeOut.idClient(UPDATED_ID_CLIENT);

        restAnnexeOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnexeOut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnexeOut))
            )
            .andExpect(status().isOk());

        // Validate the AnnexeOut in the database
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeUpdate);
        AnnexeOut testAnnexeOut = annexeOutList.get(annexeOutList.size() - 1);
        assertThat(testAnnexeOut.getNomAnnexe()).isEqualTo(DEFAULT_NOM_ANNEXE);
        assertThat(testAnnexeOut.getIdClient()).isEqualTo(UPDATED_ID_CLIENT);
    }

    @Test
    void fullUpdateAnnexeOutWithPatch() throws Exception {
        // Initialize the database
        annexeOutRepository.save(annexeOut);

        int databaseSizeBeforeUpdate = annexeOutRepository.findAll().size();

        // Update the annexeOut using partial update
        AnnexeOut partialUpdatedAnnexeOut = new AnnexeOut();
        partialUpdatedAnnexeOut.setId(annexeOut.getId());

        partialUpdatedAnnexeOut.nomAnnexe(UPDATED_NOM_ANNEXE).idClient(UPDATED_ID_CLIENT);

        restAnnexeOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnexeOut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnexeOut))
            )
            .andExpect(status().isOk());

        // Validate the AnnexeOut in the database
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeUpdate);
        AnnexeOut testAnnexeOut = annexeOutList.get(annexeOutList.size() - 1);
        assertThat(testAnnexeOut.getNomAnnexe()).isEqualTo(UPDATED_NOM_ANNEXE);
        assertThat(testAnnexeOut.getIdClient()).isEqualTo(UPDATED_ID_CLIENT);
    }

    @Test
    void patchNonExistingAnnexeOut() throws Exception {
        int databaseSizeBeforeUpdate = annexeOutRepository.findAll().size();
        annexeOut.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnexeOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, annexeOut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(annexeOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexeOut in the database
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAnnexeOut() throws Exception {
        int databaseSizeBeforeUpdate = annexeOutRepository.findAll().size();
        annexeOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(annexeOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexeOut in the database
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAnnexeOut() throws Exception {
        int databaseSizeBeforeUpdate = annexeOutRepository.findAll().size();
        annexeOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeOutMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(annexeOut))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnexeOut in the database
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAnnexeOut() throws Exception {
        // Initialize the database
        annexeOutRepository.save(annexeOut);

        int databaseSizeBeforeDelete = annexeOutRepository.findAll().size();

        // Delete the annexeOut
        restAnnexeOutMockMvc
            .perform(delete(ENTITY_API_URL_ID, annexeOut.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnnexeOut> annexeOutList = annexeOutRepository.findAll();
        assertThat(annexeOutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

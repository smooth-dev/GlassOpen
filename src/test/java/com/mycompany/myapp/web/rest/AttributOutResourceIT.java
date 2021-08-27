package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AttributOut;
import com.mycompany.myapp.repository.AttributOutRepository;
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
 * Integration tests for the {@link AttributOutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttributOutResourceIT {

    private static final String DEFAULT_NOM_ATTRIBUT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ATTRIBUT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attribut-outs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AttributOutRepository attributOutRepository;

    @Autowired
    private MockMvc restAttributOutMockMvc;

    private AttributOut attributOut;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributOut createEntity() {
        AttributOut attributOut = new AttributOut().nomAttribut(DEFAULT_NOM_ATTRIBUT);
        return attributOut;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributOut createUpdatedEntity() {
        AttributOut attributOut = new AttributOut().nomAttribut(UPDATED_NOM_ATTRIBUT);
        return attributOut;
    }

    @BeforeEach
    public void initTest() {
        attributOutRepository.deleteAll();
        attributOut = createEntity();
    }

    @Test
    void createAttributOut() throws Exception {
        int databaseSizeBeforeCreate = attributOutRepository.findAll().size();
        // Create the AttributOut
        restAttributOutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributOut)))
            .andExpect(status().isCreated());

        // Validate the AttributOut in the database
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeCreate + 1);
        AttributOut testAttributOut = attributOutList.get(attributOutList.size() - 1);
        assertThat(testAttributOut.getNomAttribut()).isEqualTo(DEFAULT_NOM_ATTRIBUT);
    }

    @Test
    void createAttributOutWithExistingId() throws Exception {
        // Create the AttributOut with an existing ID
        attributOut.setId("existing_id");

        int databaseSizeBeforeCreate = attributOutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributOutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributOut)))
            .andExpect(status().isBadRequest());

        // Validate the AttributOut in the database
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomAttributIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributOutRepository.findAll().size();
        // set the field null
        attributOut.setNomAttribut(null);

        // Create the AttributOut, which fails.

        restAttributOutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributOut)))
            .andExpect(status().isBadRequest());

        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAttributOuts() throws Exception {
        // Initialize the database
        attributOutRepository.save(attributOut);

        // Get all the attributOutList
        restAttributOutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributOut.getId())))
            .andExpect(jsonPath("$.[*].nomAttribut").value(hasItem(DEFAULT_NOM_ATTRIBUT)));
    }

    @Test
    void getAttributOut() throws Exception {
        // Initialize the database
        attributOutRepository.save(attributOut);

        // Get the attributOut
        restAttributOutMockMvc
            .perform(get(ENTITY_API_URL_ID, attributOut.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attributOut.getId()))
            .andExpect(jsonPath("$.nomAttribut").value(DEFAULT_NOM_ATTRIBUT));
    }

    @Test
    void getNonExistingAttributOut() throws Exception {
        // Get the attributOut
        restAttributOutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewAttributOut() throws Exception {
        // Initialize the database
        attributOutRepository.save(attributOut);

        int databaseSizeBeforeUpdate = attributOutRepository.findAll().size();

        // Update the attributOut
        AttributOut updatedAttributOut = attributOutRepository.findById(attributOut.getId()).get();
        updatedAttributOut.nomAttribut(UPDATED_NOM_ATTRIBUT);

        restAttributOutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttributOut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttributOut))
            )
            .andExpect(status().isOk());

        // Validate the AttributOut in the database
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeUpdate);
        AttributOut testAttributOut = attributOutList.get(attributOutList.size() - 1);
        assertThat(testAttributOut.getNomAttribut()).isEqualTo(UPDATED_NOM_ATTRIBUT);
    }

    @Test
    void putNonExistingAttributOut() throws Exception {
        int databaseSizeBeforeUpdate = attributOutRepository.findAll().size();
        attributOut.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributOutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributOut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributOut in the database
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAttributOut() throws Exception {
        int databaseSizeBeforeUpdate = attributOutRepository.findAll().size();
        attributOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributOutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributOut in the database
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAttributOut() throws Exception {
        int databaseSizeBeforeUpdate = attributOutRepository.findAll().size();
        attributOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributOutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributOut)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributOut in the database
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAttributOutWithPatch() throws Exception {
        // Initialize the database
        attributOutRepository.save(attributOut);

        int databaseSizeBeforeUpdate = attributOutRepository.findAll().size();

        // Update the attributOut using partial update
        AttributOut partialUpdatedAttributOut = new AttributOut();
        partialUpdatedAttributOut.setId(attributOut.getId());

        restAttributOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributOut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributOut))
            )
            .andExpect(status().isOk());

        // Validate the AttributOut in the database
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeUpdate);
        AttributOut testAttributOut = attributOutList.get(attributOutList.size() - 1);
        assertThat(testAttributOut.getNomAttribut()).isEqualTo(DEFAULT_NOM_ATTRIBUT);
    }

    @Test
    void fullUpdateAttributOutWithPatch() throws Exception {
        // Initialize the database
        attributOutRepository.save(attributOut);

        int databaseSizeBeforeUpdate = attributOutRepository.findAll().size();

        // Update the attributOut using partial update
        AttributOut partialUpdatedAttributOut = new AttributOut();
        partialUpdatedAttributOut.setId(attributOut.getId());

        partialUpdatedAttributOut.nomAttribut(UPDATED_NOM_ATTRIBUT);

        restAttributOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributOut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributOut))
            )
            .andExpect(status().isOk());

        // Validate the AttributOut in the database
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeUpdate);
        AttributOut testAttributOut = attributOutList.get(attributOutList.size() - 1);
        assertThat(testAttributOut.getNomAttribut()).isEqualTo(UPDATED_NOM_ATTRIBUT);
    }

    @Test
    void patchNonExistingAttributOut() throws Exception {
        int databaseSizeBeforeUpdate = attributOutRepository.findAll().size();
        attributOut.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attributOut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributOut in the database
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAttributOut() throws Exception {
        int databaseSizeBeforeUpdate = attributOutRepository.findAll().size();
        attributOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributOutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributOut))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributOut in the database
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAttributOut() throws Exception {
        int databaseSizeBeforeUpdate = attributOutRepository.findAll().size();
        attributOut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributOutMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(attributOut))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributOut in the database
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAttributOut() throws Exception {
        // Initialize the database
        attributOutRepository.save(attributOut);

        int databaseSizeBeforeDelete = attributOutRepository.findAll().size();

        // Delete the attributOut
        restAttributOutMockMvc
            .perform(delete(ENTITY_API_URL_ID, attributOut.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttributOut> attributOutList = attributOutRepository.findAll();
        assertThat(attributOutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

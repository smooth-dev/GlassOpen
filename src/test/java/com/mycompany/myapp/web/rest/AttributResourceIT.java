package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Attribut;
import com.mycompany.myapp.repository.AttributRepository;
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
 * Integration tests for the {@link AttributResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttributResourceIT {

    private static final String DEFAULT_NOM_ATTRIBUT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ATTRIBUT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attributs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AttributRepository attributRepository;

    @Autowired
    private MockMvc restAttributMockMvc;

    private Attribut attribut;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attribut createEntity() {
        Attribut attribut = new Attribut().nomAttribut(DEFAULT_NOM_ATTRIBUT);
        return attribut;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attribut createUpdatedEntity() {
        Attribut attribut = new Attribut().nomAttribut(UPDATED_NOM_ATTRIBUT);
        return attribut;
    }

    @BeforeEach
    public void initTest() {
        attributRepository.deleteAll();
        attribut = createEntity();
    }

    @Test
    void createAttribut() throws Exception {
        int databaseSizeBeforeCreate = attributRepository.findAll().size();
        // Create the Attribut
        restAttributMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attribut)))
            .andExpect(status().isCreated());

        // Validate the Attribut in the database
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeCreate + 1);
        Attribut testAttribut = attributList.get(attributList.size() - 1);
        assertThat(testAttribut.getNomAttribut()).isEqualTo(DEFAULT_NOM_ATTRIBUT);
    }

    @Test
    void createAttributWithExistingId() throws Exception {
        // Create the Attribut with an existing ID
        attribut.setId("existing_id");

        int databaseSizeBeforeCreate = attributRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attribut)))
            .andExpect(status().isBadRequest());

        // Validate the Attribut in the database
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomAttributIsRequired() throws Exception {
        int databaseSizeBeforeTest = attributRepository.findAll().size();
        // set the field null
        attribut.setNomAttribut(null);

        // Create the Attribut, which fails.

        restAttributMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attribut)))
            .andExpect(status().isBadRequest());

        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAttributs() throws Exception {
        // Initialize the database
        attributRepository.save(attribut);

        // Get all the attributList
        restAttributMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attribut.getId())))
            .andExpect(jsonPath("$.[*].nomAttribut").value(hasItem(DEFAULT_NOM_ATTRIBUT)));
    }

    @Test
    void getAttribut() throws Exception {
        // Initialize the database
        attributRepository.save(attribut);

        // Get the attribut
        restAttributMockMvc
            .perform(get(ENTITY_API_URL_ID, attribut.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attribut.getId()))
            .andExpect(jsonPath("$.nomAttribut").value(DEFAULT_NOM_ATTRIBUT));
    }

    @Test
    void getNonExistingAttribut() throws Exception {
        // Get the attribut
        restAttributMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewAttribut() throws Exception {
        // Initialize the database
        attributRepository.save(attribut);

        int databaseSizeBeforeUpdate = attributRepository.findAll().size();

        // Update the attribut
        Attribut updatedAttribut = attributRepository.findById(attribut.getId()).get();
        updatedAttribut.nomAttribut(UPDATED_NOM_ATTRIBUT);

        restAttributMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttribut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttribut))
            )
            .andExpect(status().isOk());

        // Validate the Attribut in the database
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeUpdate);
        Attribut testAttribut = attributList.get(attributList.size() - 1);
        assertThat(testAttribut.getNomAttribut()).isEqualTo(UPDATED_NOM_ATTRIBUT);
    }

    @Test
    void putNonExistingAttribut() throws Exception {
        int databaseSizeBeforeUpdate = attributRepository.findAll().size();
        attribut.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attribut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attribut in the database
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAttribut() throws Exception {
        int databaseSizeBeforeUpdate = attributRepository.findAll().size();
        attribut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attribut in the database
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAttribut() throws Exception {
        int databaseSizeBeforeUpdate = attributRepository.findAll().size();
        attribut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attribut)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attribut in the database
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAttributWithPatch() throws Exception {
        // Initialize the database
        attributRepository.save(attribut);

        int databaseSizeBeforeUpdate = attributRepository.findAll().size();

        // Update the attribut using partial update
        Attribut partialUpdatedAttribut = new Attribut();
        partialUpdatedAttribut.setId(attribut.getId());

        partialUpdatedAttribut.nomAttribut(UPDATED_NOM_ATTRIBUT);

        restAttributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttribut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttribut))
            )
            .andExpect(status().isOk());

        // Validate the Attribut in the database
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeUpdate);
        Attribut testAttribut = attributList.get(attributList.size() - 1);
        assertThat(testAttribut.getNomAttribut()).isEqualTo(UPDATED_NOM_ATTRIBUT);
    }

    @Test
    void fullUpdateAttributWithPatch() throws Exception {
        // Initialize the database
        attributRepository.save(attribut);

        int databaseSizeBeforeUpdate = attributRepository.findAll().size();

        // Update the attribut using partial update
        Attribut partialUpdatedAttribut = new Attribut();
        partialUpdatedAttribut.setId(attribut.getId());

        partialUpdatedAttribut.nomAttribut(UPDATED_NOM_ATTRIBUT);

        restAttributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttribut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttribut))
            )
            .andExpect(status().isOk());

        // Validate the Attribut in the database
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeUpdate);
        Attribut testAttribut = attributList.get(attributList.size() - 1);
        assertThat(testAttribut.getNomAttribut()).isEqualTo(UPDATED_NOM_ATTRIBUT);
    }

    @Test
    void patchNonExistingAttribut() throws Exception {
        int databaseSizeBeforeUpdate = attributRepository.findAll().size();
        attribut.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attribut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attribut in the database
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAttribut() throws Exception {
        int databaseSizeBeforeUpdate = attributRepository.findAll().size();
        attribut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attribut in the database
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAttribut() throws Exception {
        int databaseSizeBeforeUpdate = attributRepository.findAll().size();
        attribut.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(attribut)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attribut in the database
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAttribut() throws Exception {
        // Initialize the database
        attributRepository.save(attribut);

        int databaseSizeBeforeDelete = attributRepository.findAll().size();

        // Delete the attribut
        restAttributMockMvc
            .perform(delete(ENTITY_API_URL_ID, attribut.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Attribut> attributList = attributRepository.findAll();
        assertThat(attributList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

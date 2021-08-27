package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AnnexeGroup;
import com.mycompany.myapp.repository.AnnexeGroupRepository;
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
 * Integration tests for the {@link AnnexeGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnnexeGroupResourceIT {

    private static final String DEFAULT_NOM_ANNEXE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ANNEXE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/annexe-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AnnexeGroupRepository annexeGroupRepository;

    @Autowired
    private MockMvc restAnnexeGroupMockMvc;

    private AnnexeGroup annexeGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnexeGroup createEntity() {
        AnnexeGroup annexeGroup = new AnnexeGroup().nomAnnexe(DEFAULT_NOM_ANNEXE);
        return annexeGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnexeGroup createUpdatedEntity() {
        AnnexeGroup annexeGroup = new AnnexeGroup().nomAnnexe(UPDATED_NOM_ANNEXE);
        return annexeGroup;
    }

    @BeforeEach
    public void initTest() {
        annexeGroupRepository.deleteAll();
        annexeGroup = createEntity();
    }

    @Test
    void createAnnexeGroup() throws Exception {
        int databaseSizeBeforeCreate = annexeGroupRepository.findAll().size();
        // Create the AnnexeGroup
        restAnnexeGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexeGroup)))
            .andExpect(status().isCreated());

        // Validate the AnnexeGroup in the database
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeCreate + 1);
        AnnexeGroup testAnnexeGroup = annexeGroupList.get(annexeGroupList.size() - 1);
        assertThat(testAnnexeGroup.getNomAnnexe()).isEqualTo(DEFAULT_NOM_ANNEXE);
    }

    @Test
    void createAnnexeGroupWithExistingId() throws Exception {
        // Create the AnnexeGroup with an existing ID
        annexeGroup.setId("existing_id");

        int databaseSizeBeforeCreate = annexeGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnexeGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexeGroup)))
            .andExpect(status().isBadRequest());

        // Validate the AnnexeGroup in the database
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomAnnexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = annexeGroupRepository.findAll().size();
        // set the field null
        annexeGroup.setNomAnnexe(null);

        // Create the AnnexeGroup, which fails.

        restAnnexeGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexeGroup)))
            .andExpect(status().isBadRequest());

        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAnnexeGroups() throws Exception {
        // Initialize the database
        annexeGroupRepository.save(annexeGroup);

        // Get all the annexeGroupList
        restAnnexeGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annexeGroup.getId())))
            .andExpect(jsonPath("$.[*].nomAnnexe").value(hasItem(DEFAULT_NOM_ANNEXE)));
    }

    @Test
    void getAnnexeGroup() throws Exception {
        // Initialize the database
        annexeGroupRepository.save(annexeGroup);

        // Get the annexeGroup
        restAnnexeGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, annexeGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annexeGroup.getId()))
            .andExpect(jsonPath("$.nomAnnexe").value(DEFAULT_NOM_ANNEXE));
    }

    @Test
    void getNonExistingAnnexeGroup() throws Exception {
        // Get the annexeGroup
        restAnnexeGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewAnnexeGroup() throws Exception {
        // Initialize the database
        annexeGroupRepository.save(annexeGroup);

        int databaseSizeBeforeUpdate = annexeGroupRepository.findAll().size();

        // Update the annexeGroup
        AnnexeGroup updatedAnnexeGroup = annexeGroupRepository.findById(annexeGroup.getId()).get();
        updatedAnnexeGroup.nomAnnexe(UPDATED_NOM_ANNEXE);

        restAnnexeGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnnexeGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAnnexeGroup))
            )
            .andExpect(status().isOk());

        // Validate the AnnexeGroup in the database
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeUpdate);
        AnnexeGroup testAnnexeGroup = annexeGroupList.get(annexeGroupList.size() - 1);
        assertThat(testAnnexeGroup.getNomAnnexe()).isEqualTo(UPDATED_NOM_ANNEXE);
    }

    @Test
    void putNonExistingAnnexeGroup() throws Exception {
        int databaseSizeBeforeUpdate = annexeGroupRepository.findAll().size();
        annexeGroup.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnexeGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, annexeGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annexeGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexeGroup in the database
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAnnexeGroup() throws Exception {
        int databaseSizeBeforeUpdate = annexeGroupRepository.findAll().size();
        annexeGroup.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annexeGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexeGroup in the database
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAnnexeGroup() throws Exception {
        int databaseSizeBeforeUpdate = annexeGroupRepository.findAll().size();
        annexeGroup.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annexeGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnexeGroup in the database
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAnnexeGroupWithPatch() throws Exception {
        // Initialize the database
        annexeGroupRepository.save(annexeGroup);

        int databaseSizeBeforeUpdate = annexeGroupRepository.findAll().size();

        // Update the annexeGroup using partial update
        AnnexeGroup partialUpdatedAnnexeGroup = new AnnexeGroup();
        partialUpdatedAnnexeGroup.setId(annexeGroup.getId());

        partialUpdatedAnnexeGroup.nomAnnexe(UPDATED_NOM_ANNEXE);

        restAnnexeGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnexeGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnexeGroup))
            )
            .andExpect(status().isOk());

        // Validate the AnnexeGroup in the database
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeUpdate);
        AnnexeGroup testAnnexeGroup = annexeGroupList.get(annexeGroupList.size() - 1);
        assertThat(testAnnexeGroup.getNomAnnexe()).isEqualTo(UPDATED_NOM_ANNEXE);
    }

    @Test
    void fullUpdateAnnexeGroupWithPatch() throws Exception {
        // Initialize the database
        annexeGroupRepository.save(annexeGroup);

        int databaseSizeBeforeUpdate = annexeGroupRepository.findAll().size();

        // Update the annexeGroup using partial update
        AnnexeGroup partialUpdatedAnnexeGroup = new AnnexeGroup();
        partialUpdatedAnnexeGroup.setId(annexeGroup.getId());

        partialUpdatedAnnexeGroup.nomAnnexe(UPDATED_NOM_ANNEXE);

        restAnnexeGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnexeGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnexeGroup))
            )
            .andExpect(status().isOk());

        // Validate the AnnexeGroup in the database
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeUpdate);
        AnnexeGroup testAnnexeGroup = annexeGroupList.get(annexeGroupList.size() - 1);
        assertThat(testAnnexeGroup.getNomAnnexe()).isEqualTo(UPDATED_NOM_ANNEXE);
    }

    @Test
    void patchNonExistingAnnexeGroup() throws Exception {
        int databaseSizeBeforeUpdate = annexeGroupRepository.findAll().size();
        annexeGroup.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnexeGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, annexeGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(annexeGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexeGroup in the database
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAnnexeGroup() throws Exception {
        int databaseSizeBeforeUpdate = annexeGroupRepository.findAll().size();
        annexeGroup.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(annexeGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexeGroup in the database
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAnnexeGroup() throws Exception {
        int databaseSizeBeforeUpdate = annexeGroupRepository.findAll().size();
        annexeGroup.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexeGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(annexeGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnexeGroup in the database
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAnnexeGroup() throws Exception {
        // Initialize the database
        annexeGroupRepository.save(annexeGroup);

        int databaseSizeBeforeDelete = annexeGroupRepository.findAll().size();

        // Delete the annexeGroup
        restAnnexeGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, annexeGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnnexeGroup> annexeGroupList = annexeGroupRepository.findAll();
        assertThat(annexeGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

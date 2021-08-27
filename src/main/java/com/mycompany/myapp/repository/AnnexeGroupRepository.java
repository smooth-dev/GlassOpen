package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AnnexeGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AnnexeGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnexeGroupRepository extends MongoRepository<AnnexeGroup, String> {
    AnnexeGroup findByNomAnnexe(String nom);
}

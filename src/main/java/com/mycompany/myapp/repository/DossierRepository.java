package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Dossier;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Dossier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DossierRepository extends MongoRepository<Dossier, String> {
    @Query("{'produit.id': ?0}")
    List<Dossier> findAllById(String id);

    Optional<Dossier> findAllByNumeroDossier(String id);
}

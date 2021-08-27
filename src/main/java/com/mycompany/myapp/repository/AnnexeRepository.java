package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Annexe;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Annexe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnexeRepository extends MongoRepository<Annexe, String> {
    @Query("{'produit.id': ?0}")
    List<Annexe> findAllById(String id);
}

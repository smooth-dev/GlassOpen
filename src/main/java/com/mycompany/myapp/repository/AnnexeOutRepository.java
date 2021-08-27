package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AnnexeOut;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AnnexeOut entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnexeOutRepository extends MongoRepository<AnnexeOut, String> {
    @Query("{'annexegroup.produit.id': ?0}")
    List<AnnexeOut> findAllById(String id);
}

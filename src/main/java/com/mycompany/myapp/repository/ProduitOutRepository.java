package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProduitOut;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ProduitOut entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProduitOutRepository extends MongoRepository<ProduitOut, String> {}

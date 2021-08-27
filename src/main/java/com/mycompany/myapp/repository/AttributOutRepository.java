package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AttributOut;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AttributOut entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributOutRepository extends MongoRepository<AttributOut, String> {}

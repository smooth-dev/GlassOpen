package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AttributClient;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AttributClient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributClientRepository extends MongoRepository<AttributClient, String> {
    @Query("{'client.id': ?0}")
    List<AttributClient> findAllById(String id);

    List<AttributClient> findAllByNomAttribut(String id);
}

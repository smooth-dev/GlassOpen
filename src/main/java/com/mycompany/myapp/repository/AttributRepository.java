package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Attribut;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Attribut entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributRepository extends MongoRepository<Attribut, String> {
    @Query("{'annexe.id': ?0}")
    List<Attribut> findAllById(String id);
}

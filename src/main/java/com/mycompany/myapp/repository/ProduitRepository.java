package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Produit;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Produit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProduitRepository extends MongoRepository<Produit, String> {
    Produit findByNomProduit(String nom);
    Optional<Produit> findAllById(String nom);
}

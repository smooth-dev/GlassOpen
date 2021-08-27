package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ProduitOut.
 */
@Document(collection = "produit_out")
public class ProduitOut implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nom_annexe")
    private String nomAnnexe;

    @DBRef
    @Field("produit")
    @JsonIgnoreProperties(value = { "dossiers", "annexes", "annexegroups", "produitouts" }, allowSetters = true)
    private Produit produit;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProduitOut id(String id) {
        this.id = id;
        return this;
    }

    public String getNomAnnexe() {
        return this.nomAnnexe;
    }

    public ProduitOut nomAnnexe(String nomAnnexe) {
        this.nomAnnexe = nomAnnexe;
        return this;
    }

    public void setNomAnnexe(String nomAnnexe) {
        this.nomAnnexe = nomAnnexe;
    }

    public Produit getProduit() {
        return this.produit;
    }

    public ProduitOut produit(Produit produit) {
        this.setProduit(produit);
        return this;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProduitOut)) {
            return false;
        }
        return id != null && id.equals(((ProduitOut) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProduitOut{" +
            "id=" + getId() +
            ", nomAnnexe='" + getNomAnnexe() + "'" +
            "}";
    }
}

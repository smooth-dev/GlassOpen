package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Annexe.
 */
@Document(collection = "annexe")
public class Annexe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nom_annexe")
    private String nomAnnexe;

    @NotNull
    @Field("entete_annexe")
    private String enteteAnnexe;

    @DBRef
    @Field("attribut")
    @JsonIgnoreProperties(value = { "annexe" }, allowSetters = true)
    private Set<Attribut> attributs = new HashSet<>();

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

    public Annexe id(String id) {
        this.id = id;
        return this;
    }

    public String getNomAnnexe() {
        return this.nomAnnexe;
    }

    public Annexe nomAnnexe(String nomAnnexe) {
        this.nomAnnexe = nomAnnexe;
        return this;
    }

    public void setNomAnnexe(String nomAnnexe) {
        this.nomAnnexe = nomAnnexe;
    }

    public String getEnteteAnnexe() {
        return this.enteteAnnexe;
    }

    public Annexe enteteAnnexe(String enteteAnnexe) {
        this.enteteAnnexe = enteteAnnexe;
        return this;
    }

    public void setEnteteAnnexe(String enteteAnnexe) {
        this.enteteAnnexe = enteteAnnexe;
    }

    public Set<Attribut> getAttributs() {
        return this.attributs;
    }

    public Annexe attributs(Set<Attribut> attributs) {
        this.setAttributs(attributs);
        return this;
    }

    public Annexe addAttribut(Attribut attribut) {
        this.attributs.add(attribut);
        attribut.setAnnexe(this);
        return this;
    }

    public Annexe removeAttribut(Attribut attribut) {
        this.attributs.remove(attribut);
        attribut.setAnnexe(null);
        return this;
    }

    public void setAttributs(Set<Attribut> attributs) {
        if (this.attributs != null) {
            this.attributs.forEach(i -> i.setAnnexe(null));
        }
        if (attributs != null) {
            attributs.forEach(i -> i.setAnnexe(this));
        }
        this.attributs = attributs;
    }

    public Produit getProduit() {
        return this.produit;
    }

    public Annexe produit(Produit produit) {
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
        if (!(o instanceof Annexe)) {
            return false;
        }
        return id != null && id.equals(((Annexe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Annexe{" +
            "id=" + getId() +
            ", nomAnnexe='" + getNomAnnexe() + "'" +
            ", enteteAnnexe='" + getEnteteAnnexe() + "'" +
            "}";
    }
}

package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Attribut.
 */
@Document(collection = "attribut")
public class Attribut implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nom_attribut")
    private String nomAttribut;

    @DBRef
    @Field("annexe")
    @JsonIgnoreProperties(value = { "attributs", "produit" }, allowSetters = true)
    private Annexe annexe;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Attribut id(String id) {
        this.id = id;
        return this;
    }

    public String getNomAttribut() {
        return this.nomAttribut;
    }

    public Attribut nomAttribut(String nomAttribut) {
        this.nomAttribut = nomAttribut;
        return this;
    }

    public void setNomAttribut(String nomAttribut) {
        this.nomAttribut = nomAttribut;
    }

    public Annexe getAnnexe() {
        return this.annexe;
    }

    public Attribut annexe(Annexe annexe) {
        this.setAnnexe(annexe);
        return this;
    }

    public void setAnnexe(Annexe annexe) {
        this.annexe = annexe;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attribut)) {
            return false;
        }
        return id != null && id.equals(((Attribut) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attribut{" +
            "id=" + getId() +
            ", nomAttribut='" + getNomAttribut() + "'" +
            "}";
    }
}

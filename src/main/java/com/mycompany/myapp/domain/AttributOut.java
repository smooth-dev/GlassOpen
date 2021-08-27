package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A AttributOut.
 */
@Document(collection = "attribut_out")
public class AttributOut implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nom_attribut")
    private String nomAttribut;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttributOut id(String id) {
        this.id = id;
        return this;
    }

    public String getNomAttribut() {
        return this.nomAttribut;
    }

    public AttributOut nomAttribut(String nomAttribut) {
        this.nomAttribut = nomAttribut;
        return this;
    }

    public void setNomAttribut(String nomAttribut) {
        this.nomAttribut = nomAttribut;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributOut)) {
            return false;
        }
        return id != null && id.equals(((AttributOut) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttributOut{" +
            "id=" + getId() +
            ", nomAttribut='" + getNomAttribut() + "'" +
            "}";
    }
}

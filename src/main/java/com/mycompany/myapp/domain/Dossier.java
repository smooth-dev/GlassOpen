package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Dossier.
 */
@Document(collection = "dossier")
public class Dossier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("numero_dossier")
    private String numeroDossier;

    @DBRef
    @Field("client")
    @JsonIgnoreProperties(value = { "dossiers", "attributclients" }, allowSetters = true)
    private Client client;

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

    public Dossier id(String id) {
        this.id = id;
        return this;
    }

    public String getNumeroDossier() {
        return this.numeroDossier;
    }

    public Dossier numeroDossier(String numeroDossier) {
        this.numeroDossier = numeroDossier;
        return this;
    }

    public void setNumeroDossier(String numeroDossier) {
        this.numeroDossier = numeroDossier;
    }

    public Client getClient() {
        return this.client;
    }

    public Dossier client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Produit getProduit() {
        return this.produit;
    }

    public Dossier produit(Produit produit) {
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
        if (!(o instanceof Dossier)) {
            return false;
        }
        return id != null && id.equals(((Dossier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dossier{" +
            "id=" + getId() +
            ", numeroDossier='" + getNumeroDossier() + "'" +
            "}";
    }
}

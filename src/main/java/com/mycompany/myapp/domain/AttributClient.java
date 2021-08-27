package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A AttributClient.
 */
@Document(collection = "attribut_client")
public class AttributClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nom_attribut")
    private String nomAttribut;

    // @NotNull
    @Field("contenu")
    private String contenu;

    @DBRef
    @Field("client")
    @JsonIgnoreProperties(value = { "dossiers", "attributclients" }, allowSetters = true)
    private Client client;

    @DBRef
    @Field("annexeout")
    @JsonIgnoreProperties(value = { "attributclients", "annexegroup" }, allowSetters = true)
    private AnnexeOut annexeout;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttributClient id(String id) {
        this.id = id;
        return this;
    }

    public String getNomAttribut() {
        return this.nomAttribut;
    }

    public AttributClient nomAttribut(String nomAttribut) {
        this.nomAttribut = nomAttribut;
        return this;
    }

    public void setNomAttribut(String nomAttribut) {
        this.nomAttribut = nomAttribut;
    }

    public String getContenu() {
        return this.contenu;
    }

    public AttributClient contenu(String contenu) {
        this.contenu = contenu;
        return this;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Client getClient() {
        return this.client;
    }

    public AttributClient client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public AnnexeOut getAnnexeout() {
        return this.annexeout;
    }

    public AttributClient annexeout(AnnexeOut annexeOut) {
        this.setAnnexeout(annexeOut);
        return this;
    }

    public void setAnnexeout(AnnexeOut annexeOut) {
        this.annexeout = annexeOut;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributClient)) {
            return false;
        }
        return id != null && id.equals(((AttributClient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    //    // prettier-ignore
//    @Override
//    public String toString() {
//        return "AttributClient{" +
//            "id=" + getId() +
//            ", nomAttribut='" + getNomAttribut() + "'" +
//            ", contenu='" + getContenu() + "'" +
//            "}";
//    }
    
    // prettier-ignore
    @Override
    public String toString() {
        return "AttributClient{" +
            "id=" + getId() +
            "}";
    }
}

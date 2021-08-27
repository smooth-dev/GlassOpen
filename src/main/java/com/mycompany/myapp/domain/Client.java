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
 * A Client.
 */
@Document(collection = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nom_client")
    private String nomClient;

    @NotNull
    @Field("prenom_client")
    private String prenomClient;

    @NotNull
    @Field("cin")
    private String cin;

    @NotNull
    @Field("adresse")
    private String adresse;

    @DBRef
    @Field("dossier")
    @JsonIgnoreProperties(value = { "client", "produit" }, allowSetters = true)
    private Set<Dossier> dossiers = new HashSet<>();

    @DBRef
    @Field("attributclient")
    @JsonIgnoreProperties(value = { "client", "annexeout" }, allowSetters = true)
    private Set<AttributClient> attributclients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Client id(String id) {
        this.id = id;
        return this;
    }

    public String getNomClient() {
        return this.nomClient;
    }

    public Client nomClient(String nomClient) {
        this.nomClient = nomClient;
        return this;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getPrenomClient() {
        return this.prenomClient;
    }

    public Client prenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
        return this;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getCin() {
        return this.cin;
    }

    public Client cin(String cin) {
        this.cin = cin;
        return this;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Client adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Set<Dossier> getDossiers() {
        return this.dossiers;
    }

    public Client dossiers(Set<Dossier> dossiers) {
        this.setDossiers(dossiers);
        return this;
    }

    public Client addDossier(Dossier dossier) {
        this.dossiers.add(dossier);
        dossier.setClient(this);
        return this;
    }

    public Client removeDossier(Dossier dossier) {
        this.dossiers.remove(dossier);
        dossier.setClient(null);
        return this;
    }

    public void setDossiers(Set<Dossier> dossiers) {
        if (this.dossiers != null) {
            this.dossiers.forEach(i -> i.setClient(null));
        }
        if (dossiers != null) {
            dossiers.forEach(i -> i.setClient(this));
        }
        this.dossiers = dossiers;
    }

    public Set<AttributClient> getAttributclients() {
        return this.attributclients;
    }

    public Client attributclients(Set<AttributClient> attributClients) {
        this.setAttributclients(attributClients);
        return this;
    }

    public Client addAttributclient(AttributClient attributClient) {
        this.attributclients.add(attributClient);
        attributClient.setClient(this);
        return this;
    }

    public Client removeAttributclient(AttributClient attributClient) {
        this.attributclients.remove(attributClient);
        attributClient.setClient(null);
        return this;
    }

    public void setAttributclients(Set<AttributClient> attributClients) {
        if (this.attributclients != null) {
            this.attributclients.forEach(i -> i.setClient(null));
        }
        if (attributClients != null) {
            attributClients.forEach(i -> i.setClient(this));
        }
        this.attributclients = attributClients;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", nomClient='" + getNomClient() + "'" +
            ", prenomClient='" + getPrenomClient() + "'" +
            ", cin='" + getCin() + "'" +
            ", adresse='" + getAdresse() + "'" +
            "}";
    }
}

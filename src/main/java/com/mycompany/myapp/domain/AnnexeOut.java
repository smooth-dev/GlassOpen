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
 * A AnnexeOut.
 */
@Document(collection = "annexe_out")
public class AnnexeOut implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nom_annexe")
    private String nomAnnexe;

    @NotNull
    @Field("id_client")
    private String idClient;

    @DBRef
    @Field("attributclient")
    @JsonIgnoreProperties(value = { "client", "annexeout" }, allowSetters = true)
    private Set<AttributClient> attributclients = new HashSet<>();

    @DBRef
    @Field("annexegroup")
    @JsonIgnoreProperties(value = { "annexeouts", "produit" }, allowSetters = true)
    private AnnexeGroup annexegroup;

    public AnnexeOut() {}

    public AnnexeOut(@NotNull String nomAnnexe, @NotNull String idClient, Set<AttributClient> attributclients, AnnexeGroup annexegroup) {
        this.nomAnnexe = nomAnnexe;
        this.idClient = idClient;
        this.attributclients = attributclients;
        this.annexegroup = annexegroup;
    }

    public AnnexeOut(@NotNull String nomAnnexe, @NotNull String idClient, Set<AttributClient> attributclients) {
        this.nomAnnexe = nomAnnexe;
        this.idClient = idClient;
        this.attributclients = attributclients;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AnnexeOut id(String id) {
        this.id = id;
        return this;
    }

    public String getNomAnnexe() {
        return this.nomAnnexe;
    }

    public AnnexeOut nomAnnexe(String nomAnnexe) {
        this.nomAnnexe = nomAnnexe;
        return this;
    }

    public void setNomAnnexe(String nomAnnexe) {
        this.nomAnnexe = nomAnnexe;
    }

    public String getIdClient() {
        return this.idClient;
    }

    public AnnexeOut idClient(String idClient) {
        this.idClient = idClient;
        return this;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public Set<AttributClient> getAttributclients() {
        return this.attributclients;
    }

    public AnnexeOut attributclients(Set<AttributClient> attributClients) {
        this.setAttributclients(attributClients);
        return this;
    }

    public AnnexeOut addAttributclient(AttributClient attributClient) {
        this.attributclients.add(attributClient);
        attributClient.setAnnexeout(this);
        return this;
    }

    public AnnexeOut removeAttributclient(AttributClient attributClient) {
        this.attributclients.remove(attributClient);
        attributClient.setAnnexeout(null);
        return this;
    }

    public void setAttributclients(Set<AttributClient> attributClients) {
        if (this.attributclients != null) {
            this.attributclients.forEach(i -> i.setAnnexeout(null));
        }
        if (attributClients != null) {
            attributClients.forEach(i -> i.setAnnexeout(this));
        }
        this.attributclients = attributClients;
    }

    public AnnexeGroup getAnnexegroup() {
        return this.annexegroup;
    }

    public AnnexeOut annexegroup(AnnexeGroup annexeGroup) {
        this.setAnnexegroup(annexeGroup);
        return this;
    }

    public void setAnnexegroup(AnnexeGroup annexeGroup) {
        this.annexegroup = annexeGroup;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnexeOut)) {
            return false;
        }
        return id != null && id.equals(((AnnexeOut) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
//    @Override
//    public String toString() {
//        return "AnnexeOut{" +
//            "id=" + getId() +
//            ", nomAnnexe='" + getNomAnnexe() + "'" +
//            ", idClient='" + getIdClient() + "'" + getAttributclients()+
//            "}";
//    }
    
    // prettier-ignore
    @Override
    public String toString() {
        return "AnnexeOut{" +
            "id=" + getId() +
            ", nomAnnexe='" + getNomAnnexe() +
            "}";
    }
}

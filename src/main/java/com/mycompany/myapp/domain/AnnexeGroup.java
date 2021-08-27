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
 * A AnnexeGroup.
 */
@Document(collection = "annexe_group")
public class AnnexeGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nom_annexe")
    private String nomAnnexe;

    @DBRef
    @Field("annexeout")
    @JsonIgnoreProperties(value = { "annexegroup" }, allowSetters = true)
    private Set<AnnexeOut> annexeouts = new HashSet<>();

    @DBRef
    @Field("produit")
    @JsonIgnoreProperties(value = { "dossiers", "annexes", "annexegroups", "produitouts" }, allowSetters = true)
    private Produit produit;

    public AnnexeGroup() {}

    public AnnexeGroup(String nomAnnexe) {
        this.nomAnnexe = nomAnnexe;
    }

    public AnnexeGroup(String nomAnnexe, Produit produit) {
        this.nomAnnexe = nomAnnexe;
        this.produit = produit;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AnnexeGroup id(String id) {
        this.id = id;
        return this;
    }

    public String getNomAnnexe() {
        return this.nomAnnexe;
    }

    public AnnexeGroup nomAnnexe(String nomAnnexe) {
        this.nomAnnexe = nomAnnexe;
        return this;
    }

    public void setNomAnnexe(String nomAnnexe) {
        this.nomAnnexe = nomAnnexe;
    }

    public Set<AnnexeOut> getAnnexeouts() {
        return this.annexeouts;
    }

    public AnnexeGroup annexeouts(Set<AnnexeOut> annexeOuts) {
        this.setAnnexeouts(annexeOuts);
        return this;
    }

    public AnnexeGroup addAnnexeout(AnnexeOut annexeOut) {
        this.annexeouts.add(annexeOut);
        annexeOut.setAnnexegroup(this);
        return this;
    }

    public AnnexeGroup removeAnnexeout(AnnexeOut annexeOut) {
        this.annexeouts.remove(annexeOut);
        annexeOut.setAnnexegroup(null);
        return this;
    }

    public void setAnnexeouts(Set<AnnexeOut> annexeOuts) {
        if (this.annexeouts != null) {
            this.annexeouts.forEach(i -> i.setAnnexegroup(null));
        }
        if (annexeOuts != null) {
            annexeOuts.forEach(i -> i.setAnnexegroup(this));
        }
        this.annexeouts = annexeOuts;
    }

    public Produit getProduit() {
        return this.produit;
    }

    public AnnexeGroup produit(Produit produit) {
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
        if (!(o instanceof AnnexeGroup)) {
            return false;
        }
        return id != null && id.equals(((AnnexeGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    //    // prettier-ignore
//    @Override
//    public String toString() {
//        return "AnnexeGroup{" +
//            "id=" + getId() +
//            ", nomAnnexe='" + getNomAnnexe() + "'" + getAnnexeouts() +
//            "}";
//    }
    
    // prettier-ignore
    @Override
    public String toString() {
        return "AnnexeGroup{" +
            "id=" + getId() +
            ", nomAnnexe='" + getNomAnnexe() +
            "}";
    }
}

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
 * A Produit.
 */
@Document(collection = "produit")
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("nom_produit")
    private String nomProduit;

    @DBRef
    @Field("dossier")
    @JsonIgnoreProperties(value = { "client", "produit" }, allowSetters = true)
    private Set<Dossier> dossiers = new HashSet<>();

    @DBRef
    @Field("annexe")
    @JsonIgnoreProperties(value = { "attributs", "produit" }, allowSetters = true)
    private Set<Annexe> annexes = new HashSet<>();

    @DBRef
    @Field("annexegroup")
    @JsonIgnoreProperties(value = { "produit" }, allowSetters = true)
    private Set<AnnexeGroup> annexegroups = new HashSet<>();

    @DBRef
    @Field("produitout")
    @JsonIgnoreProperties(value = { "produit" }, allowSetters = true)
    private Set<ProduitOut> produitouts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Produit id(String id) {
        this.id = id;
        return this;
    }

    public String getNomProduit() {
        return this.nomProduit;
    }

    public Produit nomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
        return this;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Set<Dossier> getDossiers() {
        return this.dossiers;
    }

    public Produit dossiers(Set<Dossier> dossiers) {
        this.setDossiers(dossiers);
        return this;
    }

    public Produit addDossier(Dossier dossier) {
        this.dossiers.add(dossier);
        dossier.setProduit(this);
        return this;
    }

    public Produit removeDossier(Dossier dossier) {
        this.dossiers.remove(dossier);
        dossier.setProduit(null);
        return this;
    }

    public void setDossiers(Set<Dossier> dossiers) {
        if (this.dossiers != null) {
            this.dossiers.forEach(i -> i.setProduit(null));
        }
        if (dossiers != null) {
            dossiers.forEach(i -> i.setProduit(this));
        }
        this.dossiers = dossiers;
    }

    public Set<Annexe> getAnnexes() {
        return this.annexes;
    }

    public Produit annexes(Set<Annexe> annexes) {
        this.setAnnexes(annexes);
        return this;
    }

    public Produit addAnnexe(Annexe annexe) {
        this.annexes.add(annexe);
        annexe.setProduit(this);
        return this;
    }

    public Produit removeAnnexe(Annexe annexe) {
        this.annexes.remove(annexe);
        annexe.setProduit(null);
        return this;
    }

    public void setAnnexes(Set<Annexe> annexes) {
        if (this.annexes != null) {
            this.annexes.forEach(i -> i.setProduit(null));
        }
        if (annexes != null) {
            annexes.forEach(i -> i.setProduit(this));
        }
        this.annexes = annexes;
    }

    public Set<AnnexeGroup> getAnnexegroups() {
        return this.annexegroups;
    }

    public Produit annexegroups(Set<AnnexeGroup> annexeGroups) {
        this.setAnnexegroups(annexeGroups);
        return this;
    }

    public Produit addAnnexegroup(AnnexeGroup annexeGroup) {
        this.annexegroups.add(annexeGroup);
        annexeGroup.setProduit(this);
        return this;
    }

    public Produit removeAnnexegroup(AnnexeGroup annexeGroup) {
        this.annexegroups.remove(annexeGroup);
        annexeGroup.setProduit(null);
        return this;
    }

    public void setAnnexegroups(Set<AnnexeGroup> annexeGroups) {
        if (this.annexegroups != null) {
            this.annexegroups.forEach(i -> i.setProduit(null));
        }
        if (annexeGroups != null) {
            annexeGroups.forEach(i -> i.setProduit(this));
        }
        this.annexegroups = annexeGroups;
    }

    public Set<ProduitOut> getProduitouts() {
        return this.produitouts;
    }

    public Produit produitouts(Set<ProduitOut> produitOuts) {
        this.setProduitouts(produitOuts);
        return this;
    }

    public Produit addProduitout(ProduitOut produitOut) {
        this.produitouts.add(produitOut);
        produitOut.setProduit(this);
        return this;
    }

    public Produit removeProduitout(ProduitOut produitOut) {
        this.produitouts.remove(produitOut);
        produitOut.setProduit(null);
        return this;
    }

    public void setProduitouts(Set<ProduitOut> produitOuts) {
        if (this.produitouts != null) {
            this.produitouts.forEach(i -> i.setProduit(null));
        }
        if (produitOuts != null) {
            produitOuts.forEach(i -> i.setProduit(this));
        }
        this.produitouts = produitOuts;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Produit)) {
            return false;
        }
        return id != null && id.equals(((Produit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Produit{" +
            "id=" + getId() +
            ", nomProduit='" + getNomProduit() +
            "}";
    }
}

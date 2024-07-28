package com.ocp.demo1;

import org.w3c.dom.Text;

public class PiecesSearch {
    String code;
    String reference; // Make sure the name is correctly spelled here
    String nom;
    String unité;
    String emplacement;
    Integer nombre;
    String description;
    String type;

    public PiecesSearch(String code, String reference, String nom, Integer nombre, String description, String type,String emplacement,String unité) {
        this.code = code;
        this.reference = reference; // Ensure parameter names match field names
        this.type=type;
        this.unité=unité;
        this.emplacement=emplacement;
        this.nom = nom;
        this.nombre = nombre;
        this.description = description;
    }

    // Check getters and setters for correct spelling and return types

    public String getCode() {
        return code;
    }
    public String getEmplacement() {
        return emplacement;
    }
    public String getUnité() {
        return unité;
    }

    public String getReference() {
        return reference;
    }

    public String getNom() {
        return nom;
    }

    public Integer getNombre() {
        return nombre;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setReference(String reference) { // Typo was in earlier code as 'refrence'
        this.reference = reference;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUnité(String unité) {
        this.unité = unité;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }
}

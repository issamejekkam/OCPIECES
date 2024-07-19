package com.ocp.demo1;
public class PiecesSearch {
    String code;
    String reference; // Make sure the name is correctly spelled here
    String nom;
    Integer nombre;
    String description;
    String type;

    public PiecesSearch(String code, String reference, String nom, Integer nombre, String description, String type) {
        this.code = code;
        this.reference = reference; // Ensure parameter names match field names
        this.type=type;
        this.nom = nom;
        this.nombre = nombre;
        this.description = description;
    }

    // Check getters and setters for correct spelling and return types
    public String getCode() {
        return code;
    }

    public String getReference() { // Must match the name used in PropertyValueFactory
        return reference;
    }

    public String getNom() { // Must match the name used in PropertyValueFactory
        return nom;
    }
    public String getType(){
        return type;
    }

    public Integer getNombre() {
        return nombre;
    }

    public String getDescription() {
        return description;
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
}

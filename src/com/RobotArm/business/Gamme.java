package com.RobotArm.business;

import java.util.ArrayList;

/**
 * Classe représentant une gamme
 *
 * @author Alvin
 */
public class Gamme {
    String id;
    String description;
    ArrayList<Operation> listeOperations;

    public Gamme(String i, String d) {
        this.id = i;
        this.description = d;
        this.listeOperations = new ArrayList<>();
    }


    public Gamme() {
    }


    public void AjouterOperation(Operation o) {
        this.listeOperations.add(o);
    }


    public void SupprimerOperation(Operation o) throws Exception {
        if (this.listeOperations.contains(o)) {
            this.listeOperations.remove(o);
        } else {
            throw new Exception("Cette operation n'est pas presente dans la gamme.");
        }
    }

    public String getId() {
        return this.id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getDescription() {
        return this.description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public ArrayList<Operation> getListeOperations() {
        return this.listeOperations;
    }

    public void setListeOperations(ArrayList<Operation> listeOperations) { this.listeOperations = listeOperations; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gamme gamme = (Gamme) o;
        return id.equals(gamme.id);
    }

}


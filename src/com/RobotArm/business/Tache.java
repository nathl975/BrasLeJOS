package com.RobotArm.business;

import com.RobotArm.enumeration.TypeAction;

/*
 * Classe représentant une tâche
 * @author Alvin
 *
 */
public class Tache {
    private String id;
    private String description;
    private TypeAction typeAction;

    public Tache(String id, String description, TypeAction typeAction) {
        this.id = id;
        this.description = description;
        this.typeAction = typeAction;
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

    public TypeAction getTypeAction() {
        return this.typeAction;
    }

    public void setTypeAction(TypeAction typeAction) {
        this.typeAction = typeAction;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tache tache = (Tache) o;
        return id.equals(tache.id);
    }

}


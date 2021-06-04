 package com.RobotArm.business;

import java.util.ArrayList;



public class Operation
{
  private int id;
  private String description;
  ArrayList<Tache> listeTaches;
  
  public Operation() {
    this.listeTaches = new ArrayList<>();
  }

  
  public Operation(int id, String description) {
    this.id = id;
    this.description = description;
    this.listeTaches = new ArrayList<>();
  }


  
  public void AjouterTache(Tache t) {
    this.listeTaches.add(t);
  }


  
  public void SupprimerTache(Tache t) {
    this.listeTaches.remove(t);
  }


  
  public void executer() {
    for (Tache t : this.listeTaches)
    {
      t.executer();
    }
  }

  
  public int getId() {
    return this.id;
  }

  
  public void setId(int id) {
    this.id = id;
  }

  
  public String getDescription() {
    return this.description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }

  
  public ArrayList<Tache> getListeTaches() {
    return this.listeTaches;
  }
}

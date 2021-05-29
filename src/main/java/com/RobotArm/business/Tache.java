 package com.RobotArm.business;

import lejos.utility.Delay;

public class Tache { private int id;
   private String description;
   private TypeAction typeAction;
   int valeur;
   Moteur moteur;
   
   public enum TypeAction { Attendre, Tourner; }

   public Tache(TypeAction ta, int v) {
     setTypeAction(ta);
     this.valeur = v;
   }
 
   
   public Tache(int id, String description, TypeAction typeAction, int valeur, char moteur) {}
 
   
   public void executer() {
     switch (getTypeAction()) {
       case Attendre:
         Delay.msDelay(this.valeur);
         break;
       case Tourner:
         this.moteur.tourner(this.valeur);
         break;
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
   
   public TypeAction getTypeAction() {
     return this.typeAction;
   }
   
   public void setTypeAction(TypeAction typeAction) {
     this.typeAction = typeAction;
   } }


 package com.RobotArm.business;
 
 import java.util.ArrayList; 
 
 public class Gamme
 {
   String id;
   String description;
   ArrayList<Operation> listeOperations;
   
   public Gamme(String i, String d) {
     this.id = i;
     this.description = d;
     this.listeOperations = new ArrayList<>();
   }
 
   
   public Gamme() {}
 
 
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
 
   
   public void executer() {
     for (Operation o : this.listeOperations)
     {
       o.executer();
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
 }

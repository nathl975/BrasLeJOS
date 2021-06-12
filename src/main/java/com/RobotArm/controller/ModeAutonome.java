 package com.RobotArm.controller;
 
import com.RobotArm.interfaces.IEtatMode;
 
 /**
	* Mode manuel qui autorise l'exécution et l'automatisme
	* @author Alvin
	*
	*/
 public class ModeAutonome
	 implements IEtatMode
 {
	 public boolean peutExecuter() {
		 return true;
	 }
	 public boolean estAutonome() {
		 return true;
	 }
 }

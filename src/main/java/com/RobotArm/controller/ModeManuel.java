 package com.RobotArm.controller;
 
 import com.RobotArm.interfaces.IEtatMode;
 
 /**
	* Mode manuel qui autorise l'exécution, mais pas l'automatisme
	* @author Alvin
	*
	*/
 public class ModeManuel implements IEtatMode
 {
	 public boolean peutExecuter() {
		 return true;
	 }
	 public boolean estAutonome() {
		 return false;
	 }
 }

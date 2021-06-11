 package com.RobotArm.controller;
 
 import com.RobotArm.interfaces.IEtatMode;

/**
 * Mode panne du robot, qui interdit l'exécution 
 * @author Alvin
 *
 */
 public class ModePanne implements IEtatMode
 {
	 public boolean peutExecuter() {
		 return false;
	 }
	 public boolean estAutonome() {
		 return false;
	 }
 }

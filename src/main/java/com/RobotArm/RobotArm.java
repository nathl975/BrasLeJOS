package com.RobotArm;

import lejos.hardware.*;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.*;
import lejos.hardware.port.*;
import lejos.utility.Delay;

import java.io.IOException;
import java.lang.Thread;
import java.sql.SQLException;

import com.RobotArm.controller.*;
import com.RobotArm.interfaces.*;
import com.RobotArm.persistance.*;

public class RobotArm {	
	private static IPersistance persistance;
	private static IPilotage pilotage;
	private static Controleur controleur;
	
	public static void main(String[] args) {
		try {
			persistance = new ThreadPersistance();
			controleur = new Controleur(persistance, pilotage);			
		} catch (IOException e) {
			System.out.println("Une erreur bloquane est survenue, impossible de démarrer !");
			e.printStackTrace();			
		} catch (SQLException e) {
			System.out.println("Une erreur SQL bloquante est survenue, impossible de démarrer !");
			e.printStackTrace();			
		} catch(Exception e) {
			System.out.println("Une erreur inconnue est survenue, impossible de démarrer !");
			e.printStackTrace();			
		}
		Delay.msDelay(3000);
		System.out.println("Fin du programme");
	}
}

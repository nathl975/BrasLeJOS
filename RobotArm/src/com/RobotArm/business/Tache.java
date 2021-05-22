package com.RobotArm.business;

import lejos.utility.Delay;

public class Tache {
	public enum TypeAction {
		Attendre, Tourner
	}

	private TypeAction typeAction;
	int valeur;
	Moteur moteur;

	public Tache(TypeAction ta, int v) {
		this.setTypeAction(ta);
		this.valeur = v;
	}

	public void executer() {
		switch (getTypeAction()) {
		case Attendre:
			Delay.msDelay(this.valeur);
			break;
		case Tourner:
			moteur.tourner(this.valeur);
			break;
		}
	}

	public TypeAction getTypeAction() {
		return typeAction;
	}

	public void setTypeAction(TypeAction typeAction) {
		this.typeAction = typeAction;
	}
}
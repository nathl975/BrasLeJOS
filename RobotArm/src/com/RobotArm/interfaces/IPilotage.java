package com.RobotArm.interfaces;

import java.util.ArrayList;

public interface IPilotage
{
	public void afficherEtatSysteme();
	
	public void afficherHistorique(ArrayList<String> rapports);
	
	public void envoyerMessage(String message);
}
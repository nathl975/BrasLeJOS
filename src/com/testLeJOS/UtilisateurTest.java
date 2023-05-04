package com.testLeJOS;

import com.RobotArm.business.Utilisateur;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilisateurTest {

	@Test
	/*
	 * On vérifie qu'un utilisateur définit comme admin l'est bien
	 */
	public void isAdmin() {
		Utilisateur user = new Utilisateur("test","test",true);
		
		assertTrue(user.isAdmin());
	}
	
	@Test
	public void isNotAdmin() {
		Utilisateur user = new Utilisateur("test","test",true);
		
		assertFalse(user.isAdmin());
	}
}

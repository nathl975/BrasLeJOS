package com.testLeJOS;

import static org.junit.Assert.*;

import org.junit.Test;

import com.RobotArm.business.Gamme;
import com.RobotArm.business.Operation;

public class GammeTest {

	@Test
	/**
	 * On vérifie que l'ajout d'une opération à une gamme s'effectue correctement
	 */
	public void isOperationAjoutee() {
		Gamme g = new Gamme("1","Gamme test");
		Operation o = new Operation("1","Operation test");
		
		g.AjouterOperation(o);
		
		assertTrue(g.getListeOperations().contains(o));
	}
	
	@Test
	/**
	 * On vérifie que la suppresion d'une opération dans une gamme s'effectue correctement
	 */
	public void isOperationSupprimee() throws Exception {
		Gamme g = new Gamme("1","Gamme test");
		Operation o1 = new Operation("1","Operation test 1");
		Operation o2 = new Operation("2","Operation test 2");
		
		g.AjouterOperation(o1);
		g.AjouterOperation(o2);
		
		g.SupprimerOperation(o1);
		
		assertFalse(g.getListeOperations().contains(o1));
	}

}

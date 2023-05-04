package com.testLeJOS;

import com.RobotArm.business.Operation;
import com.RobotArm.business.Tache;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OperationTest {

	@Test
	/**
	 * On vérifie que l'ajout d'une tache é une opération s'effectue correctement
	 */
	public void isTacheAjoutee() {
		Operation o = new Operation("1","Operation test");
		Tache t = new Tache("1","Tache test",0);
		
		o.AjouterTache(t);
		
		assertTrue(o.getListeTaches().contains(t));
	}
	
	@Test
	/**
	 * On vérifie que la suppression d'une téche é une opération s'effectue correctement
	 */
	public void isTacheSupprimee() {
		Operation o = new Operation("1","Operation test");
		Tache t1 = new Tache("1","Tache test 1",0);
		Tache t2 = new Tache("2","Tache test 2",0);
		
		o.AjouterTache(t1);
		o.AjouterTache(t2);
		
		o.SupprimerTache(t1);
		
		assertFalse(o.getListeTaches().contains(t1));
	}

}

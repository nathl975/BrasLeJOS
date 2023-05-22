package com.testLeJOS;

import com.RobotArm.business.Operation;
import com.RobotArm.business.Tache;
import com.RobotArm.enumeration.TypeAction;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OperationTest {

	@Test
	public void isTacheAjoutee() {
		Operation o = new Operation("1","Operation test");
		Tache t = new Tache("1","Tache test", TypeAction.TournerDroite);

		o.AjouterTache(t);
		
		assertTrue(o.getListeTaches().contains(t));
	}
	
	@Test
	public void isTacheSupprimee() {
		Operation o = new Operation("1","Operation test");
		Tache t1 = new Tache("1","Tache test 1",TypeAction.TournerDroite);
		Tache t2 = new Tache("2","Tache test 2",TypeAction.Attraper);
		
		o.AjouterTache(t1);
		o.AjouterTache(t2);
		
		o.SupprimerTache(t1);
		
		assertFalse(o.getListeTaches().contains(t1));
	}

}

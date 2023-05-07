package com.testLeJOS;

import com.RobotArm.business.Gamme;
import com.RobotArm.business.Operation;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GammeTest {

	@Test
	public void isOperationAjoutee() {
		Gamme g = new Gamme("1","Gamme test");
		Operation o = new Operation("1","Operation test");
		
		g.AjouterOperation(o);
		
		assertTrue(g.getListeOperations().contains(o));
	}
	
	@Test
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

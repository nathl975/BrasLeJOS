package com.RobotArm.interfaces;

/**
 * Interface implémentée par les capteurs du robot
 * @author Alvin
 *
 */
public interface ICapteur {
	/**
	 * @return 1 si le capteur détecte les conditions pour arréter un moteur, 0 sinon
	 */
  int getMesure();
}

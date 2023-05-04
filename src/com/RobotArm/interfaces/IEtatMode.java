package com.RobotArm.interfaces;

/**
 * Interface implémentée par les différents états du robot
 *
 * @author Alvin
 */
public interface IEtatMode {
    /**
     * @return True si l'état autorise d'exécuter une gamme, False sinon
     */
    boolean peutExecuter();

    /**
     * @return True si l'état correspond é une exécution autonmatique, False sinon
     */
    boolean estAutonome();
}

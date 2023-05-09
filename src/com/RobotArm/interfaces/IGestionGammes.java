package com.RobotArm.interfaces;

import com.RobotArm.business.Gamme;
import com.RobotArm.exception.GammeNotFoundException;
import com.RobotArm.exception.UnableToReadGammesException;

import java.io.IOException;
import java.util.ArrayList;

public interface IGestionGammes {
    /**
     * Sauvegarde une gamme dans la persistance
     * @param gamme Gamme à sauvegarder
     *
     */
    void creerGamme(Gamme gamme) throws IOException;

    /**
     * Modifie une gamme dans la persistance
     * @param gamme Gamme à modifier
     *
     */
    void modifierGamme(Gamme gamme);

    /**
     * Supprime une gamme dans la persistance
     * @param id Gamme à supprimer
     */
    void supprimerGamme(String id);

    /**
     * @return Liste des gammes sauvegardées
     */
    ArrayList<Gamme> getGammes() throws UnableToReadGammesException;

    /**
     * @return la gamme possédant l'id recherché
     */
    Gamme findGamme(String id) throws GammeNotFoundException;

    /**
     *
     * @return Retourne la gamme définie par défaut, utilisée pour le mode automatique
     */
    Gamme recupererGammeDefaut();
}

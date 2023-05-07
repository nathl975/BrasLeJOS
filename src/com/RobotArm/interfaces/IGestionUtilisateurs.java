package com.RobotArm.interfaces;

import com.RobotArm.business.Utilisateur;

public interface IGestionUtilisateurs {
    /**
     * Crée un compte dans la persistance
     * @param login Identifiant de l'utilisateur
     * @param pwd Mot de passe chiffré de l'utilisateur
     *
     */
    void creerCompte(String login, String pwd);

    /**
     * @param l Identifiant de l'utilisateur
     * @param p Mot de passe
     * @return Utilisateur trouvé ou null
     */
    Utilisateur trouverCompte(String l, String p);

    /**
     * Supprime un compte utilisateur
     * @param login Identifiant du compte à supprimer
     *
     */
    void supprimerCompte(String login);
}

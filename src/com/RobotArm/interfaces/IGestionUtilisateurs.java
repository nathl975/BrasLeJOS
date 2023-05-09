package com.RobotArm.interfaces;

import com.RobotArm.business.Utilisateur;
import com.RobotArm.exception.UnableToReadUsersException;
import com.RobotArm.exception.UserNotFoundException;

import java.util.ArrayList;

public interface IGestionUtilisateurs {
    /**
     * Crée un compte dans la persistance
     * @param login Identifiant de l'utilisateur
     * @param pwd Mot de passe chiffré de l'utilisateur
     *
     */
    void createUser(String login, String pwd);

    /**
     * @param l Identifiant de l'utilisateur
     * @param p Mot de passe
     * @return Utilisateur trouvé ou null
     */
    Utilisateur findUser(String l, String p) throws UserNotFoundException;

    /**
     * Supprime un compte utilisateur
     * @param login Identifiant du compte à supprimer
     *
     */
    void supprimerCompte(String login);

    ArrayList<Utilisateur> getUsers() throws UnableToReadUsersException;
}

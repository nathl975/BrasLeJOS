package com.RobotArm.interfaces;

import java.util.ArrayList;

public interface IGestionLogs {
    /**
     * Sauvegarde un log dans la persistance
     * @param paramString le log à sauvegarder
     *
     */
    void sauverLog(String paramString);

    /**
     * @return Liste des logs récents
     *
     */
    ArrayList<String> recupererLogs();
}

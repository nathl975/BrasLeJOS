package com.RobotArm.factory;

import com.RobotArm.business.Gamme;
import com.RobotArm.business.Operation;
import com.RobotArm.business.Tache;
import com.RobotArm.dto.GammeDTO;
import com.RobotArm.dto.OperationDTO;
import com.RobotArm.dto.TacheDTO;

import java.util.ArrayList;

public class GammeFactory {
    public Gamme createFromGammeDTO(GammeDTO dto){
        Gamme gamme = new Gamme();
        gamme.setId(dto.id);
        gamme.setDescription(dto.description);

        ArrayList<Operation> listeOperations = new ArrayList<>();
        for (OperationDTO o:dto.listeOperations) {
            Operation op = new Operation();
            op.setId(o.id);
            op.setDescription(o.description);

            ArrayList<Tache> listeTaches = new ArrayList<>();
            for (TacheDTO t : o.listeTaches) {
                Tache ta;
                if (t.typeAction == Tache.TypeAction.Attendre) {
                    ta = new Tache(t.id, t.description, t.valeur);
                } else {
                    ta = new Tache(t.id, t.description, t.valeur, t.moteur);
                }

                listeTaches.add(ta);
            }
            op.setListeTaches(listeTaches);

            listeOperations.add(op);
        }
        gamme.setListeOperations(listeOperations);

        return gamme;
    }
}

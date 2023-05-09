package com.RobotArm.dto;

import com.RobotArm.business.Tache;
import com.RobotArm.jsonAdapters.TacheDTOAdapter;
import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(TacheDTOAdapter.class)

public class TacheDTO {
    public String id;
    public String description;
    public Tache.TypeAction typeAction;
    public int valeur;
    public char moteur;
}

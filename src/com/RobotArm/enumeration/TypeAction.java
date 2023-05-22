package com.RobotArm.enumeration;

import com.google.gson.annotations.SerializedName;

public enum TypeAction {
    @SerializedName("wait")
    Attendre,

    @SerializedName("turnL")
    TournerGauche,
    @SerializedName("turnR")
    TournerDroite,
    @SerializedName("grab")
    Attraper,
    @SerializedName("drop")
    Poser;
}

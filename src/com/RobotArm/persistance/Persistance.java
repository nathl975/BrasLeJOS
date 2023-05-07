package com.RobotArm.persistance;

import com.RobotArm.business.Gamme;
import com.RobotArm.business.Utilisateur;
import com.RobotArm.interfaces.IPersistance;
import com.RobotArm.jsonAdapters.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Persistance implements IPersistance {
    private final JsonAdapter jsonAdapter;
    private final JsonReader jsonReader;
    private final JsonWriter jsonWriter;
    public Persistance (String filename) throws IOException {
        this.jsonAdapter = new JsonAdapter();
        this.jsonReader = new JsonReader(new FileReader(filename));
        this.jsonWriter = new JsonWriter(new FileWriter(filename));
    }

    @Override
    public void creerGamme(Gamme gamme) {
        String jsonContent = jsonAdapter.serialize(gamme);
        try {
            jsonWriter.beginObject().jsonValue(jsonContent);
        } catch (IOException e) {
            System.out.println("Impossible de stocker la gamme");
        }
    }

    @Override
    public void modifierGamme(Gamme paramGamme) {

    }

    @Override
    public void supprimerGamme(String paramString) {

    }

    @Override
    public HashMap<String, Gamme> recupererGammes() {
        return null;
    }

    @Override
    public Gamme recupererGammeDefaut() {
        return null;
    }

    @Override
    public void sauverLog(String paramString) {

    }

    @Override
    public ArrayList<String> recupererLogs() {
        return null;
    }

    @Override
    public String getConfig(String paramString) {
        return null;
    }

    @Override
    public void creerCompte(String login, String pwd) {

    }

    @Override
    public Utilisateur trouverCompte(String l, String p) {
        return null;
    }

    @Override
    public void supprimerCompte(String login) {

    }
}

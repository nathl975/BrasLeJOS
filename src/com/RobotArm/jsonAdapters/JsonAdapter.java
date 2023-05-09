package com.RobotArm.jsonAdapters;

import com.RobotArm.business.Gamme;
import com.RobotArm.interfaces.IAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonAdapter<T> implements IAdapter<T> {

    public String serialize(T object) {
        return new Gson().toJson(object);
    }

    public void serializeAll(ArrayList<T> array, FileWriter fileWriter) {
        new Gson().toJson(array, fileWriter);
    }

    public Gamme deserialize(String json, Type type) throws JsonParseException {
        return new Gson().fromJson(json, type);
    }

    public ArrayList<T> deserialize(FileReader fileReader, Type type) throws IOException {

        return new Gson().fromJson(fileReader, type);
    }
}

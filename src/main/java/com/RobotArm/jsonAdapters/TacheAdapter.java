package com.RobotArm.jsonAdapters;

import java.lang.reflect.Type;

import com.RobotArm.business.Tache;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;

public class TacheAdapter implements JsonDeserializer<Tache>{
	public Tache deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
	{
		JsonObject json = arg0.getAsJsonObject();
		Gson gson = new Gson();
		Tache.TypeAction typeAction = gson.fromJson(json.get("typeAction"), Tache.TypeAction.class);
		
		Tache t;
		switch(typeAction)
		{
			case Tourner:
				t = new Tache(json.get("id").getAsString(), json.get("description").getAsString(), json.get("valeur").getAsInt(), json.get("moteur").getAsCharacter());
				break;
			default:
				t = new Tache(json.get("id").getAsString(), json.get("description").getAsString(), json.get("valeur").getAsInt());				
				break;
		}
		
		return t;
	}

}

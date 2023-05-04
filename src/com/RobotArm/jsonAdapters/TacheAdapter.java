package com.RobotArm.jsonAdapters;

import com.RobotArm.business.Tache;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Classe permettant de convertir un objet JSON représentant une téche, en téche.
 * Assure la compatibilité entre la classe Téche EV3 et la classe Téche Android.
 * @author Alvin
 *
 */
public class TacheAdapter implements JsonDeserializer<Tache>
{
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

package com.RobotArm.jsonAdapters;

import com.RobotArm.business.Tache;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Classe permettant de convertir un objet JSON représentant une tâche, en tâche.
 * Assure la compatibilité entre la classe Tâche EV3 et la classe Tâche Android.
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
		if (Objects.requireNonNull(typeAction) == Tache.TypeAction.Tourner) {
			t = new Tache(json.get("id").getAsString(), json.get("description").getAsString(), json.get("valeur").getAsInt(), json.get("moteur").getAsString().charAt(0));
		} else {
			t = new Tache(json.get("id").getAsString(), json.get("description").getAsString(), json.get("valeur").getAsInt());
		}
		
		return t;
	}
}

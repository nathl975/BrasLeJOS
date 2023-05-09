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
public class TacheAdapter implements JsonDeserializer<Tache>, JsonSerializer<Tache>
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

	@Override
	public JsonElement serialize(Tache tache, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", tache.getId());
		jsonObject.addProperty("description", tache.getDescription());
		jsonObject.addProperty("typeAction", tache.getTypeAction().toString());
		jsonObject.addProperty("valeur", tache.getValeur());
		System.out.println(tache.toString());
		if (tache.getTypeAction() == Tache.TypeAction.Tourner) {
			System.out.println(tache.getId());
			jsonObject.addProperty("moteur", tache.getMoteur().getPort());
		}

		return null;
	}
}

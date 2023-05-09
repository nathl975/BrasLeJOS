package com.RobotArm.jsonAdapters;

import com.RobotArm.business.Tache;
import com.RobotArm.dto.TacheDTO;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Classe permettant de convertir un objet JSON représentant une tâche, en tâche.
 * Assure la compatibilité entre la classe Tâche EV3 et la classe Tâche Android.
 * @author Alvin
 *
 */
public class TacheDTOAdapter implements JsonDeserializer<TacheDTO>
{
	public TacheDTO deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
	{
		JsonObject json = arg0.getAsJsonObject();
		Gson gson = new Gson();
		Tache.TypeAction typeAction = gson.fromJson(json.get("typeAction"), Tache.TypeAction.class);

		TacheDTO t = new TacheDTO();
		t.id = json.get("id").getAsString();
		t.description = json.get("description").getAsString();
		t.valeur = json.get("valeur").getAsInt();
		t.typeAction = typeAction;
		if (typeAction == Tache.TypeAction.Tourner) {
			t.moteur = json.get("moteur").getAsString().charAt(0);
		}

		return t;
	}
}

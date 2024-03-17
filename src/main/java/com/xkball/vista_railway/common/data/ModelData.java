package com.xkball.vista_railway.common.data;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.xkball.vista_railway.utils.JsonSerializable;

import com.xkball.vista_railway.utils.JsonUtils;
import com.xkball.vista_railway.utils.Quaternion;
import com.xkball.vista_railway.utils.Vector3f;


@Desugar
public record ModelData(String modelPath,
                        double scale,
                        Quaternion rotation,
                        Vector3f offset) implements JsonSerializable {
    public ModelData(JsonObject obj){
        this(
                obj.get("modelPath").getAsString(),
                obj.get("scale").getAsFloat(),
                JsonUtils.quaternionFromJson(obj.getAsJsonObject("rotation")),
                JsonUtils.vector3fFromJson(obj.getAsJsonObject("offset"))
        );
        //assert obj.has("modelPath") && obj.has("scale") && obj.has("rotation") && obj.has("offset");
        
    }
    
    @Override
    public JsonObject toJson() {
        var result = new JsonObject();
        result.addProperty("modelPath",modelPath);
        result.addProperty("scale",scale);
        result.add("rotation", JsonUtils.quaternionToJson(rotation));
        result.add("offset",JsonUtils.vector3fToJson(offset));
        return result;
    }
}

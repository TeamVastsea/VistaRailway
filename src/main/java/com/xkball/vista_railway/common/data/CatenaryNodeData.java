package com.xkball.vista_railway.common.data;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.xkball.vista_railway.utils.JsonSerializable;
import com.xkball.vista_railway.utils.JsonUtils;
import com.xkball.vista_railway.utils.Vector3f;

@Desugar
public record CatenaryNodeData(int nodeID,
                               Vector3f topOffset,
                               Vector3f bottomOffset,
                               LineType lineType) implements JsonSerializable {
    
    public CatenaryNodeData(JsonObject obj){
        this(obj.get("id").getAsInt(),
                JsonUtils.vector3fFromJson(obj.getAsJsonObject("topOffset")),
                JsonUtils.vector3fFromJson(obj.getAsJsonObject("bottomOffset")),
                LineType.getFromID(obj.get("lineType").getAsInt())
                );
    }
    
    @Override
    public JsonObject toJson() {
        var result = new JsonObject();
        result.addProperty("id",nodeID);
        result.add("topOffset", JsonUtils.vector3fToJson(topOffset));
        result.add("bottomOffset", JsonUtils.vector3fToJson(bottomOffset));
        result.addProperty("lineType",lineType.ordinal()+1);
        return result;
    }
}

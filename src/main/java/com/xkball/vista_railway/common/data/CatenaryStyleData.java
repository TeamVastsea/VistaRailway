package com.xkball.vista_railway.common.data;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.xkball.vista_railway.utils.JsonSerializable;
import com.xkball.vista_railway.utils.JsonUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;
@Desugar

public record CatenaryStyleData(String name,
                                int id,
                                List<ModelData> rendering,
                                int nodeCount,
                                Int2ObjectMap<CatenaryNodeData> nodeMap) implements JsonSerializable {
    
    public CatenaryStyleData(JsonObject obj){
        this(obj.get("name").getAsString(),
                obj.get("id").getAsInt(),
                JsonUtils.listFromJson(ArrayList::new,ModelData::new,obj.getAsJsonArray("rendering")),
                obj.get("nodeCount").getAsInt(),
                JsonUtils.mapFromJson(Int2ObjectOpenHashMap::new,CatenaryNodeData::new,obj.getAsJsonObject("nodeData"))
                );
    }
    @Override
    public JsonObject toJson() {
        var result = new JsonObject();
        result.addProperty("name",name);
        result.addProperty("id",id);
        result.addProperty("nodeCount",nodeCount);
        result.add("rendering", JsonUtils.listToJson(rendering));
        result.add("nodeData",JsonUtils.intMapToJson(nodeMap));
        return result;
    }
}

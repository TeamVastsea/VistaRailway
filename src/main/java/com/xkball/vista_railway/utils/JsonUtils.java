package com.xkball.vista_railway.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.function.Supplier;

public class JsonUtils {
    
    public static String jsonToString(JsonElement element){
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setLenient(true);
            jsonWriter.setIndent("  ");
            Streams.write(element, jsonWriter);
            return stringWriter.toString();
        } catch (IOException e) {
            return element.getAsString();
        }
    }
    public static Quaternion quaternionFromJson(JsonObject obj){
        assert obj.has("x") && obj.has("y") && obj.has("z") && obj.has("w");
        return new Quaternion(
                obj.get("x").getAsFloat(),
                obj.get("y").getAsFloat(),
                obj.get("z").getAsFloat(),
                obj.get("w").getAsFloat());
    }
    
    public static JsonObject quaternionToJson(Quaternion quaternion){
        var result = new JsonObject();
        result.addProperty("x",quaternion.x);
        result.addProperty("y",quaternion.y);
        result.addProperty("z",quaternion.z);
        result.addProperty("w",quaternion.w);
        return result;
    }
    
    public static Vector3f vector3fFromJson(JsonObject obj){
        assert  obj.has("x") && obj.has("y") && obj.has("z");
        return new Vector3f(
                obj.get("x").getAsFloat(),
                obj.get("y").getAsFloat(),
                obj.get("z").getAsFloat());
    }
    
    public static JsonObject vector3fToJson(Vector3f vec3f){
        var result = new JsonObject();
        result.addProperty("x",vec3f.x);
        result.addProperty("y",vec3f.y);
        result.addProperty("z",vec3f.z);
        return result;
    }
    
    public static <T extends JsonSerializable> List<T> listFromJson(Supplier<List<T>> listSupplier, JsonFunction<T> fromJson, JsonArray array){
        var result = listSupplier.get();
        for(var obj : array){
            result.add(fromJson.applyFromJson(obj.getAsJsonObject()));
        }
        return result;
    }
    
    public static JsonArray listToJson(List<? extends JsonSerializable> list){
        var result = new JsonArray();
        for(var obj : list){
            result.add(obj.toJson());
        }
        return result;
    }
    
    public static <T extends JsonSerializable> Int2ObjectMap<T> mapFromJson(Supplier<Int2ObjectMap<T>> mapSupplier,JsonFunction<T> fromJson,JsonObject object){
        var result = mapSupplier.get();
        for(var entry:object.entrySet()){
            assert AssertUtils.assertIsInt(entry.getKey());
            result.put(Integer.parseInt(entry.getKey()),fromJson.applyFromJson(entry.getValue().getAsJsonObject()));
        }
        return result;
    }
    
    
    public static JsonObject intMapToJson(Int2ObjectMap<? extends JsonSerializable> map){
        var result = new JsonObject();
        for(var entry:map.entrySet()){
            result.add(entry.getKey().toString(),entry.getValue().toJson());
        }
        return result;
    }
    
    
    @FunctionalInterface
    public interface JsonFunction<T extends JsonSerializable>{
        T applyFromJson(JsonObject obj);
    }
}

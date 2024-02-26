package com.xkball.vista_railway.common.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.xkball.vista_railway.utils.Final;
import com.xkball.vista_railway.utils.JsonUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CatenaryDataManager {
    
    public static final CatenaryDataManager INSTANCE = new CatenaryDataManager();
    public boolean init = false;
    private final JsonParser jsonParser = new JsonParser();
    public final List<CatenaryStyleData> catenaryDataList = new ArrayList<>();
    private final Int2ObjectMap<CatenaryStyleData> catenaryDataMap = new Int2ObjectOpenHashMap<>();
    
    public final Final<String> data = new Final<>();
    
    private CatenaryDataManager(){
    
    }
    
    public void loadFromDirectory(Path path){
        try {
            if(path.toFile().isDirectory()){
                //noinspection ResultOfMethodCallIgnored
                path.toFile().delete();
            }
            if(!path.toFile().exists()){
                genDefaultConfig(path);
            }
            else {
                var contents = new String(Files.readAllBytes(path));
                data.set(contents);
                loadFromString(contents);
            }
           
            
        } catch (IOException e) {
            var logger = LogManager.getLogger();
            logger.error("VistaRailway read config failed");
            logger.error(e);
        }
        
    }
    
    public void loadFromString(String str){
        try {
            var jsonRaw = jsonParser.parse(str);
            assert jsonRaw.isJsonArray();
            var jsonArray = jsonRaw.getAsJsonArray();
            for(var element : jsonArray){
                assert element.isJsonObject();
                catenaryDataList.add(new CatenaryStyleData(element.getAsJsonObject()));
            }
            catenaryDataList.sort(Comparator.comparingInt(CatenaryStyleData::id));
            init = true;
        }catch (IllegalStateException e){
            var logger = LogManager.getLogger();
            logger.error("VistaRailway read config failed");
            logger.error(e);
        }
        
    }
    
    public CatenaryStyleData get(int id){
        if(catenaryDataMap.isEmpty()){
            for(var data:catenaryDataList){
                catenaryDataMap.put(data.id(),data);
            }
        }
        return catenaryDataMap.get(id);
    }
    
    
    public void genDefaultConfig(Path path) throws IOException {
        var modelData = new ModelData("1.obj",1d,new Quaternion(0,0,0,0),new Vector3f(0,0,0));
        var catenaryNodeData = new CatenaryNodeData(0,new Vector3f(0,0,0),new Vector3f(0,0,0));
        var catenaryNodeData2 = new CatenaryNodeData(1,new Vector3f(0,0,0),new Vector3f(0,0,0));
        var catenaryNodeData3 = new CatenaryNodeData(2,new Vector3f(0,0,0),new Vector3f(0,0,0));
        var catenaryNodeData4 = new CatenaryNodeData(3,new Vector3f(0,0,0),new Vector3f(0,0,0));
        var catenaryStyleData = new CatenaryStyleData("default",0, ImmutableList.of(modelData),1, Int2ObjectMaps.singleton(0,catenaryNodeData));
        var map2 = new Int2ObjectOpenHashMap<CatenaryNodeData>();
        var map3 = new Int2ObjectOpenHashMap<CatenaryNodeData>();
        var map4 = new Int2ObjectOpenHashMap<CatenaryNodeData>();
        map2.put(0,catenaryNodeData);
        map2.put(1,catenaryNodeData2);
        map3.put(0,catenaryNodeData);
        map3.put(1,catenaryNodeData2);
        map3.put(2,catenaryNodeData3);
        map4.put(0,catenaryNodeData);
        map4.put(1,catenaryNodeData2);
        map4.put(2,catenaryNodeData3);
        map4.put(3,catenaryNodeData4);
        var catenaryStyleData2 =  new CatenaryStyleData("default2",1, ImmutableList.of(modelData),2, map2);
        var catenaryStyleData3 =  new CatenaryStyleData("default3",2, ImmutableList.of(modelData),3, map3);
        var catenaryStyleData4 =  new CatenaryStyleData("default4",3, ImmutableList.of(modelData),4, map4);
        catenaryDataList.add(catenaryStyleData);
        catenaryDataList.add(catenaryStyleData2);
        catenaryDataList.add(catenaryStyleData3);
        catenaryDataList.add(catenaryStyleData4);
        var jsonArr = new JsonArray();
        for(var csd : catenaryDataList){
            jsonArr.add(csd.toJson());
        }
        var str = JsonUtils.jsonToString(jsonArr);
        data.set(str);
        //noinspection ReadWriteStringCanBeUsed
        Files.write(path, str.getBytes(StandardCharsets.UTF_8));
        init = true;
    }
    
}

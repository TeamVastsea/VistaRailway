package com.xkball.vista_railway.client.gui.component;

import com.xkball.vista_railway.client.gui.screen.VRBaseScreen;
import org.lwjgl.util.vector.Vector3f;

public class Vector3fEditField extends Vector3EditField<Vector3f>{
    protected Vector3f value = new Vector3f(0,0,0);
    public Vector3fEditField(VRBaseScreen screen, int x, int y, int wTextField, int wInterval, int h) {
        super(screen, x, y, wTextField, wInterval, h);
    }
    
    @Override
    public Vector3f get() {
        value.set(parseAsFloat(textFieldX),parseAsFloat(textFieldY),parseAsFloat(textFieldZ));
        return value;
    }
    
    @Override
    public void init(Vector3f value) {
        this.value.set(value);
        textFieldX.setText(Float.toString(value.x));
        textFieldY.setText(Float.toString(value.y));
        textFieldZ.setText(Float.toString(value.z));
    }
    
    @Override
    public boolean validInput(String str) {
        if (str.isEmpty()) return true;
        try {
            Float.parseFloat(str);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    
}

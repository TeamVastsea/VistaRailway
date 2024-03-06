package com.xkball.vista_railway.client.gui.component;

import com.xkball.vista_railway.client.gui.screen.VRBaseScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockPosEditField extends Vector3EditField<BlockPos> {
    
    protected BlockPos value;
    
    public BlockPosEditField(VRBaseScreen screen, int x, int y, int wTextField, int wInterval, int h) {
        super(screen, x, y, wTextField, wInterval, h);
    }
    
    @Override
    public BlockPos get() {
        value = new BlockPos(parseAsInt(textFieldX),parseAsInt(textFieldY),parseAsInt(textFieldZ));
        return value;
    }
    
    @Override
    public void init(BlockPos value) {
        this.value = new BlockPos(value);
        textFieldX.setText(Integer.toString(value.getX()));
        textFieldY.setText(Integer.toString(value.getY()));
        textFieldZ.setText(Integer.toString(value.getZ()));
    }
    
    @Override
    public boolean validInput(String str) {
        if (str.isEmpty()) return true;
        try {
            Integer.parseInt(str);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}

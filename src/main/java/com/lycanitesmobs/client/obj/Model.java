package com.lycanitesmobs.client.obj;

/*
 * This class was created by <Lycanite>. It's distributed as
 * part of the LycanitesMob Mod. Get the Source Code in gitlab:
 * https://gitlab.com/Lycanite/LycanitesMobs
 *
 * LycanitesMob is Open Source and distributed under the
 * GPLv3 License: https://www.gnu.org/licenses/gpl-3.0.html
 */
public abstract class Model
{

    private String id;

    public abstract void render();
    
    public abstract void renderGroups(String s);
    
    public void setID(String id)
    {
        this.id = id;
    }
    
    public String getID()
    {
        return id;
    }
}

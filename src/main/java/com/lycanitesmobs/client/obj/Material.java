package com.lycanitesmobs.client.obj;

import org.lwjgl.util.vector.Vector3f;

/*
 * This class was created by <Lycanite>. It's distributed as
 * part of the LycanitesMob Mod. Get the Source Code in gitlab:
 * https://gitlab.com/Lycanite/LycanitesMobs
 *
 * LycanitesMob is Open Source and distributed under the
 * GPLv3 License: https://www.gnu.org/licenses/gpl-3.0.html
 */
public class Material
{
    
    private String name;
    public Vector3f diffuseColor;
    public Vector3f ambientColor;
    public int ambientTexture;
    public int diffuseTexture;
    public float transparency;

    public Material(String name)
    {
        transparency = 1f;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

}

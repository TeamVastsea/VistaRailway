package com.lycanitesmobs.client.obj;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

/*
 * This class was created by <Lycanite>. It's distributed as
 * part of the LycanitesMob Mod. Get the Source Code in gitlab:
 * https://gitlab.com/Lycanite/LycanitesMobs
 *
 * LycanitesMob is Open Source and distributed under the
 * GPLv3 License: https://www.gnu.org/licenses/gpl-3.0.html
 */
public class Vertex {
    private Vector3f pos;
    private Vector2f texCoords;
    private Vector3f normal;
    private Vector3f tangent;

    public Vertex(Vector3f pos, Vector2f texCoords, Vector3f normal, Vector3f tangent) {
        this.pos = pos;
        this.texCoords = texCoords;
        this.normal = normal;
        this.tangent = tangent;
    }
    
    public Vector3f getPos() {
        return this.pos;
    }
    
    public Vector2f getTexCoords() {
        return this.texCoords;
    }

    /** Returns per vertex normal for smoother shading. **/
    public Vector3f getNormal() {
        return this.normal;
    }
    
    public Vector3f getTangent() {
        return tangent;
    }
}

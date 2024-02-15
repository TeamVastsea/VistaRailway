package com.lycanitesmobs.client.obj;

/*
 * This class was created by <Lycanite>. It's distributed as
 * part of the LycanitesMob Mod. Get the Source Code in gitlab:
 * https://gitlab.com/Lycanite/LycanitesMobs
 *
 * LycanitesMob is Open Source and distributed under the
 * GPLv3 License: https://www.gnu.org/licenses/gpl-3.0.html
 */
import net.minecraftforge.fml.common.eventhandler.Event;

public class ObjEventWrapper extends Event
{

    public ObjEvent objEvent;

    public ObjEventWrapper(ObjEvent e)
    {
        this.objEvent = e;
    }

    public boolean isCancelable()
    {
        return objEvent.canBeCancelled();
    }
}

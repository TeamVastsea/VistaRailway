package com.lycanitesmobs.client.obj;

import net.minecraftforge.fml.common.eventhandler.Event;

/*
 * This class was created by <Lycanite>. It's distributed as
 * part of the LycanitesMob Mod. Get the Source Code in gitlab:
 * https://gitlab.com/Lycanite/LycanitesMobs
 *
 * LycanitesMob is Open Source and distributed under the
 * GPLv3 License: https://www.gnu.org/licenses/gpl-3.0.html
 */
public class TessellatorModelEvent extends Event
{

    public static class RenderPre extends TessellatorModelEvent
    {
        public RenderPre(TessellatorModel model)
        {
            super(model);
        }
    }

    public static class RenderPost extends TessellatorModelEvent
    {
        public RenderPost(TessellatorModel model)
        {
            super(model);
        }
    }

    public TessellatorModel model;

    public TessellatorModelEvent(TessellatorModel model)
    {
        this.model = model;
    }

    public static class RenderGroupEvent extends TessellatorModelEvent
    {

        public String group;

        public RenderGroupEvent(String groupName, TessellatorModel model)
        {
            super(model);
            this.group = groupName;
        }

        public static class Pre extends RenderGroupEvent
        {
            public Pre(String g, TessellatorModel m)
            {
                super(g, m);
            }
        }

        public static class Post extends RenderGroupEvent
        {
            public Post(String g, TessellatorModel m)
            {
                super(g, m);
            }
        }

    }

    public boolean isCancelable()
    {
        return true;
    }

}

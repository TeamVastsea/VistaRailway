package com.lycanitesmobs.client.obj;

import java.util.HashMap;

/*
 * This class was created by <Lycanite>. It's distributed as
 * part of the LycanitesMob Mod. Get the Source Code in gitlab:
 * https://gitlab.com/Lycanite/LycanitesMobs
 *
 * LycanitesMob is Open Source and distributed under the
 * GPLv3 License: https://www.gnu.org/licenses/gpl-3.0.html
 */
public class HashMapWithDefault<K, V> extends HashMap<K, V>
{

	private static final long serialVersionUID = 5995791692010816132L;
	private V				 defaultValue;

	public void setDefault(V value)
	{
		defaultValue = value;
	}

	public V getDefault()
	{
		return defaultValue;
	}

	public V get(Object key)
	{
		if(this.containsKey(key))
			return super.get(key);
		else
			return defaultValue;
	}
}

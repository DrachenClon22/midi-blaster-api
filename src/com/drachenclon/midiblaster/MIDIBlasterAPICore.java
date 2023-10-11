package com.drachenclon.midiblaster;

import java.util.HashSet;

import org.bukkit.plugin.java.JavaPlugin;

import com.drachenclon.midiblaster.MIDIPlayer.MIDIPlayer;

public final class MIDIBlasterAPICore extends JavaPlugin {
	public static final String VERSION = "Author: DrachenClon22; Version: 1.0.0";
	
	private static boolean _debugMode = false;
	private static HashSet<MIDIPlayer> _players = new HashSet<MIDIPlayer>();
	
	@Override
	public void onDisable() {
		for (MIDIPlayer pl : _players) {
			pl.Stop();
		}
	}
	
	@Override
	public void onEnable() {
		_debugMode = true;
	}
	
	public static void RegisterPlayer(MIDIPlayer player) {
		if (!_players.contains(player)) {
			_players.add(player);
		}
	}
	
	public static void SetDebugMode(boolean newState) {
		_debugMode = newState;
	}
	
	public static boolean GetDebugMode() {
		return _debugMode;
	}
}

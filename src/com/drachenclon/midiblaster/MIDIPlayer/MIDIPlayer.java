package com.drachenclon.midiblaster.MIDIPlayer;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.drachenclon.midiblaster.MIDIBlasterAPICore;
import com.drachenclon.midiblaster.Entities.MIDINote;
import com.drachenclon.midiblaster.Entities.MIDI.MIDIFile;

/**
 * Main class that is playing music
 * @apiNote before calling any class' methods, always use {@link MIDIPlayer#init(JavaPlugin) init()} method
 */
public final class MIDIPlayer {
	
	private BukkitTask _task;
	private MIDINote[] _notes;
	private int _perfectBlockSize;
	
	private Player[] _players;
	
	private static JavaPlugin _core;
	/**
	 * Uses to init MIDIPlayer
	 * @apiNote uses only once to define core plugin
	 * @param core - core plugin that uses API
	 */
	public static void init(JavaPlugin core) {
		if (_core == null) {
			_core = core;
		}
	}
	
	public Player[] GetPlayers() {
		return _players;
	}
	
	public void Stop() {
		if (_task != null) {
			_task.cancel();
			_task = null;
		}
	}
	
	public void Play(MIDIFile midi, Player player) {
		Play(midi, new Player[] {player});
	}
	
	public void Play(MIDIFile midi, Player[] players) {
		
		_players = players;
		
		// Register this MIDIPlayer in core API class
		MIDIBlasterAPICore.RegisterPlayer(this);
		
		Stop();
		_notes = midi.GetNotes();
		
		/*
		 * Result notes that will be played divide into blocks of notes to optimize
		 * handling process in the future. This allows to play MIDI files with more than
		 * 20k notes (max MIDI tested had 21k notes) without any problems and any lags
		 * 
		 * Perfect block size is sqrt of notes to keep balance between block size and blocks count
		 * 
		 * And not to forget that last block usually not equals to the first block
		 */
		_perfectBlockSize = (int) Math.ceil(Math.sqrt(_notes.length));
		MIDIFile[] notesBlocks = new MIDIFile[_notes.length / _perfectBlockSize];
		
		int blockSize = 0;
		MIDINote[] currentNotesForBlock = null;
		for (int i = 0, o = 0, b = 0; i < _notes.length; i++, b++) {
			if (b == 0) {
				blockSize = (_notes.length-i>_perfectBlockSize) ? 
						_perfectBlockSize : (_notes.length-i)%_perfectBlockSize-1;
				
				if (blockSize == 0) {
					break;
				}
				
				currentNotesForBlock = new MIDINote[blockSize];
			}
			
			if (b-blockSize==0) {
				notesBlocks[o] = new MIDIFile(currentNotesForBlock);
				b=0;
				o++;
			}
			
			currentNotesForBlock[b] = _notes[i].GetCopy();
		}
		currentNotesForBlock = null;
		
		// Start new task so no delays will be in plugin classes
		_task = new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : _players) {
					if (MIDIBlasterAPICore.GetDebugMode() && player.isOp()) {
						player.sendMessage("Total notes: " + midi.GetSize());
						player.sendMessage("Block size: " + _perfectBlockSize);
						player.sendMessage("Total blocks: " + notesBlocks.length);
					}
				}
				
				try {
					Thread.sleep(1000);
					for (Player player : _players) {
						if (MIDIBlasterAPICore.GetDebugMode() && player.isOp()) {
							player.sendMessage("Starting");
						}
					}
				} catch (InterruptedException e) {
					for (Player player : _players) {
						if (MIDIBlasterAPICore.GetDebugMode() && player.isOp()) {
							player.sendMessage(e.getMessage());
						}
					}
				}
				
				long time = 0;
				Location loc = null;
				for (int block = 0; block < notesBlocks.length; block++) {
					_notes = notesBlocks[block].GetNotes();
					for (int i = 0; i < notesBlocks[block].GetSize(); i++) {
						if (_task == null || _task.isCancelled()) {
							return;
						}
						try {
							if (_notes[i].GetMillisecondsTick() > 0) {
								time = _notes[i].GetMillisecondsTick();
								for (Player player : _players) {
									if (MIDIBlasterAPICore.GetDebugMode() && player.isOp()) {
										player.sendMessage("Waiting for: " + time + " ms");
									}
								}
								Thread.sleep(time);
							}
							
							if (_notes[i].GetRawNote() != -1) {
								for (Player player : _players) {
									loc = player.getLocation();
									
									if (MIDIBlasterAPICore.GetDebugMode() && player.isOp()) {
										player.sendMessage("Note: " + _notes[i].GetNote().getTone().name() + 
												(_notes[i].GetNote().isSharped() ? "#" : "_") + _notes[i].GetNote().getOctave() +
												" Channel: " + _notes[i].GetChannel());
									}
									player.playNote(loc, _notes[i].GetInstrument().GetInstrument(), _notes[i].GetNote());
								}
							}
						} catch (Exception e) {
							for (Player player : _players) {
								if (MIDIBlasterAPICore.GetDebugMode() && player.isOp()) {
									player.sendMessage(e.getMessage());
								}
							}
							Stop();
						}
					}
				}
				Stop();
			}
		}.runTaskAsynchronously(_core);
	}
}
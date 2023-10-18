package com.drachenclon.midiblaster.Entities.MIDI;

import com.drachenclon.midiblaster.Entities.MIDINote;

/**
 * File that contain read-to-use files to play music, notes are sorted
 */
public class MIDIFile extends RawMIDIFile {
	private int _size;
	
	public MIDIFile(int size, MIDINote[] notes) {
		super(CopyNotes(notes));
		_size = size;
	}
	
	private static MIDINote[] CopyNotes(MIDINote[] notes) {
		MIDINote[] temp = new MIDINote[notes.length];
		for (int i = 0; i < notes.length; i++) {
			temp[i] = notes[i].GetCopy();
		}
		return temp;
	}
	
	public MIDIFile(MIDINote[] notes) {
		this(notes.length, notes);
	}
	
	/**
	 * Get size of total amount of notes in file
	 * @return integer, amount of notes in file
	 */
	public int GetSize() {
		return _size;
	}
	
	/**
	 * Get current notes from file
	 * @return array of MIDINote, all notes from file
	 */
	public MIDINote[] GetNotes() {
		return GetRawNotes();
	}
}

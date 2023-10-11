package com.drachenclon.midiblaster.Entities.MIDI;

import com.drachenclon.midiblaster.Entities.MIDINote;

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
	
	public int GetSize() {
		return _size;
	}
	
	public MIDINote[] GetNotes() {
		return GetRawNotes();
	}
}

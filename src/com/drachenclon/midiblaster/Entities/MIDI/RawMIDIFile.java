package com.drachenclon.midiblaster.Entities.MIDI;

import com.drachenclon.midiblaster.Entities.MIDINote;

public class RawMIDIFile {
	private MIDINote[] _notes;
	
	public RawMIDIFile(MIDINote[] notes) {
		_notes = notes;
	}
	
	public MIDINote[] GetRawNotes() {
		return _notes;
	}
}

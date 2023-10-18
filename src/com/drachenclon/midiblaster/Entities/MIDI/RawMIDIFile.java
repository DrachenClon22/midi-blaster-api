package com.drachenclon.midiblaster.Entities.MIDI;

import com.drachenclon.midiblaster.Entities.MIDINote;

/**
 * Raw, just decoded MIDI file information, contain MIDINotes and all info. 
 * Not sorted and not read-to-use.
 */
public class RawMIDIFile {
	private MIDINote[] _notes;
	
	public RawMIDIFile(MIDINote[] notes) {
		_notes = notes;
	}
	
	/**
	 * Get current raw notes from file
	 * @return array of MIDINote, all raw notes from raw file
	 */
	public MIDINote[] GetRawNotes() {
		return _notes;
	}
}

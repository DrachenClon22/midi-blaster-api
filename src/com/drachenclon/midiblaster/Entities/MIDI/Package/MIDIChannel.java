package com.drachenclon.midiblaster.Entities.MIDI.Package;

import com.drachenclon.midiblaster.Entities.MIDINote;
import com.drachenclon.midiblaster.Entities.MIDI.MIDIFile;

public final class MIDIChannel extends MIDIFile {
	private int _channel = -1;
	
	public MIDIChannel(int channel, int size, MIDINote[] notes) {
		super(size, notes);
		_channel = channel;
	}
	
	/**
	 * Get current channel
	 * @return integer, current channel
	 */
	public int GetChannel() {
		return _channel;
	}
}

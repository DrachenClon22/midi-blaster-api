package com.drachenclon.midiblaster.Entities;

import org.bukkit.Instrument;

public final class MIDIInstrument {
	private Instrument _instrument;
	private int _octave;
	
	public MIDIInstrument(Instrument instrument, int octave) {
		_instrument = instrument;
		_octave = octave;
	}
	
	public static MIDIInstrument FromPercussion(int note) {
		if (note <= 14) {
			return new MIDIInstrument(Instrument.BASS_DRUM, 0);
		} else {
			return new MIDIInstrument(Instrument.SNARE_DRUM, 0);
		}
	}
	
	public Instrument GetInstrument() {
		return _instrument;
	}
	
	public int GetOctave() {
		return _octave;
	}
}

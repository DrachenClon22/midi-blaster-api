package com.drachenclon.midiblaster.Entities;

import org.bukkit.Instrument;

/**
 * MIDI instrument uses to adjust octaves and notes for current note.
 * <br>Contains information about octave adjust and native Minecraft instrument
 * <br>
 * <br>
 * <b>Octave adjustment example:</b><br>
 * Input note is 0(F#0), octave adjustment is 1, result note will be 0+12*(octave+1)=12(F#1)<br>
 * Octave has 12 notes, so adding 1 octave adding 12 notes. If result note more than 24(Max Minecraft note F#2),
 * result note will be start note.<br>
 * <b>Actual example:</b><br>
 * Start note=13(F1)+2 octave (see above)<br>
 * Temp note=35 (Not in range) <br>
 * Result note=13 (Not changed)
 */
public final class MIDIInstrument {
	private Instrument _instrument;
	private int _octave;
	
	public MIDIInstrument(Instrument instrument, int octave) {
		_instrument = instrument;
		_octave = octave;
	}
	
	/**
	 * If current channel is 9 (percussion), instrument should be selected depends on note,
	 * if one more than 14 (high enough) that'll be snare, else is bass
	 * @param note - current playing note
	 * @return MIDI instrument, snare or bass drum, depends on note playing
	 */
	public static MIDIInstrument FromPercussion(int note) {
		if (note <= 14) {
			return new MIDIInstrument(Instrument.BASS_DRUM, 0);
		} else {
			return new MIDIInstrument(Instrument.SNARE_DRUM, 0);
		}
	}
	
	/**
	 * Get current native instrument
	 * @return Minecraft native Instrument class of instrument
	 */
	public Instrument GetInstrument() {
		return _instrument;
	}
	
	/**
	 * Get octave adjustment for current instrument
	 * @return integer, octave adjust
	 */
	public int GetOctave() {
		return _octave;
	}
}

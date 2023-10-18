package com.drachenclon.midiblaster.Entities;

import org.bukkit.Note;

import com.drachenclon.midiblaster.Utils.MIDIConverter;

/**
 * Class that contains information about current MIDI note.
 * 
 * Notes can be 2 types:
 * (1) Regular note with pitch and delay, in this case _note variable
 * will be from 0 to 24;
 * (2) Pause note or just delay, actually not even played in final sequence,
 * needs for converting and optimizing, in this case _note will be -1.
 */
public class MIDINote {
	private int _note;
	private Note _convertedNote;
	private int _octave;
	private long _tick;
	private int _channel;
	private long _positionInTrack;
	private MIDIInstrument _instrument;
	
	public MIDINote(int note, int octave, long tick, int channel, long position) {
		_note = note;
		_octave = octave;
		_tick = tick;
		_channel = channel;
		_positionInTrack = position;
		
		if (note != -1) {
			_instrument = MIDIConverter.ConvertChannel(channel, note);
			_convertedNote = MIDIConverter.FinalizeNote(_note * octave, _instrument.GetOctave());
		}
	}
	
	public MIDINote GetCopy() {
		return new MIDINote(_note, _octave, _tick, _channel, _positionInTrack);
	}
	
	@Override
	public boolean equals(Object v) {
		boolean result = false;
		
		if (v instanceof MIDINote) {
			MIDINote ptr = (MIDINote)v;
			result = (_note == ptr.GetRawNote() &&
					_octave == ptr.GetOctave() &&
					_channel == ptr.GetChannel());
		}
		
		return result;
	}
	
	public static MIDINote Empty() {
		return new MIDINote(-1, 0, 0, 0, 0);
	}
	
	public void SetInstrument(MIDIInstrument instrument) {
		_instrument = instrument;
	}
	
	public MIDIInstrument GetInstrument() {
		return _instrument;
	}
	
	public void SetNote(Note note) {
		_convertedNote = note;
	}
	
	public Note GetNote() {
		return _convertedNote;
	}
	
	public int GetRawNote() {
		return _note;
	}
	
	public int GetOctave() {
		return _octave;
	}
	
	public long GetMillisecondsTick() {
		return _tick;
	}
	
	public int GetChannel() {
		return _channel;
	}
	
	public long GetTickPosition() {
		return _positionInTrack;
	}
	
	public void SetMillisecondsTick(long ms) {
		_tick = ms;
	}
	
	public void AddMillisecondsTick(long ms) {
		_tick += ms;
	}
}

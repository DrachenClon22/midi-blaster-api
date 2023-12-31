package com.drachenclon.midiblaster.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Instrument;
import org.bukkit.Note;

import com.drachenclon.midiblaster.Entities.MIDIInstrument;
import com.drachenclon.midiblaster.Entities.MIDINote;
import com.drachenclon.midiblaster.Entities.MIDI.MIDIFile;
import com.drachenclon.midiblaster.Entities.MIDI.RawMIDIFile;

public final class MIDIConverter {
	/**
	 * Converts channel and note input to MIDI instrument<bt>
	 * @param channel - channel on what current note is playing
	 * @param note - current playing note
	 * @return MIDI instrument
	 * @apiNote Every channel in MIDI has their own instrument
	 */
	public static MIDIInstrument ConvertChannel(int channel, int note) {
		switch(channel) {
		case 0: return new MIDIInstrument(Instrument.PIANO, 0);
		case 1: return new MIDIInstrument(Instrument.BASS_GUITAR, 2);
		case 2: return new MIDIInstrument(Instrument.PIANO, 0);
		case 3: return new MIDIInstrument(Instrument.PIANO, 0);
		case 4: return new MIDIInstrument(Instrument.FLUTE, 0);
		case 5: return new MIDIInstrument(Instrument.PIANO, 0);
		case 6: return new MIDIInstrument(Instrument.BASS_GUITAR, 1);
		case 7: return new MIDIInstrument(Instrument.FLUTE, 1);
		case 8: return new MIDIInstrument(Instrument.GUITAR, -1);
		
		case 9: return MIDIInstrument.FromPercussion(note);
		
		case 10: return new MIDIInstrument(Instrument.SNARE_DRUM, 1);
		case 11: return new MIDIInstrument(Instrument.GUITAR, -1);
		case 12: return new MIDIInstrument(Instrument.PIANO, 0);
		case 13: return new MIDIInstrument(Instrument.GUITAR, -1);
		case 14: return new MIDIInstrument(Instrument.PIANO, 0);
		case 15: return new MIDIInstrument(Instrument.BASS_GUITAR, 2);
		
		default: return new MIDIInstrument(Instrument.STICKS, 0);
		}
	}
	
	/**
	 * Finalizing note by adding all octave adjustments etc.
	 * @param raw - raw note input, 0-24
	 * @param octave - octave amount that should be added (remember that Minecraft only supports notes in range F#0-F#2)
	 * @return final note as Note
	 */
	public static Note FinalizeNote(int raw, int octave) {
		int temp = raw + octave * 12;
		raw = (temp > 0 && temp < 25) ? temp : raw;
		
		return new Note(raw);
	}
	
	/**
	 * Converts raw MIDI file to ready-to-use MIDI file<br>
	 * <br>
	 * Sorts all notes in right position, removes every duplicate notes and unite pauses
	 * @param input - input raw MIDI file
	 * @return ready-to-use MIDI file
	 */
	public static MIDIFile ConvertFromRaw(RawMIDIFile input) {
		MIDINote[] notes = input.GetRawNotes();
		/*
		 * Sort all notes by position in MIDI file and find delay between
		 * each note to play them correctly
		 */
		Arrays.sort(notes, Comparator.comparingDouble(o -> o.GetTickPosition()));
		long lastTick = 0;
		long currTick = 0;
		for (int i = 0; i < notes.length; i++) {
			currTick = notes[i].GetMillisecondsTick();
			notes[i].SetMillisecondsTick(currTick - lastTick);
			lastTick = currTick;
		}
		
		// Temp array of result notes
		List<MIDINote> waitable = new ArrayList<MIDINote>();
		// Array of temp elements that are actually accords (all notes between notes with delay or pauses)
		List<MIDINote> tempArray = new ArrayList<MIDINote>();
		
		// Last index of input note that is pause (to unite pauses that follow each other)
		int pauseIndex = -1;
		// Buffer variable of temp pauses ms delay
		long totalPause = 0;
		for (int i = 0; i < notes.length; i++) {
			// If note pitch is -1, note is actually pause
			if (notes[i].GetRawNote() == -1) {
				if (pauseIndex == -1) {
					pauseIndex = i;
				}
			} else {
				totalPause = 0;
				if (pauseIndex != -1) {
					for (int a = pauseIndex; a < i; a++) {
						totalPause += notes[a].GetMillisecondsTick();
					}
					pauseIndex = -1;
					
					notes[i].AddMillisecondsTick(totalPause);
				}
				
				if (notes[i].GetMillisecondsTick() > 0) {
					for (int o = 0; o < tempArray.size(); o++) {
						waitable.add(tempArray.get(o));
					}
					tempArray.clear();
				}

				if (!tempArray.contains(notes[i])) {
					tempArray.add(notes[i]);
				}
			}
		}
		
		notes = waitable.toArray(new MIDINote[waitable.size()]);
		waitable.clear();
		
		return new MIDIFile(notes.length, notes);
	}
}

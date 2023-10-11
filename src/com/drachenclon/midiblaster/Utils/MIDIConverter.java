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
	
	public static Note FinalizeNote(int raw, int octave) {
		int temp = raw + octave * 12;
		raw = (temp > 0 && temp < 25) ? temp : raw;
		
		return new Note(raw);
	}
	
	public static MIDIFile ConvertFromRaw(RawMIDIFile input) {
		MIDINote[] notes = input.GetRawNotes();
		Arrays.sort(notes, Comparator.comparingDouble(o -> o.GetTickPosition()));
		
		long lastTick = 0;
		long currTick = 0;
		for (int i = 0; i < notes.length; i++) {
			currTick = notes[i].GetMillisecondsTick();
			notes[i].SetMillisecondsTick(currTick - lastTick);
			lastTick = currTick;
		}
		
		List<MIDINote> waitable = new ArrayList<MIDINote>();
		List<MIDINote> tempArray = new ArrayList<MIDINote>();
		
		int pauseIndex = -1;
		long totalPause = 0;
		for (int i = 0; i < notes.length; i++) {
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

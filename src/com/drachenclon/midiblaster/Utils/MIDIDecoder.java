package com.drachenclon.midiblaster.Utils;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import com.drachenclon.midiblaster.Entities.MIDINote;
import com.drachenclon.midiblaster.Entities.MIDI.MIDIFile;
import com.drachenclon.midiblaster.Entities.MIDI.RawMIDIFile;
import com.drachenclon.midiblaster.Entities.MIDI.Package.MIDIChannel;

public class MIDIDecoder {
	
	public static MIDIFile Decode(String path) throws InvalidMidiDataException, IOException {
		Sequence sequence = MidiSystem.getSequence(new File(path));
		
		Track[] tracks = sequence.getTracks();
		
		HashSet<MIDINote> resultNotes = new HashSet<MIDINote>();
		
		int bpm = 0;
		boolean needBpm = true;
		for (int tracksNum = 0; tracksNum < tracks.length; tracksNum++) {
			Track track = tracks[tracksNum];
			
			int size = track.size();
			for (int i = 0; i < size; i++) {
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				
				if (message instanceof ShortMessage) {
					ShortMessage sm = (ShortMessage)message;
					
					int temp = sm.getData2();		
					if (needBpm && temp != 0) {
						bpm = temp;
						needBpm = false;
					} else {
						bpm = 100;
					}
					
					int tickRate = (int) (event.getTick() * 60000f / (sequence.getResolution() * bpm));
					
					MIDINote toAdd = null;
					if (sm.getCommand()==ShortMessage.NOTE_ON) {
						int key = sm.getData1();
	                    int octave = ((key / 12)-1);
	                    octave = (octave < 1) ? 1 : octave;
	                    octave = (octave > 2) ? 2 : octave;
	                    int note = (key % 12) + 1;
	                    
	                    toAdd = new MIDINote(note, octave, tickRate, sm.getChannel(), event.getTick());         
					} else if (sm.getCommand()==ShortMessage.NOTE_OFF) {
						toAdd = new MIDINote(-1, 0, tickRate, sm.getChannel(), event.getTick());
					}
					
					if (toAdd != null) {
						resultNotes.add(toAdd);
					}
					
					if (sm.getCommand()==ShortMessage.TIMING_CLOCK ||
							sm.getCommand()==ShortMessage.SONG_SELECT || 
							sm.getCommand()==ShortMessage.SONG_POSITION_POINTER) {
						needBpm = true;
					}
				}
			}
		}
		
		MIDINote[] finalResult = resultNotes.toArray(new MIDINote[resultNotes.size()]);
		return MIDIConverter.ConvertFromRaw(new RawMIDIFile(finalResult));
	}
	
	public static MIDIChannel[] SeparateToChannels(MIDIFile file) {
		List<Integer> channels = new ArrayList<Integer>();
		MIDINote[] notes = file.GetNotes();
		for (int i = 0; i < file.GetSize(); i++) {
			if (!channels.contains(notes[i].GetChannel())) {
				channels.add(channels.size());
			}
		}
		
		MIDIChannel[] result = new MIDIChannel[channels.size()];
		List<MIDINote> channelNotes = new ArrayList<MIDINote>();
		for (int i = 0; i < channels.size(); i++) {
			channelNotes.clear();
			for (int j = 0; j < file.GetSize(); j++) {
				if (notes[j].GetChannel()==channels.get(i)) {
					channelNotes.add(notes[j]);
				} else {
					channelNotes.add(new MIDINote(-1, 0, notes[j].GetMillisecondsTick(), notes[j].GetChannel(), notes[j].GetTickPosition()));
				}
			}
			
			MIDINote[] temp = new MIDINote[channelNotes.size()];
			for (int j = 0; j < channelNotes.size(); j++) {
				temp[j] = channelNotes.get(j);
			}
			result[i] = new MIDIChannel(channels.get(i), channelNotes.size(), temp);
		}
		return result;
	}
}

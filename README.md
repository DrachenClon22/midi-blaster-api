# MIDI Blaster API
## General Info
API for Minecraft Spigot plugins for decoding and playing MIDI files in game.
- Author: DrachenClon22
- Current Spigot version: 1.20
- Current version: 1.0.0

Documentation can be found at [Wiki page](https://github.com/DrachenClon22/midi-blaster-api/wiki)
## Description
This API can be used by server administration for playing any music to players, but there's no need to convert your files to .nbs format, API supports any raw .mid (MIDI) files.<br>
<br>
It is highly recommended using music in the F#0 to F#2 range, otherwise your melody will be inexorably and mercilessly transposed, that may cause unexpected sounding. All 16 channels and voices are also supported,
so polyphony is more or less possible, but do not forget that Minecraft is just a game, not an MP3 player, everything should be within reasonable limits, but no limits otherwise, so explore and have fun.<br>
<br>
.mdi (MIDI) file can be any size, but still within reasonable limits, so use this API wisely and everything will be fine.<br>
## Features
- Using raw .mid (MIDI) files, not .nbs or any converted format;
- Music can be played to specific player only or for everyone, people around user will not hear a one note;
- Different music can be played to different players, multi-threading support.
## Licence
This API can be freely used only in any open-source projects, not any commercial or closed or whatever. But if you want to, you can write me an email (drachenclon22@gmail.com).<br>
<br>
If you use this API anywhere, please, put me in credits (DrachenClon22) and write me an email (drachenclon22@gmail.com), I'd be really glad.
## Example
Example on [this video](https://www.youtube.com/watch?v=QE_6F3KzpT8) shows only music playing option.

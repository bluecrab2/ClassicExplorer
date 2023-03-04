# ClassicExplorer
This program reads all the information from Pre-Classic or Classic Minecraft files and displays them in a folder tree similar to [NBTExplorer](https://github.com/jaquadro/NBTExplorer/releases). The specific version range is rd-132211 (first version of Minecraft that has been released) to Classic 0.30 (last classic version). Indev and later versions use NBT format and so can be read with NBTExplorer.

## Installation
ClassicExplorer requires a Java version 8 or later to run. The jar should run by double-clicking it, or you can run it through the command prompt with the command `java -jar ClassicExplorer.jar` in its directory. For a video tutorial, check out my [Youtube tutorial video](https://youtu.be/-QrfYUrjVsQ).

## Settings
The settings tab has 5 options that are saved in the settings.txt file. These are the effects of all the settings:
* Dark Mode - Toggles between light and dark colors.
* Convert Unix Timestamp to Date - When enabled, this setting will convert the createTime field in the Level class from a Unix timestamp to a YYYY-MM-DD hh:mm:ss date in your time zone.
* Show Full Class Name - Will display the entire package of each class instead of just the end class name.
* Show SerialVersionUID - Will display the SerialVersionUID of classes next to them in parentheses. This number is not used in game at all, it is only used by the class when serializing into the file.
* Skip Blocks - Will skip the "blocks" field in the Level class. Defaults to true to avoid lag associated with creating usually millions of nodes for each block.
* Zoom - Increase or decrease the font. Can be done with the shortcuts CTRL + or CTRL - respectively.

## Level Save Format
All save files from Pre-Classic and Classic were compressed using the GZIP format. Inside the compressed file, the format changed twice and the information inside was changed regularly.

### rd-132211 to 0.0.12a_03
During these versions, there was no header or footer, it was just an array of blocks. 
| Data | Length (Bytes) |	Description |
| ---- | -------------- | ----------- |
| Block Array |	4194304 (2^22) | An array of blocks in the world in the order x -> z -> y from 0 to the limit. Each byte represents a single block with its ID. |

### Classic 0.0.13a-dev to Classic 0.0.13a_03
During these versions, there was only a header with a few pieces of information.
| Data | Length (Bytes) |	Description |
| ---- | -------------- | ----------- |
| Magic Number |	4 |	A number used to identify the file. Always will be `27 1B B7 88`. |
| Version |	1 |	A version identifier byte `01`. |
| World Name | Variable |	First two bytes will be a short for the length of the string then that length bytes will be ASCII characters for the string. This string will always be either "A Nice World" or "--". |
| Creator Player Name |	Variable |	First two bytes will be a short for the length of the string then that length bytes will be ASCII characters for the string. In Classic 0.0.13a-dev this will always be "noname" but Classic 0.0.13a_03 will be the actual user's name. |
| Time Created | 8 |	A long value for the time the level was created as a Unix timestamp. |
| Width |	2 |	The width of the world (x direction). |
| Height |	2 |	The height of the world (z direction). |
| Depth |	2 |	The depth of the world (y direction). |
| Block Array |	4194304 (2^22) |	An array of blocks in the world in the order x -> z -> y from 0 to the limit. Each byte represents a single block with its ID. | 

### Classic 0.0.14a_08 and later
During these versions, the file begins with these bytes: 
| Data | Length (Bytes) |	Description |
| ---- | -------------- | ----------- |
| Magic Number |	4 |	A number used to identify the file. Always will be `27 1B B7 88`. |
| Version |	1 |	A version identifier byte `02`. |

After the header bytes, Minecraft uses [Java's Serializable interface](https://docs.oracle.com/javase/6/docs/platform/serialization/spec/protocol.html) to save the `Level` class. The fields of the Level class changed throughout Classic versions many times.

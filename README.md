NamedSignEdit
=============

Name signs and edit them remotely

Usage:
/nse action parameter or
/namedsignedit action parameter

actions:
 - name:
  - when running as a player looking at the target sign: <name> (eg. /nse name test)
  - when not looking at the sign or form server console <name> <world> <x> <y> <z> (eg. /nse name test world -100 64 20) 
 - set <name> <line> <text> - line range 0-3 - (eg. /nse test 0 Hello!)
 - clear <name> [<line>] - if no line is given, all 4 lines are cleared - (eg. /nse clear test)
 - remove <name> - removes sign from plugin
 - x <line> [<text>] - shortcut to set sign without giving it a name - must be run by a player looking at the sign - if no text is given, the line is cleared - (eg. /nse x 0 Hello!)
 
The text can be formated with color codes (see http://ess.khhq.net/mc/).
The plugin will not check if the given text will fit the sign!

Permissions:
The player must be OP to run the commands.



# StaffNotes
[![Build Status](https://jenkins.shadow5353.com/job/StaffNotes/com.shadow5353$StaffNotes/badge/icon)](https://jenkins.shadow5353.com/job/StaffNotes/com.shadow5353$StaffNotes/)
[![Release Status](https://img.shields.io/github/release/shadow5353/StaffNotes.svg)](https://github.com/shadow5353/StaffNotes/releases)
[![Issues Status](https://img.shields.io/github/issues/shadow5353/StaffNotes.svg)](https://github.com/shadow5353/StaffNotes/issues)
## Description
Staff Notes let your staff easily make notes on your players! All the notes are saved in your Database so it can be used on multiple servers.

## Commands & Permissions:
- /Staffnotes
  - Info: Show a list of commands!
  - Permission: staffnotes.list
- /Staffnotes info
  - Info: Show information about the plugin!
  - Permission: staffnotes.info
- /Staffnotes add [Player] [Note message]
  - Info: Add a note onto a player!
  - Permission: staffnotes.add
- /Staffnotes show [Player]
  - Info: Show a list of notes on a player!
  - Permission: staffnotes.show
- /Staffnotes remove [Player] [NoteID]
  - Info: Remove a note from a player!
  - Permission: staffnotes.remove
- /Staffnotes removeall [Player]
  - Info: Remove all notes from a player!
  - Permission: staffnotes.removeall

## Installation:
Download the plugin and drag it into your plugins folder.
Run the server.
Create a MySQL Database and put in the information into the config.yml.
Reload the server and enjoy!

## To Do List:
- [ ] Add support for SQLite.
- [ ] Add support for flat file storage.
- [ ] Add GUI with players heads if they have a not.

## Useful Links:
- [Spigot Page](https://www.spigotmc.org/resources/staff-notes.33671/)
- [Development Builds](https://jenkins.shadow5353.com/job/StaffNotes/)

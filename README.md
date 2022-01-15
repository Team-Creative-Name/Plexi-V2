
# Plexi V2 - A Discord Bot for Overseerr

The second iteration of Plexi, A discord bot that integrates with [Overseerr](https://github.com/sct/overseerr) by sct. This project is still under development, but should be fairly stable.


## 
<div align="center">
  <br />
  <p>
    <a href="https://github.com/Team-Creative-Name/Plexi-V2/actions/workflows/buildMain.yml"><img src="https://github.com/Team-Creative-Name/Plexi-V2/actions/workflows/buildMain.yml/badge.svg"></a>
    <a href="https://github.com/Team-Creative-Name/Plexi-V2/issues"><img src="https://img.shields.io/github/issues/Team-Creative-Name/Plexi-V2" alt="Issues" /></a>
    <a href="https://github.com/Team-Creative-Name/Plexi-V2/blob/master/LICENSE"><img src="https://img.shields.io/github/license/Team-Creative-Name/Plexi-V2" alt="License" /></a>
  </p>
  
</div>


## Features - So Many Features!

- A GUI for monitoring the status of your bot
- Searching Overseerr for Television shows and movies
    - Results shown in an easy to use and information rich page-based menu system
- Adding search results to Overseerr's requests
- The ability to look up media via The Movie Database id numbers
- Viewing user requests
    - And the ability to filter them
- and more!
    - If we don't have any features that you might want, just make it yourself and contribute via a pull request or [make an issue for it!](https://github.com/Team-Creative-Name/Plexi-V2/issues/new/choose)


## Setup
Setting up plexi is easy! Upon the first run, A configuration file is generated in the same directory as the executable. You can [see what that file is supposed to look like here.](https://github.com/Team-Creative-Name/Plexi-V2/blob/main/src/main/resources/assets/config.txt)

The following items are needed for the config file:
 - [Overseerr API key](https://docs.overseerr.dev/using-overseerr/settings)
 - Overseerr URL (what you put into your browser's address bar to access Overseerr)
 - Discord Bot Token, obtained from the Discord developer portal
 - Your Discord user ID, obtained by right clicking on your username in discord with dev mode on

 Once all of that is put into the file, change any of the other settings, save, and launch plexi. If everything was put in correctly, you should see a gui with a start button. Pressing start will bring the bot online and have it listen to any commands. 
 
 *Note: due to how slash commands work with discord, they may not show up in your server for anywhere from 30 minutes to an hour after the bot has started. This will only happen once if at all. 

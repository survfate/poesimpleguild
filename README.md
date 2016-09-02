# PoESimpleGuild

![screenshot](https://cloud.githubusercontent.com/assets/10634948/13550005/c0c4c9aa-e345-11e5-8f48-1a1d538fe2b5.png)

A simple Guild management tool for the game Path of Exile. Written in Java.

## Download

[**Lastest Release**](https://github.com/survfate/poesimpleguild/releases/latest)

## Requirement

Java SE Runtime Environment for the `.jar` build. You can also use the standalone Windows build instead.

## Features

- Guild Members listing and management, making it convenient to check who is active / inactive in your guild.
- Showing Account Name, Member Type (Status), Challenge(s) Done, Total Forum Posts, Joined Date, Last Visted Date, Last Ladder Tracked, Supporter Tag(s), Poe.Trade Online and Profile URL (all sortable by clicking on the Column headers)
- Exportable to .xls file for more advance management activity.
- Support Multithread for faster job processing time.

**Note**: Last Ladder Tracked is a feature using the Ladder API provided by http://poestatistics.com/, it'll calculate the lastest date your most recent character is online on the Ladder (if the data is exist).

## Usages

Just run `poesimpleguild-x.y.z.jar` (it's a runable jar file, with x, y, z being the version numbers), enter the Guild ID which you can get from the Guild Page URL - `https://www.pathofexile.com/guild/profile/this_is_your_guild_id` or using a Profile Name to get the Guild which the Profile belong to, changing the options as you wish and press `Get Guild Members Data` button to start the job. 

## Libraries

- **BeautyEye LnF**: https://github.com/JackJiang2011/beautyeye

One of the best Java Swing cross-platform look and feel.

- **jsoup**: https://github.com/jhy/jsoup

This library is a Java HTML Parser for getting the elements we need from the website.

- **minimal-json**: https://github.com/ralfstx/minimal-json

A fast and small JSON parser and writer for Java.

- **MigLayout**: http://www.miglayout.com/

Easy to use yet very powerful Java Swing, JavaFX 2 and SWT layout manager.

- **Java Excel API**: http://jexcelapi.sourceforge.net/

A Java API to read, write, and modify Excel spreadsheets.

## Notes

- I tested this in my laptop running Windows 10 and it working fine, it might be able to run on Linux and Mac system with Java SE Runtime Environment installed but I haven't able to test that.  
- If there any problem with getting the tool run please let me know. Thanks a tons ;)  
- Please report any bugs if you found them, and also suggestions are alway welcome.

## To-do list

- Account Overview and Characters Details.  
- Guild Transactions listing (with "Purgeable?" information base on the last time an account has a points transfer to the guild).  
- And more if I think of any and has the time.

# PoESimpleGuild

![screenshot](https://cloud.githubusercontent.com/assets/10634948/13550005/c0c4c9aa-e345-11e5-8f48-1a1d538fe2b5.png)

A simple Guild management tool for the game Path of Exile. Written in Java.

## Download

[**Lastest Release**](https://github.com/survfate/PoESimpleGuild/releases)

## Requirement

Java SE Runtime Environment (some of you may not like having Java, forgive me then). Windows version is not guarantee for every release.

## Features

- Guild Members listing and management. Convenient to check who is active / inactive in your guild.
- Showing Account Name, Member Type (Status), Challenge(s) Done, Total Forum Posts, Joined Date, Last Visted Date, Last Ladder Online, Supporter Tag(s), Poe.Trade Online and Profile URL (all sortable by clicking on the Column headers)
- Exportable to .xls file for more advance management activity.

**Note**: Last Ladder Online is a feature using the Ladder API provided by http://exiletools.com/, it'll auto checking your account on all ladder (2 permanent one and 2 temporary one and special events if the API is supported for them) and getting the lastest online date it could get from said account.

## Usages

Just run `poesimpleguild-x.y.z.jar` (it's a runable jar file, with x, y, z being the version numbers), enter the Guild ID which you can get from the Guild Page URL - `https://www.pathofexile.com/guild/profile/this_is_your_guild_id`, tick the "Poe.Trade Online Check?" checkbox if you want and wait for it to complete loading.

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

- I tested this in my laptop running Windows 8.1 and it working fine, it might be able to run on Linux and Mac system with Java SE Runtime Environment installed but I haven't able to test that.  
- If there any problem with getting the tool run please let me know. Thanks.  
- Please report any bugs if you found them, and suggestions are alway welcome.

## To-do list

- Account Overview and Characters details.  
- Guild Transactions listing (with "Kickable?" information base on the last time an account has a points transfer to the guild).  
- And more if I think of any and has the time for it.

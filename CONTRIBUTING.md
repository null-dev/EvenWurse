[![Creative Commons License](https://i.creativecommons.org/l/by-sa/4.0/88x31.png)](#credits)

# Content
1. [Code of Conduct](#code-of-conduct)
2. [Issues](#issues)
3. [Pull Requests](#pull-requests)
4. [Credits](#credits)

# Code of Conduct
- **Think before you react.** If you disagree strongly, consider giving it a few minutes before responding.

- **Ask questions, don't make demands.** ("What do you think about trying...?" rather than "Don’t do...!")

- **Don't insult.** Avoid using terms that could be seen as referring to personal traits. ("dumb", "stupid"). Assume everyone is attractive, intelligent, and well-meaning.

- **Be explicit.** Remember people don't always understand your intentions online.

- **Be humble.** ("I'm not sure - let's look it up.")

- **Don't use hyperbole.** ("always", "never", "endlessly", "nothing")

- **Use emoji to clarify tone.** (":sparkles::sparkles: Looks good :+1: :sparkles::sparkles:" rather than "Looks good.")

# Issues
## Before writing an Issue

[**Watch this tutorial**](https://www.youtube.com/watch?v=TJlYiMp8FuY) if you have never used GitHub's Issue tracker before. It only takes 3 minutes!

[![](https://i.ytimg.com/vi/TJlYiMp8FuY/mqdefault.jpg)](https://www.youtube.com/watch?v=TJlYiMp8FuY)

#### Always search your Issue before opening a new one. Creating duplicates is slower for everyone!

## Writing an Issue
### The Title

- #### Others should be able to guess what the Issue is about by reading its title.

- Try to use less than 40 characters.

- **Avoid writing `Wurst`** in the title. It is obvious that your Issue is about Wurst.

- If the title gets too long, remove words like `the` and `a`.

#### Examples:

- ".help 1" command throwing NullPointerException :white_check_mark: **good title**

- WUrst bug FIX UP!!1 (╯°□°）╯︵ ┻━┻ :x: **bad title**

### The Body

- #### Don't include opinions or judgements, only information.

- **Use proper grammar & Markdown.** If no one can read it, no one can help you.  
If you have never used markdown before, you can [learn it in 3 minutes](https://guides.github.com/features/mastering-markdown/).

- **Don't put multiple Issues into one issue.** That makes them impossible organize and will likely get your issue ignored or removed.

- **Be clear** about problems: What was the expected outcome, what happened instead? Detail how someone else can recreate the problem.

- **Include screenshots** if your Issue is about something visible. Embed the screenshots directly into your Issue, don't link to external images.

- **Include system details** like the Java version and operating system you’re using.

- **Paste error output** like Java stacktraces or Minecraft launcher logs into your Issue. Wrap it into a Markdown code block, like this:

<pre>```
java.lang.NullPointerException: null
  at org.apache.logging.log4j.core.config.AppenderControl.callAppender(AppenderControl.java:89)
	at org.apache.logging.log4j.core.config.LoggerConfig.callAppenders(LoggerConfig.java:425)
	at org.apache.logging.log4j.core.config.LoggerConfig.log(LoggerConfig.java:406)
	at org.apache.logging.log4j.core.config.LoggerConfig.log(LoggerConfig.java:367)
	at org.apache.logging.log4j.core.Logger.log(Logger.java:110)
```</pre>

#### Always include stacktraces or crash logs when reporting errors or crashes.

If it's too long to paste it into the Issue, paste it into a [Gist](https://gist.github.com/).

#### Examples:

> :white_check_mark: **good Issue**
>
> # Description
> After typing the command `.nuker mode flat` while Nuker was still enabled and in Smash mode, an exception was thrown (see below).
> 
> This seems to happen every time, but only if Nuker is enabled.
> 
> # Stacktrace
> ```
> java.lang.ArrayIndexOutOfBoundsException: 1
> 	at tk.wurst_client.command.commands.NukerMod.onEnable(NukerMod.java:34)
> 	at tk.wurst_client.command.CommandManager.onSentMessage(CommandManager.java:40)
> 	at tk.wurst_client.event.EventManager.fireEvent(EventManager.java:137)
> 	at net.minecraft.client.entity.EntityPlayerSP.sendChatMessage(EntityPlayerSP.java:292)
> 	at net.minecraft.client.gui.GuiScreen.func_175281_b(GuiScreen.java:487)
> 	at net.minecraft.client.gui.GuiScreen.func_175275_f(GuiScreen.java:477)
> 	at net.minecraft.client.gui.GuiChat.keyTyped(GuiChat.java:132)
> 	at net.minecraft.client.gui.GuiScreen.handleKeyboardInput(GuiScreen.java:617)
> 	at net.minecraft.client.gui.GuiScreen.handleInput(GuiScreen.java:568)
> 	at net.minecraft.client.Minecraft.runTick(Minecraft.java:2024)
> 	at net.minecraft.client.Minecraft.runGameLoop(Minecraft.java:1354)
> 	at net.minecraft.client.Minecraft.run(Minecraft.java:468)
> 	at net.minecraft.client.main.Main.main(Main.java:127)
> ```
> 
> # System details
> - OS: Windows 8.1 (x86)
> - Java version: 1.8.0_31 (Oracle Corporation)
> - Wurst version: 1.9 (latest: 1.9)

---
> :x: **bad Issue**
> 
> Me got error, this sooooooo buggy!!1!!!11!!11111!!!!!!!!!!!!!!!!!!!!!!!1!!  
> (╯°□°）╯︵ ┻━┻  
> Fix ASAP and add OptiFine or I will uninstall!!!!  
> 
> PS subscribe me on YouTube and Facebook

## After writing an Issue

- **Respond to questions**, especially if you get the `more info required` label.

- **Don't get angry** or needy if your Issue gets rejected. We are free to reject any Issue for any reason.

# Pull Requests
> **Note:** There haven't been many Pull Requests yet, so these guidelines are based on predictions rather than experience.

- [**Read the tutorial**](https://guides.github.com/activities/forking/) if you have never forked a repository before.  
[![](https://github-images.s3.amazonaws.com/help/bootcamp/Bootcamp-Fork.png)](https://guides.github.com/activities/forking/)

- **Fork the repository** and clone it locally.

- **Pull in changes** from the original often so that you stay up to date. That way, when you submit your pull request, merge conflicts will be less likely.

- **Create a branch** for your edits.

- **Be clear** about what problem is occurring and how someone can recreate that problem or why your feature will help. Then be equally as clear about the steps you took to make your changes.

- **It’s best to test.** Run your changes against any existing tests if they exist and create new ones when needed. Whether tests exist or not, make sure your changes don’t break the existing project.

- **Include screenshots** of the before and after if your changes include visible differences. Drag and drop the images into the body of your pull request.

- **Keep the style** of the project to the best of your abilities. This may mean using indents, semi colons or comments differently than you would in your own repository, but makes it easier for the maintainer to merge, others to understand and maintain in the future.

# Credits
[![Creative Commons License](https://i.creativecommons.org/l/by-sa/4.0/88x31.png)](http://creativecommons.org/licenses/by-sa/4.0/)

These guidelines by Alexander01998 are licensed under a [Creative Commons Attribution-ShareAlike 4.0 International License](http://creativecommons.org/licenses/by-sa/4.0/).

These guidelines are partially based on the following guides and guidelines:
- [thoughtbot Inc: Code Review](https://github.com/thoughtbot/guides/tree/master/code-review)
- [Atom editor: Contributing guidelines](https://github.com/atom/atom/blob/master/CONTRIBUTING.md)
- [The GitHub Blog: How to write the perfect Pull Request](https://github.com/blog/1943-how-to-write-the-perfect-pull-request)
- [AngularJS: Contributing guidelines](https://github.com/angular/angular.js/blob/master/CONTRIBUTING.md)

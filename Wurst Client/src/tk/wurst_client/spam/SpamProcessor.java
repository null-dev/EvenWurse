/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.spam;

import java.awt.HeadlessException;

import javax.swing.JOptionPane;

import tk.wurst_client.module.modules.Spammer;
import tk.wurst_client.spam.exceptions.InvalidVariableException;
import tk.wurst_client.spam.exceptions.SpamException;
import tk.wurst_client.spam.exceptions.UnreadableTagException;
import tk.wurst_client.spam.exceptions.UnreadableVariableException;
import tk.wurst_client.spam.tag.TagData;
import tk.wurst_client.spam.tag.TagManager;
import tk.wurst_client.utils.MiscUtils;

public class SpamProcessor
{
	public static TagManager tagManager = new TagManager();
	public static VarManager varManager = new VarManager();
	
	public static String process(String spam, Spammer spammer, boolean test)
	{
		try
		{
			log("### Cleaning up variables...");
			varManager.clearUserVars();
			log("### Processing spam...");
			final String source = new String(spam);
			log("### Processing comments...");
			if(test)
				spam = spam.replace("<!--", "#!--");
			else
				spam = spam.replaceAll("(?s)<!--.*?-->", "");
			log("** Processed comments:\n" + spam);
			log("### Processing tags...");
			while(spam.contains("<"))
			{
				log("** Processing tag...");
				int tagStart = spam.indexOf("<");
				log("TagStart: " + tagStart);
				int tagLine = MiscUtils.countMatches(spam.substring(0, tagStart), "\n");
				log("TagLine: " + tagLine);
				String tag;
				String tagName = null;
				tag = spam.substring(tagStart);
				try
				{
					tagName = tag.substring(1, tag.indexOf(">")).split(" ")[0];
				}catch(StringIndexOutOfBoundsException e1)
				{
					throw new UnreadableTagException(source.substring(tagStart), tagLine);
				}
				log("TagName: " + tagName);
				String[] tagArgs;
				try
				{
					tagArgs = tag.substring(tagName.length() + 2, tag.indexOf(">")).split(" ");
				}catch(StringIndexOutOfBoundsException e)
				{
					tagArgs = new String[0];
				}
				log("TagArgs:");
				for(int i = 0; i < tagArgs.length; i++)
					log("No. " + i + ": " + tagArgs[i]);
				String tmpTag = new String(tag);
				int tmpSubTags = 0;
				int tagLength = tag.length();
				boolean tagClosed = false;
				int tagContentLength = tag.length();
				log("+ Calculating TagLength...");
				while(tmpTag.contains("<"))
				{
					log("Found subtag: " + tmpTag.substring(tmpTag.indexOf("<"), tmpTag.indexOf("<") + 2) + " at " + tmpTag.indexOf("<"));
					if(tmpTag.substring(tmpTag.indexOf("<") + 1, tmpTag.indexOf("<") + 2).equals("/"))
						tmpSubTags--;
					else
						tmpSubTags++;
					log("Subtags left: " + tmpSubTags);
					if(tmpSubTags == 0)
					{
						tagLength = tmpTag.indexOf("<") + tagName.length() + 3;
						tagContentLength = tmpTag.indexOf("<");
						log("TagContentLength: " + tagContentLength);
						tmpTag = tmpTag.replaceFirst("<", "#");
						tagClosed = true;
						break;
					}
					tmpTag = tmpTag.replaceFirst("<", "#");
				}
				log("TagLength: " + tagLength);
				tag = tag.substring(0, tagLength);
				log("Raw Tag:\n" + tag);
				String tagContent = tag.substring(tag.indexOf(">") + 1, tagContentLength);
				log("TagContent: " + tagContent);
				TagData tagData = new TagData(tagStart, tagLength, tagLine, tagName, tagArgs, tagClosed, tag, tagContent, tagContentLength, spam);
				String tagReplacement = tagManager.process(tagData);
				if(test)
					spam = spam.substring(0, tagStart) + (tagClosed ? tag.replaceFirst("<", "#").replaceFirst("(?s)(.*)<", "$1#") : tag.replaceFirst("<", "#")) + spam.substring(tagStart + tagLength, spam.length());
				else
					spam = spam.substring(0, tagStart) + tagReplacement + spam.substring(tagStart + tagLength, spam.length());
				log("** Processed tag:\n" + spam);
			}
			log("### Processing variables...");
			while(spam.contains("§"))
			{
				log("** Processing variable...");
				int varStart = spam.indexOf("§");
				log("VarStart: " + varStart);
				int varLine = MiscUtils.countMatches(spam.substring(0, varStart), "\n");
				log("VarLine: " + varLine);
				int varEnd = spam.indexOf(";", varStart) + 1;
				log("VarEnd: " + varEnd);
				String var = spam.substring(varStart);
				try
				{
					if(varEnd <= 0)
						throw new Exception();
					var = spam.substring(varStart, varEnd);
				}catch(Exception e)
				{
					throw new UnreadableVariableException(source.substring(varStart), varLine);
				}
				log("Var: " + var);
				String varName = spam.substring(varStart + 1, varEnd - 1);
				log("VarName: " + varName);
				log("** Processed variable:\n" + spam);
				String varReplacement = varManager.getValueOfVar(varName);
				if(varReplacement == null)
					throw new InvalidVariableException(varName, varLine);
				if(test)
					spam = spam.substring(0, varStart) + var.replace("§", "*") + spam.substring(varEnd, spam.length());
				else
					spam = spam.substring(0, varStart) + varReplacement + spam.substring(varEnd, spam.length());
			}
			log("### Final Spam:\n" + spam);
		}catch(SpamException e)
		{
			if(!test)
				return null;
			if(spammer == null)
			{
				e.printStackTrace();
				return null;
			}
			String message = e.getClass().getSimpleName() + " at line " + (e.line + 1) + ":\n" + e.getMessage();
			switch(JOptionPane.showOptionDialog(spammer.getDialog(), message, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{"Go to line", "Show help"}, 0))
			{
				case 0:
					spammer.goToLine(e.line);
					break;
				case 1:
					try
					{
						JOptionPane.showOptionDialog(spammer.getDialog(), e.getHelp(), "Help", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"OK"}, 0);
					}catch(HeadlessException e1)
					{
						e1.printStackTrace();
					}
					break;
				
				default:
					break;
			}
			return null;
		}catch(Exception e)
		{
			System.err.println("Unknown exception in SpamProcessor:");
			e.printStackTrace();
			return null;
		}
		return spam;
	}
	
	private static void log(String log)
	{
		if(!"".isEmpty())// Manual switch for debugging
			System.out.println(log);
	}
}

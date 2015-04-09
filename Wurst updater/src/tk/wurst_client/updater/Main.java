/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.updater;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URLEncoder;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main
{
	public static final File CURRENT_DIRECTORY = new File(Main.class
		.getProtectionDomain().getCodeSource().getLocation().getPath());
	
	public static void main(String[] args)
	{
		if(args == null || args.length == 0)
			return;
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			
		}catch(Exception e)
		{
			StringWriter stacktraceWriter = new StringWriter();
			e.printStackTrace(new PrintWriter(stacktraceWriter));
			String trace = stacktraceWriter.toString();
			try
			{
				switch(JOptionPane.showOptionDialog(null,
					"An error occurred while updating Wurst.", "Error",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
					null, new String[]{"Report on GitHub", "Report via e-mail",
						"Copy stack trace", "Do nothing"}, 0))
				{
					case 0:
						Desktop.getDesktop().browse(
							new URI(
								"https://github.com/Wurst-Imperium/Wurst-updater/issues/new?title="
									+ URLEncoder.encode(
										"Error report: " + e.getMessage(),
										"UTF-8")
									+ "&body="
									+ URLEncoder.encode("# Description\n"
										+ "Auto-generated error report.\n\n"
										+ "# Stacktrace\n" + "```\n" + trace
										+ "```", "UTF-8")));
						break;
					case 1:
						Desktop.getDesktop().browse(
							new URI(
								"mailto:contact.wurstimperium@gmail.com?subject="
									+ URLEncoder.encode(
										"Wurst updater - Error report",
										"UTF-8")
									+ "&body="
									+ URLEncoder.encode("Description:\n"
										+ "Auto-generated error report.\n\n"
										+ "Stacktrace:\n" + trace, "UTF-8")));
						break;
					case 2:
						Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(trace), null);
						break;
					default:
						break;
				}
			}catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}
}

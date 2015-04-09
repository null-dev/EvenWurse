/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.updater;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class PleaseWaitDialog extends JDialog
{
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			PleaseWaitDialog dialog = new PleaseWaitDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public PleaseWaitDialog()
	{
		setUndecorated(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		Component glue = Box.createGlue();
		getContentPane().add(glue);
		
		JLabel lblImage = new JLabel("");
		lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setIcon(new ImageIcon(PleaseWaitDialog.class.getResource("/tk/wurst_client/updater/wurst_253x64.png")));
		getContentPane().add(lblImage);
		{
			JLabel lblPleaseWait = new JLabel("<html>\r\n<center>\r\n<h1>Please Wait...</h1>\r\n<p>Wurst is installing an update.</p>\r\n<p>This should only take a couple of seconds.</p>");
			lblPleaseWait.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblPleaseWait.setFont(new Font("Verdana", Font.PLAIN, 16));
			getContentPane().add(lblPleaseWait);
			lblPleaseWait.setHorizontalAlignment(SwingConstants.CENTER);
		}
		
		Component glue_1 = Box.createGlue();
		getContentPane().add(glue_1);
	}
	
}

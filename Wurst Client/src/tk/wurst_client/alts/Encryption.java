/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.alts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

import net.minecraft.client.Minecraft;
import tk.wurst_client.Client;
import tk.wurst_client.utils.MiscUtils;

public class Encryption
{
	private static SecretKey aesKey;
	private static File aesFile = new File(Client.wurst.fileManager.wurstDir, "key");
	
	private static KeyPair keypair;
	private static File privateFile = System.getProperty("user.home") != null ? new File(System.getProperty("user.home") + "\\.ssh\\wurst_rsa") : null;
	private static File publicFile = System.getProperty("user.home") != null ? new File(System.getProperty("user.home") + "\\.ssh\\wurst_rsa.pub") : null;

	public static String encrypt(String string)
	{
		checkKeys();
		try
		{
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			return new String(cipher.doFinal(string.getBytes()));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String string)
	{
		checkKeys();
		try
		{
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			return new String(cipher.doFinal(string.getBytes()));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private static void checkKeys()
	{
		if(!hasKeyFiles())
			generateKeys();
		if(!hasKey())
			loadKeys();
	}

	private static boolean hasKeyFiles()
	{
		return privateFile != null
			&& privateFile.exists()
			&& publicFile != null
			&& publicFile.exists()
			&& aesFile != null
			&& aesFile.exists();
	}
	
	private static boolean hasKey()
	{
		return keypair != null
			&& keypair.getPrivate().getEncoded() != null
			&& keypair.getPublic().getEncoded() != null
			&& aesKey.getEncoded() != null;
	}

	public static void generateKeys()
	{
		try
		{
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			keygen.init(128);
			aesKey = keygen.generateKey();
			KeyPairGenerator keypairgen = KeyPairGenerator.getInstance("RSA");
			keypairgen.initialize(4096);
			keypair = keypairgen.generateKeyPair();
			if(privateFile == null || publicFile == null)
			{
				JOptionPane.showMessageDialog(null, "Cannot create RSA key.\nThis is a bug, please report it!", "Error", JOptionPane.ERROR_MESSAGE);
				MiscUtils.openLink("https://github.com/Wurst-Imperium/Wurst-Client/issues?q=cannot+create+RSA+key");
				Minecraft.getMinecraft().shutdown();
				return;
			}
			if(!privateFile.exists())
			{
				if(!privateFile.getParentFile().exists())
					privateFile.getParentFile().mkdirs();
				ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream(privateFile));
				save.writeObject(keypair.getPrivate().getEncoded());
				save.close();
			}
			if(!publicFile.exists())
			{
				if(!publicFile.getParentFile().exists())
					publicFile.getParentFile().mkdirs();
				ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream(publicFile));
				save.writeObject(keypair.getPublic().getEncoded());
				save.close();
			}
			if(aesFile  == null)
			{
				JOptionPane.showMessageDialog(null, "Cannot create AES key.\nThis is a bug, please report it!", "Error", JOptionPane.ERROR_MESSAGE);
				MiscUtils.openLink("https://github.com/Wurst-Imperium/Wurst-Client/issues?q=cannot+create+AES+key");
				Minecraft.getMinecraft().shutdown();
				return;
			}
			if(!aesFile.exists())
			{
				if(!aesFile.getParentFile().exists())
					aesFile.getParentFile().mkdirs();
				Cipher rsaCipher = Cipher.getInstance("RSA");
				rsaCipher.init(Cipher.ENCRYPT_MODE, keypair.getPublic());
				Files.write(aesFile.toPath(), rsaCipher.doFinal(aesKey.getEncoded()));
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void loadKeys()
	{
		try
		{
			if(privateFile == null || publicFile == null)
			{
				JOptionPane.showMessageDialog(null, "Cannot load RSA key.\nThis is a bug, please report it!", "Error", JOptionPane.ERROR_MESSAGE);
				MiscUtils.openLink("https://github.com/Wurst-Imperium/Wurst-Client/issues?q=cannot+load+RSA+key");
				Minecraft.getMinecraft().shutdown();
			}
			if(hasKeyFiles())
				generateKeys();
			else
			{
				final byte[] loadedPrivateKey = Files.readAllBytes(privateFile.toPath());
				final byte[] loadedPublicKey = Files.readAllBytes(publicFile.toPath());
				Cipher rsaCipher = Cipher.getInstance("RSA");
				rsaCipher.init(Cipher.DECRYPT_MODE, keypair.getPrivate());
				final byte[] loadedAesKey = rsaCipher.doFinal(Files.readAllBytes(aesFile.toPath()));
				keypair = new KeyPair(new PublicKey()
				{
					@Override
					public String getFormat()
					{
						return null;
					}
					
					@Override
					public byte[] getEncoded()
					{
						return loadedPublicKey;
					}
					
					@Override
					public String getAlgorithm()
					{
						return "RSA";
					}
				}, new PrivateKey()
				{
					@Override
					public String getFormat()
					{
						return null;
					}
					
					@Override
					public byte[] getEncoded()
					{
						return loadedPrivateKey;
					}
					
					@Override
					public String getAlgorithm()
					{
						return "RSA";
					}
				});
				aesKey = new SecretKey()
				{
					@Override
					public String getFormat()
					{
						return null;
					}
					
					@Override
					public byte[] getEncoded()
					{
						return loadedAesKey;
					}
					
					@Override
					public String getAlgorithm()
					{
						return "AES";
					}
				};
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

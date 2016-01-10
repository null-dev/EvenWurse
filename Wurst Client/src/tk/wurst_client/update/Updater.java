/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.update;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.wurst_client.WurstClient;
import tk.wurst_client.utils.JsonUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Updater {
    private static final Logger logger = LogManager.getLogger();
    private boolean outdated;

    private int currentVersion = WurstClient.EW_VERSION_CODE;
    private int latestVersion = -1;

    public void checkForUpdate() {
        try {
            outdated = false;
            HttpsURLConnection connection =
                    (HttpsURLConnection)new URL(
                            "https://api.github.com/repos/null-dev/EvenWurse/releases/latest")
                            .openConnection();
            BufferedReader load =
                    new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
            String content = load.readLine();
            for(String line; (line = load.readLine()) != null;)
                content += "\n" + line;
            load.close();
            JsonObject latestRelease = JsonUtils.jsonParser.parse(content).getAsJsonObject();
            try {
                latestVersion = Integer.parseInt(latestRelease.get("tag_name").getAsString());
            } catch(NumberFormatException e) {
                logger.warn("Latest version code is invalid!");
            }
            outdated = latestVersion > currentVersion;
            if(outdated)
                logger.info("Update found: " + latestVersion);
            else
                logger.info("No update found.");
        } catch(Exception e) {
            logger.error("Unable to check for updates!", e);
        }
    }

    //TODO Finish cross-platform auto update
    /*public void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if((char)getClass().getClassLoader()
                            .getResourceAsStream("assets/minecraft/wurst/updater")
                            .read() == "0".toCharArray()[0])
                        return;
                    File updater =
                            new File(Updater.class.getProtectionDomain()
                                    .getCodeSource().getLocation().getPath());
                    if(!updater.isDirectory())
                        updater = updater.getParentFile();
                    updater = new File(updater, "Wurst-updater.jar");
                    updater =
                            new File(updater.getAbsolutePath().replace("%20", " "));
                    InputStream input =
                            getClass().getClassLoader().getResourceAsStream(
                                    "assets/minecraft/wurst/Wurst-updater.jar");
                    FileOutputStream output = new FileOutputStream(updater);
                    byte[] buffer = new byte[8192];
                    for(int length; (length = input.read(buffer)) != -1;)
                        output.write(buffer, 0, length);
                    input.close();
                    output.close();
                    String id;
                    if(currentPreRelease > 0)
                        id =
                                json.get(0).getAsJsonObject().get("id")
                                        .getAsString();
                    else
                        id =
                                JsonUtils.jsonParser
                                        .parse(
                                                new InputStreamReader(
                                                        new URL(
                                                                "https://api.github.com/repos/null-dev/EvenWurse/releases/latest")
                                                                .openStream())).getAsJsonObject()
                                        .get("id").getAsString();
                    ProcessBuilder pb =
                            new ProcessBuilder("cmd.exe", "/c", "java", "-jar",
                                    updater.getAbsolutePath(), "update", id, updater
                                    .getParentFile().getAbsolutePath()
                                    .replace(" ", "%20"));
                    pb.redirectErrorStream(true);
                    Process p = pb.start();
                    BufferedReader pInput =
                            new BufferedReader(new InputStreamReader(p
                                    .getInputStream()));
                    for(String message; (message = pInput.readLine()) != null;)
                        System.out.println(message);
                    pInput.close();
                } catch(Exception e) {
                    logger.error("Could not update!", e);
                }
            }
        }).start();
    }*/

    public boolean isOutdated() {
        return outdated;
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public int getLatestVersion() {
        return latestVersion;
    }
}

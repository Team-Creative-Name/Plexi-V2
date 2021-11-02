/*
 *  Copyright (C) 2021 Team Creative Name, https://github.com/Team-Creative-Name
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.tcn.plexi;

import com.github.tcn.plexi.discordBot.DiscordBot;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;


/**
 * The Settings class for Plexi
 * <br>
 * This class follows a singleton pattern in an attempt to stop superfluous reloading of the settings text file located
 * in the same folder as the Plexi jar. There is currently NO way to change any of these settings values once they are set.
 */
public class Settings {

    //Plexi Icon - Hosted by discord which means no loading external media. For discord use ONLY
    private final String HOSTED_ICON_URL = "https://cdn.discordapp.com/attachments/675899155083952148/736485323663474728/Plexi_icon_512x.png";


    //reference to this object - the only one
    private static Settings settingsInstance = null;


    //discord bot settings. All loaded from config file. All have a get method attached
    private String token = null; //discord token.
    private String prefix = null; //discord command prefix.
    private String overseerrUrl = null; //URL to the overseerr api plexi should use for media
    private String overseerrKey = null; //Key to the overseerr api
    private String ownerId = null; //ID of the discord user who is running the bot
    private Boolean usersViewRequests; //Lets users view overseerr requests if set to true.
    private Boolean sendErrorReports; //If enabled, plexi will send the owner of the bot a message when it encounters a command error


    //Versioning information - pulled from /resources/assets/comithash.txt (hopefully)
    private String versionNumber = "UNKNOWN"; //current version of Plexi
    private String parentHash = "UNKNOWN"; //Hash of the parent commit on github
    private String branchName = "UNKNOWN"; //Name of the branch this version of plexi is on

    private List<String> splashList = null; //A list of all of the splashes located inside /resources/assets/splashes.plexi


    //Helper variables. None of these are accessed outside of this class
    private URL internalConfigPath; //reference to the location of the template config file in /resources/assets
    private URL commitHashFilePath = null;
    private URL splashFilePath = null; //The Path of the splashes.plexi file in /resources/assets
    private Path jarPath; //The path of the Plexi jarfile on the user's filesystem.
    private Path userConfigPath; //The path of the config file on the user's filesystem
    private Logger plexiLogger = null; //reference to the main plexi logger
    DiscordBot discordBot = DiscordBot.getInstance(); //reference to the discord bot


    /**
     * No other classes are allowed to instantiate this class
     */
    //privatized constructor to ensure that nothing else is able to instantiate this class
    private Settings() {
        //calls initVariables
        initVariables();
    }

    /**
     * Returns the Settings object associated with this instance of Plexi. Allows any class to get a copy of what was contained in the settings file
     * without having to reload it every time we wanted to get relevant information
     *
     * @return The {@link Settings} object associated with this instance of Plexi
     */
    //this is the only way for an outside class to obtain a reference to this object
    public static Settings getInstance() {
        //if the object does not exist, create it
        if (settingsInstance == null) {
            settingsInstance = new Settings();
        }
        //return the object reference
        return settingsInstance;
    }

    /**
     * Attempts to load the associated `config.txt` for this Plexi instance and reads from it. If it finds invalid information,
     * it informs the user and terminates execution. If it is unable to locate the config file, it will call {@link Settings#generateConfigFile()}
     * in an attempt to create a new one.
     */
    //attempt to set all of the global variables
    private void initVariables() {
        try {
            //first attempt to get the resource path and jar path
            jarPath = new File(Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).toPath();
            internalConfigPath = this.getClass().getResource("/assets/config.txt");

            //now attempt to read from the configuration file and set values accordingly
            //if using Mac OS && are using the application bundle, we need to change the config and log paths
            if (System.getProperty("os.name").toLowerCase().contains("mac") && jarPath.getParent().getParent().getParent().toString().contains(".app")) {
                //this should only fire if the jar is wrapped inside of a .app bundle
                //set the logger to the same folder that the .app is located in
                System.setProperty("LOG_PATH", jarPath.getParent().getParent().getParent().getParent().toString());
                //now attempt to load the config file
                userConfigPath = jarPath.getParent().getParent().getParent().getParent().resolve("config.txt");
            } else {
                //If not running within an application bundle, the user should have direct access to the jar. Set Everything to its path.
                userConfigPath = jarPath.getParent().resolve("config.txt");
                //set the log path as well
                System.setProperty("LOG_PATH", jarPath.getParent().toString());
            }

            //now that we know where the logger needs to be, go ahead and create it
            plexiLogger = LoggerFactory.getLogger("Plexi");

            FileInputStream config = new FileInputStream(userConfigPath.toString());
            Properties properties = new Properties();

            properties.load(config);

            //Load settings
            token = properties.getProperty("token").replaceAll("^\"|\"$", "");
            plexiLogger.info("Bot Token: " + token);
            ownerId = properties.getProperty("ownerID").replaceAll("^\"|\"$", "");
            plexiLogger.info("Owner ID: " + ownerId);
            prefix = properties.getProperty("prefix").replaceAll("^\"|\"$", "");
            overseerrUrl = properties.getProperty("overseerrURL").replaceAll("^\"|\"$", "");
            overseerrKey = properties.getProperty("overseerrKey").replaceAll("^\"|\"$", "");
            usersViewRequests = Boolean.valueOf(properties.getProperty("usersViewRequests").replaceAll("^\"|\"$", "").toLowerCase());
            sendErrorReports = Boolean.valueOf(properties.getProperty("generateErrorReports").replaceAll("^\"|\"$", "").toLowerCase(Locale.ROOT));

            if (!validateSettings()) {
                JOptionPane.showMessageDialog(null, "The config file contains invalid settings, please check it and try again.", "Plexi - Configuration Issue", JOptionPane.INFORMATION_MESSAGE);
                plexiLogger.error("Invalid settings found in the configuration file. Plexi must exit");
                System.exit(0);
            }

        } catch (FileNotFoundException e) {
            plexiLogger.error("Unable to locate existing configuration file!");
            generateConfigFile();
            plexiLogger.info("A new configuration file has been generated at: " + userConfigPath.toString());
            JOptionPane.showMessageDialog(null, "The config file was unable to be found. A new one has been generated at: " + userConfigPath.toString(), "Plexi - Configuration Issue", JOptionPane.INFORMATION_MESSAGE);
            discordBot.stopBot();
            plexiLogger.info("Please fill out the configuration file and restart Plexi");
            System.exit(0);

        } catch (NullPointerException e){
            //This most likely means that the settings file is missing a value or something. Inform the user and have them delete the old settings file
            plexiLogger.error("Missing Key/Value pair in settings file! Please delete the settings file and let Plexi generate a new one");
            plexiLogger.trace(Arrays.toString(e.getStackTrace()));
            //pop open a dialog box to ensure the user is aware of what happened
            JOptionPane.showMessageDialog(null, "Missing Key/Value pair in settings file! Please delete the settings file and let Plexi generate a new one", "Plexi - Configuration Error", JOptionPane.INFORMATION_MESSAGE);
            //after the user closes that, shut down the bot if it is running and terminate the program
            discordBot.stopBot();
            System.exit(-1);
        } catch (Exception e) {
            //if we cant get these two, there is absolutely no way to continue program execution. We inform the user of an issue and terminate
            plexiLogger.error("Error reading the settings file: " + e.getLocalizedMessage());
            plexiLogger.trace(Arrays.toString(e.getStackTrace()));
            //pop open a dialog box to ensure the user is aware of what happened
            JOptionPane.showMessageDialog(null, "Unknown error, unable to continue program execution. Error: " + Arrays.toString(e.getStackTrace()) + "\nCheck the log for a bit more info (Hopefully)", "Plexi - Unknown Error", JOptionPane.INFORMATION_MESSAGE);
            //after the user closes that, shut down the bot if it is running and terminate the program
            discordBot.stopBot();
            System.exit(-1);
        }
        plexiLogger.info("Settings file loaded successfully!");

        //now we want to attempt to load the splash file
        try{
            splashFilePath = this.getClass().getResource("/assets/splashes.plexi");

            //open an inputsteam and loop through the steam until it ends
            InputStream splashStream = splashFilePath.openStream();
            int current;
            StringBuilder currentQuote = new StringBuilder();
            splashList = new ArrayList<>();

            while((current = splashStream.read()) != -1){
                if(current == 10){
                    splashList.add(currentQuote.toString());
                    currentQuote = new StringBuilder();
                }else{
                    currentQuote.append((char) current);
                }
            }
            //We will still have one quote left in the currentQuote var, so add that to the list
            splashList.add(currentQuote.toString());
            splashStream.close();

        } catch (FileNotFoundException e) {
            plexiLogger.error("Unable to locate splashes file, they will be disabled until found. Try re-downloading the release.");
            plexiLogger.trace(Arrays.toString(e.getStackTrace()));
        } catch (Exception e) {
            plexiLogger.error("Unknown issue when attempting to load splashes file, they will be disabled. Try re-downloading the release.\n" + e.getMessage() + "\n" + e);

        }

        //finally we want to load the commithash file
        //now we want to attempt to load the splash file
        try{
            commitHashFilePath = this.getClass().getResource("/assets/commithash.txt");
            InputStream versionInput = commitHashFilePath.openStream();
            Properties versioningInfo = new Properties();
            versioningInfo.load(versionInput);

            //load version & parent hash so we can store it in obj
            versionNumber = versioningInfo.getProperty("Version");
            parentHash = versioningInfo.getProperty("CHash");
            branchName = versioningInfo.getProperty("Branch");

            versionInput.close();

        } catch (FileNotFoundException e) {
            plexiLogger.error("Unable to locate version file. Please try re-downloading the release.");
            plexiLogger.trace(Arrays.toString(e.getStackTrace()));
        } catch (Exception e) {
            plexiLogger.error("Unknown issue when attempting to load version file. Try re-downloading the release.\n" + e.getMessage() + "\n" + e);
        }


        if(splashList != null && splashList.size() >0 && splashFilePath != null){
            plexiLogger.info("Successfully loaded " + splashList.size() + " splashes");
        }else{
            //we dont want to leave the splash list as null or it will cause issue, lets set it to a list that has one empty string.
            splashList = new ArrayList<>();
            splashList.add("");
        }

    }


    /**
     * Generates a config file in the current jar path. If there is an issue, the bot prints the error and terminates.
     */
    //if the initVariables method is unable to locate the config file, we need to create a new one.
    private void generateConfigFile() {
        try {
            //open an inputStream for the internal config file
            InputStream resourceConfigStream = internalConfigPath.openStream();
            //make sure that it was initialized properly
            if (resourceConfigStream == null) {
                plexiLogger.error("Unable to find config path!");
            }
            //now attempt to copy the file to the destination
            Files.copy(resourceConfigStream, userConfigPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            plexiLogger.error("Unable to create config file!");
            plexiLogger.trace(Arrays.toString(e.getStackTrace()));
            discordBot.stopBot();
            System.exit(-1);
        } catch (Exception e) {
            plexiLogger.error("Unknown error occurred while generating new config file!");
            plexiLogger.trace(Arrays.toString(e.getStackTrace()));
            discordBot.stopBot();
            System.exit(-1);
        }
    }


    /**
     * Checks all of the global variables for basic correctness. At the minimum, the values cannot be empty and cannot be
     * the same as the default config file's settings. The exception here is the prefix; the default is `.` and is able to stay that way.
     *
     * @return {@code false} if any of the loaded settings are invalid, {@code true} otherwise.
     */
    //we need a way to validate as many of the settings variables as possible
    //This is very rudimentary validation that checks to ensure that values are not empty and arent default. The Overseerr API is checked to see if it can connect.
    //TODO: Make this method a bit less clunky
    private boolean validateSettings() {
        boolean isValid = !token.equals("") && !token.equals("BOT_TOKEN_HERE");

        //check to ensure token != null
        //check to ensure prefix != null;
        if (prefix.equals("")) {
            isValid = false;
        }
        //check to ensure overseerURL != null;
        if (overseerrUrl.equals("") || overseerrUrl.equals("URL_HERE")) {
            isValid = false;
        }
        //check to ensure OverseerrKey != null;
        if (overseerrKey.equals("") || overseerrKey.equals("KEY_HERE")) {
            isValid = false;
        }
        //check to ensure ownerID != null;
        if (ownerId.equals("") || ownerId.equals("0")) {
            isValid = false;
        }
        //ensure that USERS_VIEW_REQUESTS is not null
        if(usersViewRequests == null){
            isValid = false;
        }
        if(sendErrorReports == null){
            isValid = false;
        }

        //ping the overseerr API to see if we have valid settings
        //if this throws an exception, we have invalid values
        //NOTE: There is no reason to check this if isValid is false
        isValid = checkOverseerrConnectivity();

        return isValid;
    }

    private boolean checkOverseerrConnectivity(){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(overseerrUrl + "/api/v1/status")
                .addHeader("accept", "application/json")
                .build();

        try(Response response = client.newCall(request).execute()){
            if(response.isSuccessful()){
                LoggerFactory.getLogger("Plexi: Settings").info("Successfully connected to Overseerr");
                return true;
            }
        }catch (Exception e){
            LoggerFactory.getLogger("Plexi: Settings").info("Failed to connect to Overseerr");
            JOptionPane.showMessageDialog(null, "Unable to connect to Overseerr - Please check your settings", "Plexi - Connectivity Issue", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        LoggerFactory.getLogger("Plexi: Settings").info("Failed to connect to Overseerr");
        JOptionPane.showMessageDialog(null, "Unable to connect to Overseerr - Please check your settings", "Plexi - Connectivity Issue", JOptionPane.INFORMATION_MESSAGE);
        return false;
    }


    //getters and setters for globals
    public String getDiscordToken() {
        return token;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getOwnerID() {
        return ownerId;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public String getBranchName(){
        return branchName;
    }

    public String getParentHash(){
        return parentHash;
    }

    public Boolean getUsersViewRequests(){
        return usersViewRequests;
    }

    public Logger getLogger(){
        return plexiLogger;
    }

    public String getHostedIconURL(){
        return HOSTED_ICON_URL;
    }

    public List<String> getSplashList(){
        return splashList;
    }

    public String getOverseerrUrl(){
        return overseerrUrl;
    }

    public String getOverseerrKey(){
        return overseerrKey;
    }

    public Boolean sendErrorReports(){
        return sendErrorReports;
    }
}
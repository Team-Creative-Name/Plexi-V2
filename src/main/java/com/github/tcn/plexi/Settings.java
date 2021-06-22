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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


/**
 * The Settings class for Plexi
 * <br>
 * This class follows a singleton pattern in an attempt to stop superfluous reloading of the settings text file located
 * in the same folder as the Plexi jar. There is currently NO way to change any of these settings values once they are set.
 */
public class Settings {

    //Variables

    //reference to this object - the only one
    private static Settings SETTINGS_INSTANCE = null;
    //the version number
    private final String VERSION_NUMBER = "v2.0-beta.2";
    //path to discord hosted icon @512x
    private final String HOSTED_ICON_URL = "https://cdn.discordapp.com/attachments/675899155083952148/736485323663474728/Plexi_icon_512x.png";
    //path to spashes file
    private URL SPLASH_FILE_PATH = null;
    private List<String> SPLASH_LIST = null;
    //stuff loaded from the config file
    private String TOKEN = null;
    private String PREFIX = null;
    private String OVERSEERR_URL = null;
    private String OVERSEERR_KEY = null;
    private String OWNER_ID = null;
    private Boolean USERS_VIEW_REQUESTS = null;
    private URL INTERNAL_CONFIG_PATH;
    private Path USER_CONFIG_PATH;
    //Variables useful for class operations
    private Path JAR_PATH;
    //reference to plexi object
    DiscordBot discordBot = DiscordBot.getInstance();

    //The Main logger for Plexi
    Logger plexiLogger;

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
        if (SETTINGS_INSTANCE == null) {
            SETTINGS_INSTANCE = new Settings();
        }
        //return the object reference
        return SETTINGS_INSTANCE;
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
            JAR_PATH = new File(Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).toPath();
            INTERNAL_CONFIG_PATH = this.getClass().getResource("/assets/config.txt");

            //now attempt to read from the configuration file and set values accordingly
            //if using Mac OS && are using the application bundle, we need to change the config and log paths
            if (System.getProperty("os.name").toLowerCase().contains("mac") && JAR_PATH.getParent().getParent().getParent().toString().contains(".app")) {
                //this should only fire if the jar is wrapped inside of a .app bundle
                //set the logger to the same folder that the .app is located in
                System.setProperty("LOG_PATH", JAR_PATH.getParent().getParent().getParent().getParent().toString());
                //now attempt to load the config file
                USER_CONFIG_PATH = JAR_PATH.getParent().getParent().getParent().getParent().resolve("config.txt");
            } else {
                //If not running within an application bundle, the user should have direct access to the jar. Set Everything to its path.
                USER_CONFIG_PATH = JAR_PATH.getParent().resolve("config.txt");
                //set the log path as well
                System.setProperty("LOG_PATH", JAR_PATH.getParent().toString());
            }

            //now that we know where the logger needs to be, go ahead and create it
            plexiLogger = LoggerFactory.getLogger("Plexi");

            FileInputStream config = new FileInputStream(USER_CONFIG_PATH.toString());
            Properties properties = new Properties();

            properties.load(config);

            //Load settings
            TOKEN = properties.getProperty("token").replaceAll("^\"|\"$", "");
            plexiLogger.info("Bot Token: " + TOKEN);
            OWNER_ID = properties.getProperty("ownerID").replaceAll("^\"|\"$", "");
            plexiLogger.info("Owner ID: " + OWNER_ID);
            PREFIX = properties.getProperty("prefix").replaceAll("^\"|\"$", "");
            OVERSEERR_URL = properties.getProperty("overseerrURL").replaceAll("^\"|\"$", "");
            OVERSEERR_KEY = properties.getProperty("overseerrKey").replaceAll("^\"|\"$", "");

            //this one is a bit special because we need to cast it to a boolean - this value defaults to false if an invalid value is provided
            USERS_VIEW_REQUESTS = Boolean.valueOf(properties.getProperty("usersViewRequests").replaceAll("^\"|\"$", "").toLowerCase());

            if (!validateSettings()) {
                JOptionPane.showMessageDialog(null, "The config file contains invalid settings, please check it and try again.", "Plexi - Configuration Issue", JOptionPane.INFORMATION_MESSAGE);
                plexiLogger.error("Invalid settings found in the configuration file. Plexi must exit");
                System.exit(0);
            }

        } catch (FileNotFoundException e) {
            plexiLogger.error("Unable to locate existing configuration file!");
            generateConfigFile();
            plexiLogger.info("A new configuration file has been generated at: " + USER_CONFIG_PATH.toString());
            JOptionPane.showMessageDialog(null, "The config file was unable to be found. A new one has been generated at: " + USER_CONFIG_PATH.toString(), "Plexi - Configuration Issue", JOptionPane.INFORMATION_MESSAGE);
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
            SPLASH_FILE_PATH = this.getClass().getResource("/assets/splashes.plexi");

            //open an inputsteam and loop through the steam until it ends
            InputStream splashStream = SPLASH_FILE_PATH.openStream();
            int current;
            StringBuilder currentQuote = new StringBuilder();
            SPLASH_LIST = new ArrayList<>();

            while((current = splashStream.read()) != -1){
                if(current == 10){
                    SPLASH_LIST.add(currentQuote.toString());
                    currentQuote = new StringBuilder();
                }else{
                    currentQuote.append((char) current);
                }
            }
            //We will still have one quote left in the currentQuote var, so add that to the list
            SPLASH_LIST.add(currentQuote.toString());

        } catch (FileNotFoundException e) {
            plexiLogger.error("Unable to locate splashes file, they will be disabled until found. Try re-downloading the release.");
            plexiLogger.trace(Arrays.toString(e.getStackTrace()));
        } catch (Exception e) {
            plexiLogger.error("Unknown issue when attempting to load splashes file, they will be disabled. Try re-downloading the release.\n" + e.getMessage() + "\n" + e);

        }

        if(SPLASH_LIST != null && SPLASH_LIST.size() >0 && SPLASH_FILE_PATH != null){
            plexiLogger.info("Successfully loaded " + SPLASH_LIST.size() + " splashes");
        }else{
            //we dont want to leave the splash list as null or it will cause issue, lets set it to a list that has one empty string.
            SPLASH_LIST = new ArrayList<>();
            SPLASH_LIST.add("");
        }

    }


    /**
     * Generates a config file in the current jar path. If there is an issue, the bot prints the error and terminates.
     */
    //if the initVariables method is unable to locate the config file, we need to create a new one.
    private void generateConfigFile() {
        try {
            //open an inputStream for the internal config file
            InputStream resourceConfigStream = INTERNAL_CONFIG_PATH.openStream();
            //make sure that it was initialized properly
            if (resourceConfigStream == null) {
                plexiLogger.error("Unable to find config path!");
            }
            //now attempt to copy the file to the destination
            Files.copy(resourceConfigStream, USER_CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING);
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
        boolean isValid = !TOKEN.equals("") && !TOKEN.equals("BOT_TOKEN_HERE");

        //check to ensure token != null
        //check to ensure prefix != null;
        if (PREFIX.equals("")) {
            isValid = false;
        }
        //check to ensure overseerURL != null;
        if (OVERSEERR_URL.equals("") || OVERSEERR_URL.equals("URL_HERE")) {
            isValid = false;
        }
        //check to ensure OverseerrKey != null;
        if (OVERSEERR_KEY.equals("") || OVERSEERR_KEY.equals("KEY_HERE")) {
            isValid = false;
        }
        //check to ensure ownerID != null;
        if (OWNER_ID.equals("") || OWNER_ID.equals("0")) {
            isValid = false;
        }
        //ensure that USERS_VIEW_REQUESTS is not null
        if(USERS_VIEW_REQUESTS == null){
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
                .url(OVERSEERR_URL + "/api/v1/status")
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
        return TOKEN;
    }

    public String getPrefix() {
        return PREFIX;
    }

    public String getOwnerID() {
        return OWNER_ID;
    }

    public String getVersionNumber() {
        return VERSION_NUMBER;
    }

    public Boolean getUsersViewRequests(){
        return USERS_VIEW_REQUESTS;
    }

    public Logger getLogger(){
        return plexiLogger;
    }

    public String getHostedIconURL(){
        return HOSTED_ICON_URL;
    }

    public List<String> getSplashList(){
        return SPLASH_LIST;
    }

    public String getOverseerrUrl(){
        return OVERSEERR_URL;
    }

    public String getOverseerrKey(){
        return OVERSEERR_KEY;
    }
}

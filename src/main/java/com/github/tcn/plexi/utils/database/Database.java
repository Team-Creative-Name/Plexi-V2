package com.github.tcn.plexi.utils.database;

import com.github.tcn.plexi.Settings;
import com.github.tcn.plexi.utils.database.template.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Database {

    Connection databaseConnecion;
    List<User> userList;

    public Database(){
        try{
            databaseConnecion = DriverManager.getConnection("jdbc:sqlite:plexiUsers.db");
        }catch (SQLException e){
            Settings.getInstance().getLogger().error("Unable to connect to users database");
            databaseConnecion = null;
        }
        //connect to the database


    }


    public void writeToDatabase(){

    }

    public void readUsers(){

    }



    public List<User> getUserList(){
        return null;
    }

    public boolean regenerateUserList(){
        return false;
    }




}

package controllers;

import Encrypters.*;
import database.DBConnector;
import model.User;

import java.sql.SQLException;

/**
 * Opretter en instans af DBConnector og kalder alle metoder til AccesTokens.
 * Hver metode er forklaret med kommentarer i DBConnector.
 */

public class TokenController {

    DBConnector db = new DBConnector();

    public User authenticate(String username, String password) throws SQLException {
        // Authenticate the user using the credentials provided
        String hashedPassword = Digester.hashWithSalt(password);

        User foundUser = db.authenticate(username, hashedPassword);

        if (foundUser != null) {

            String token = Crypter.buildToken("abcdefghijklmnopqrstuvxyz1234567890@&%!?", 25);

            db.backup("1: " + token);
            db.addToken(token, foundUser.getUserID());
            db.backup("2: " + token);
            foundUser.setToken(token);
        }


        //Retunerer en access token til klienten.
        return foundUser;


    }

    public User getUserFromTokens(String token) throws SQLException {
        DBConnector db = new DBConnector();
        User user = db.getUserFromToken(token);
        db.close();
        return user;

    }

    public boolean deleteToken(String token) throws SQLException{
        DBConnector db = new DBConnector();
        boolean deleteToken = db.deleteToken(token);
        db.close();
        return deleteToken;

    }
    public boolean validateToken(String authToken) {
        DBConnector db = new DBConnector();
        return db.validateToken(authToken);
    }
}

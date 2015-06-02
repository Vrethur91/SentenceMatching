package org.nlp.match.Tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bene
 */
public class ConfigLoader {

    public static String getProperty(String property) {
        String value = "";
        BufferedInputStream stream = null;
        try {
            Properties properties = new Properties();
            stream = new BufferedInputStream(new FileInputStream("config.properties"));
            properties.load(stream);
            stream.close();
            value = properties.getProperty("standford.ner.model.path");
        } catch (Exception ex) {
            Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return value;
    }
}

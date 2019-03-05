package code.assignment.util;

import code.assignment.exception.PropertiesFileNotFound;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.Properties;

/**
 * Properties reader general class.
 */

public class PropertiesReader {

    private Properties prop = new Properties();
    private InputStream input;
    private String property;

    /**
     * This method reads given properties file and gets the specific property value.
     * (in this application it is used to read stock file name - the endpoint)
     *
     * @param propertyFile properties file name
     * @param propertyName property with stock file name value
     * @return stock file name
     */
    public String getStockFileName(String propertyFile, String propertyName) {

        try {
            input = PropertiesReader.class.getClassLoader().getResourceAsStream(propertyFile);

            if (input != null) {
                //load a properties file from class path, inside static method
                prop.load(input);

                //get the property value
                property = prop.getProperty(propertyName);

                if (property == null) {
                    throw new InvalidParameterException("[ Unable to get property name: " + propertyName + " ]");
                }

            } else {
                throw new PropertiesFileNotFound("[ Unable to find stock properties file: " + propertyFile + " ]");
            }

        } catch (IOException e) {
            System.out.println("[ Error reading properties: " + e.getMessage() + "]");

        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("[ Error occurred when closing properties: " + e.getMessage() + "]");
                }
            }
        }

        return property;
    }
}
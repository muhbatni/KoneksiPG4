package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseConnection {
  private static Connection connection;
  private static String[] data;
  private final static String CONFIG_FILE = "setting.txt";
  private final static InputStream inputStream = DatabaseConnection.class.getClassLoader()
      .getResourceAsStream(CONFIG_FILE);

  private DatabaseConnection() {}

  private static void loadData() {
    data = new String[6];
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      if (inputStream == null) {
        throw new IOException("Configuration file not found: " + CONFIG_FILE);
      }
      String line;
      while ((line = reader.readLine()) != null) {
        processLine(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error reading the configuration file.");
    }
  }

  private static final Map<String, Integer> dataMapping = Map.of( "user", 2, "password", 3,
      "database", 4, "host", 5);

  private static void processLine(String line) {
   
      final String[] split = line.split("=");
      if (dataMapping.containsKey(split[0])) {
      
        data[dataMapping.get(split[0])] = split[1].trim();
      }
    
  }

  public static Connection getConnection() {
    if (connection == null) {
      // generate();
      loadData();
      try {
        final String user = data[2];
        final String password = data[3];
        final String database = data[4];
        final String host = data[5];
        final String URL = "jdbc:postgresql://" + host + "/" + database;
        connection = DriverManager.getConnection(URL, user, password);
        System.out.println("Estabilished connection to database");
        return connection;
      } catch (SQLException exception) {
        exception.printStackTrace();
        System.out.println("Failed to estabilish connection to database");
      }
    }
    return connection;
  }

  // private static void generate() {
  // try {
  // SecretKey key = AESUtil.generateKey(128);
  // IvParameterSpec iv = AESUtil.generateIv();
  // String encodedKey = AESUtil.keyToString(key);
  // String encodedIv = AESUtil.ivToString(iv);
  // System.out.println("key: " + encodedKey);
  // System.out.println("iv: " + encodedIv);
  // System.out.println("Encrypted password: " + AESUtil.encrypt("1sampai8", key,
  // iv));
  // } catch (NoSuchAlgorithmException e) {
  // e.printStackTrace();
  // } catch (InvalidKeyException | NoSuchPaddingException |
  // InvalidAlgorithmParameterException | BadPaddingException
  // | IllegalBlockSizeException e) {
  // e.printStackTrace();
  // }
  // }
}
package data;

import java.util.*;
import java.sql.*;
import data.*;
import models.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

// Make sure mysql-connector-java-5.1.27-bin.jar is in the classpath
// For glassfish, needs to be in the glassfish/lib folder
// Need to add user and grant access to beerprices

public class BeerData
{
   DatabaseAccess db;
   
   public BeerData() {
      String dbName = "beerprices";
      String user = "test";
      String pswd = "testing";
      String host = "localhost";
      String port = "3306";
      
      // Create an object of the utility class that you will use to do your queries
      try
      {
         db = new DatabaseAccess(dbName, user, pswd, host, port);
      }
      catch(SQLException e)
      {
         e.printStackTrace();
      }
      catch(ClassNotFoundException e)
      {
         e.printStackTrace();
      }
   } 
   
   public Beer getBeer(String beerName) {
      try{   
         String sql = "SELECT beername, beerprice FROM beers WHERE beername = ?";
            
         ArrayList<String> params = new ArrayList<String>();
         
         params.add(beerName);
         
         ArrayList<ArrayList<String>> res = db.getDataPS(sql, params);
         
         if(res != null) {
            for(ArrayList<String> row : res) {            
              return new Beer(row.get(0), Double.parseDouble(row.get(1)));
            }
         }        
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
      }
      
      return null;
   }
   
   public double getPrice(String beerName) {
   
      try{   
         String sql = "SELECT beerprice FROM beers WHERE beername = ?";
            
         ArrayList<String> params = new ArrayList<String>();
         
         params.add(beerName);
         
         ArrayList<ArrayList<String>> res = db.getDataPS(sql, params);
         
         if(res != null) {
            for(ArrayList<String> row : res) {            
              return Double.parseDouble(row.get(0));
            }
         }         
         else {
            return -1;
         }
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
      }
      return -1;
   }
   
   public boolean setPrice(Beer beer, double price) {
      try {
         String sql = "UPDATE beers SET beerprice=? WHERE beername=?";
         
         ArrayList<String> params = new ArrayList<String>();
         params.add(Double.toString(beer.getPrice()));         
         params.add(beer.getName());
         
         int updated = db.nonSelect(sql,params);
         
         if(updated > 0) {
            return true;
         }         
         else {
            return false;
         }
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
      }
      
      return false;
   }
   
   public Vector<Beer> getAllBeers() {
      Vector<Beer> beers = new Vector<Beer>();
      try {
         String sql = "SELECT beername, beerprice FROM beers";
         ArrayList<ArrayList<String>> res = db.getData(sql);      
         
         if(res != null) {
            for(ArrayList<String> row : res) {            
              beers.add(new Beer( row.get(0), Double.parseDouble(row.get(1)) ));
            }
         }    
         
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
      }
      
      return beers;
   }
   
   public Beer getCheapestBeer() {
      String sql = "SELECT beername, beerprice FROM beers ORDER BY beerprice ASC LIMIT 1";
      return getBeerWithQuery(sql);
   }
   
   public Beer getCostliestBeer() {
      String sql = "SELECT beername, beerprice FROM beers ORDER BY beerprice DESC LIMIT 1";
      return getBeerWithQuery(sql);
   }
   
   public Beer getBeerWithQuery(String query) {
      try {         
         ArrayList<ArrayList<String>> res = db.getData(query);
         
         if(res != null) {
            for(ArrayList<String> row : res) {            
              return new Beer(row.get(0), Double.parseDouble(row.get(1)));
            }
         }         
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
      }
      
      return null;
   }
   
   public Token getToken(String token) {
      try{   
         String sql = "SELECT token, user, expires FROM token WHERE token = ?";
            
         ArrayList<String> params = new ArrayList<String>();
         
         params.add(token);
         
         ArrayList<ArrayList<String>> res = db.getDataPS(sql, params);
         
         if(res != null) {
            for(ArrayList<String> row : res) {  
              User user = getUserById(row.get(1)); 
              try {
                 SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
                 java.util.Date date = dt.parse(row.get(2));             
                 
                 return new Token(row.get(0),user,new java.sql.Date(date.getTime()));
              }
              catch(ParseException pe) {
                  System.out.println("Error parsing date.");
              }
            }
         }        
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
      }
      
      return null;
   }
   
   public void removeToken(Token token) {
      try {
         String sql = "DELETE FROM token WHERE token=?";
         
         ArrayList<String> params = new ArrayList<String>();
         params.add(token.getValue());     
         
         int updated = db.nonSelect(sql,params);
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
      }
   }
   
   private void deleteUserTokens(String userId) {
      try {
         String sql = "DELETE FROM token WHERE user=?";
         
         ArrayList<String> params = new ArrayList<String>();
         params.add(userId);     
         
         int updated = db.nonSelect(sql,params);
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
      }
   }
   
   public boolean saveToken(Token token) {
      try {
         int userId = token.getUser().getId();
         deleteUserTokens( Integer.toString(userId) );
         
         String sql = "INSERT INTO token (token,user,expires) VALUES(?,?,?)";
         
         ArrayList<String> params = new ArrayList<String>();
         params.add(token.getValue());   
         params.add( Integer.toString(token.getUser().getId()) );  
         
         SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String dateToSave = dt.format(token.getExpires());
         
         params.add(dateToSave);
         
         int updated = db.nonSelect(sql,params);
         
         if(updated > 0) {
            return true;
         }         
         else { 
            return false;
         }
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
      }
      
      return false;
   }
   
   public User getUserWithQuery(String sql, ArrayList<String> params) {
      try{        
         
         ArrayList<ArrayList<String>> res = db.getDataPS(sql, params);
         
         if(res != null) {
            for(ArrayList<String> row : res) {
               return new User(Integer.parseInt(row.get(0)), row.get(1), row.get(2),
                            Integer.parseInt(row.get(3)), row.get(4));
            }
         }        
      }
      catch(SQLException sqle) {
         sqle.printStackTrace();
      }
      
      return null;
   }
   
   public User getUserById(String userId) {
   
      String sql = "SELECT id, username, password, age, accesslevel " +
                           "FROM users WHERE id = ?";
            
      ArrayList<String> params = new ArrayList<String>();
      
      params.add(userId);
      
      return getUserWithQuery(sql, params);
   }
   
   public User getUserByName(String userName) {
   
      String sql = "SELECT id, username, password, age, accesslevel " +
                           "FROM users WHERE username = ?";
            
      ArrayList<String> params = new ArrayList<String>();
      
      params.add(userName);
      
      return getUserWithQuery(sql, params);
   }
   
} //class
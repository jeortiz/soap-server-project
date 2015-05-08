package business;

import java.util.*;
import data.*;
import service.*;
import models.*;

public class BeerBusiness {

   private BeerData data;
   
   public BeerBusiness() {
      this.data = new BeerData();
   }
   
   public String createToken(String username, String password) {
      if(isValidTime()) {
      
         User user = this.data.getUserByName(username);
         
         if(user != null && user.validatePassword(password)) {
            long expires = new Date().getTime() + 300000; //current time plus 5 mins in ms
            Token token = new Token(user, new java.sql.Date(expires));
            
            if(data.saveToken(token)) {
               return token.getValue();
            }
            else {
               return null;
            }
         }
      }      
      return null;
   }
   
   public Double getPrice(String beerName, String token) {   
      if(isValidTime() && validateAndRemove(token)) {
         return this.data.getPrice(beerName);
      }
      
      return null;
   }
   
   public boolean setPrice(String beerName, double price, String token) {   
      if(isValidTime() && userIsAdmin(token) && validateAndRemove(token)) {
         Beer beer = data.getBeer(beerName);
         
         if(beer != null) {
            beer.setPrice(price);
            data.setPrice(beer, price);
            
            return true;
         }
         else {
            return false;
         }
      }
      
      return false;
   }
   
   public Vector<String> getAllBeers(String token) {
      if(isValidTime() && validateAndRemove(token)) {
         Vector<Beer> beers = this.data.getAllBeers();
         Vector<String> beerNames = new Vector<String>();
         
         for(Beer beer : beers) {
            beerNames.add(beer.getName());
         }
         
         return beerNames;
      }
            
      return null;
   }
   
   public String getCheapest(String token) {
      if(isValidTime() && validateAndRemove(token)) { 
         return this.data.getCheapestBeer().getName();
      }
      
      return null;
   }
   
   public String getCostliest(String token) {
      if(isValidTime() && validateAndRemove(token)) { 
         return this.data.getCostliestBeer().getName();
      }
      
      return null;
   }
   
   private boolean validateAndRemove(String token) {
      Token tokenObject = this.data.getToken(token);
      
      if(tokenObject != null && tokenObject.isValidToken()) {
      
         if(tokenObject.getUser().getAge() < 21) {
            return false;
         }
         
         this.data.removeToken(tokenObject);
         tokenObject = null;
         
         return true;
      }
      else if(tokenObject != null && !tokenObject.isValidToken()) {
         this.data.removeToken(tokenObject);
         return false;
      }
      
      return false;
   }
   
   private boolean userIsAdmin(String token) {
      Token tokenObject = this.data.getToken(token);
      if(tokenObject != null && 
            tokenObject.getUser().getAccessLevel() != null &&
            tokenObject.getUser().getAccessLevel().equals("admin")) {
         return true;
      }
      else
         return false;
   }
   
  private boolean isValidTime() {
  
      Date date = new Date();   
      Calendar calendar = GregorianCalendar.getInstance(); 
      calendar.setTime(date);    
      int hour = calendar.get(Calendar.HOUR_OF_DAY); 
      int minutes = calendar.get(Calendar.MINUTE);
      
      int currentHourWithMinutes = (hour * 100) + minutes;
      System.out.println(currentHourWithMinutes);
      if(currentHourWithMinutes > 1000) {
         return true;
      }
      
      return false;
      
   }
   
}
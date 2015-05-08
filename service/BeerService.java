package service;

import javax.jws.*;
import java.util.*;
import business.*;
import models.*;


@WebService
public class BeerService
{
   private BeerBusiness business;
   
   public BeerService() {
      business = new BeerBusiness();
   }
   
   public static void main(String[] args) {
      BeerService service = new BeerService();
       //System.out.println(service.createToken("joe","smith"));
       System.out.println(service.getBeers("tlp3f9gp9s9u2qg952qqubdg93"));
       //System.out.println(service.getMethods());
   }
      
   @WebMethod(operationName="getToken")
   public String createToken(String username, String password) {
      String token = this.business.createToken(username, password);
      
      if(token != null){
         return token;
      }
      
      return "Token could not be created.";
   }
   
   @WebMethod
   public Vector<String> getMethods() {
      Vector<String> methods = new Vector<String>();
      methods.add("double getPrice(string)");
      methods.add("boolean setPrice(string, double)");
      methods.add("array getBeers()");
      methods.add("string getCheapest()");
      methods.add("string getCostliest()");
            
      return methods;
   }
   
   @WebMethod
   public Vector<String> getBeers(String token) {
      Vector<String> beers = this.business.getAllBeers(token);
      
      if(beers != null)
         return beers;
      else
         return new Vector<String>();
   }
   
   @WebMethod
   public double getPrice(String beer, String token) {
      Double price = this.business.getPrice(beer, token);
      
      if(price != null) {
         return price.doubleValue();
      } 
      else {
         return -1;
      }
   }
   
   @WebMethod
   public boolean setPrice(String beer, Double price, String token) {
      return this.business.setPrice(beer, price.doubleValue(), token);
   }
   
   @WebMethod
   public String getCheapest(String token) {
      String beer = this.business.getCheapest(token);
      
      if(beer != null) {
         return beer;
      }
      else {
         return "Error looking for beer.";
      }
   }
   
   @WebMethod
   public String getCostliest(String token)
   {
      String beer = this.business.getCostliest(token);
      
      if(beer != null) {
         return beer;
      }
      else {
         return "Error looking for beer.";
      }
   }
}
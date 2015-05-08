package models;

import java.util.*;
import java.math.*;
import java.security.SecureRandom;

public class Token {

   private String tokenValue;
   private User user;
   private Date expires;
   
   public Token(User _user, Date _expire) {
      this.tokenValue = generateToken();
      setUser(_user);
      setExpires(_expire);
   }
   
   public Token(String _tokenValue, User _user, java.sql.Date _expire) {
      setValue(_tokenValue);
      setUser(_user);
      setExpires(_expire);
   }
   
   public String generateToken() {
      SecureRandom random = new SecureRandom();
      return new BigInteger(130, random).toString(32);
   }
   
   private void setValue(String _tokenValue) {
      this.tokenValue = _tokenValue;
   }
   
   public String getValue() {
      return this.tokenValue;
   }
   
   public void setUser(User _user) {
      this.user = _user;
   }
   
   public User getUser() {
      return this.user;
   }
   
   public void setExpires(Date _expire) {
      this.expires = _expire;
   }
   
   public Date getExpires() {
      return this.expires;
   }
   
   public boolean isValidToken() {
      if( getExpires().after(new Date()) )
         return true;
      else 
         return false;
   }
}
package models;

public class User {
   
   private int id;
   private String name;
   private int age;
   private String password;
   private String accessLevel;
   
   public User(int _id, String _name, String _password, int _age, String _accessLevel) {
      setId(_id);
      setName(_name);
      setAge(_age);
      setPassword(_password);
      setAccessLevel(_accessLevel);
   }
   
   public void setId(int _id) {
      this.id = _id;
   }
   
   public int getId() {
      return this.id;
   }
   
   public void setName(String _name) {
      this.name = _name;
   }
   
   public String getName() {
      return this.name;
   }
   
   public void setAge(int _age) {
      this.age = _age;
   }
   
   public int getAge() {
      return this.age;
   }
   
   public void setPassword(String _password) {
      this.password = _password;
   }
   
   public boolean validatePassword(String password) {
      return this.password.equals(password);
   }
   
   public void setAccessLevel(String _accessLevel) {
      this.accessLevel = _accessLevel;
   }
   
   public String getAccessLevel() {
      return this.accessLevel;
   }
}
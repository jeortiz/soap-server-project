package models;

public class Beer {

    private String name;
    private double price;

    public Beer(String _name, double _price) {
        setName(_name);
        setPrice(_price);
    }

    public String  getName() {
        return this.name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double _price) {
        if( _price > 0 )
            this.price = _price;
    }
}
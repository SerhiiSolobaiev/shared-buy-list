package com.example.serg.myapplinux;


public class ListModel {
    private  String Name = "";
    private  int Number = 0;
    private  int Price = 0;

    /*********** Set Methods ******************/

    public void setName(String Name)
    {
        this.Name = Name;
    }

    public void setNumber(int Number)
    {
        this.Number = Number;
    }

    public void setPrice(int Price)
    {
        this.Price= Price;
    }

    public String getName()
    {
        return this.Name;
    }

    public int getNumber()
    {
        return this.Number;
    }

    public int getPrice()
    {
        return this.Price;
    }
}

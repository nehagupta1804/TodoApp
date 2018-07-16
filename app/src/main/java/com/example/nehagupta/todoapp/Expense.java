package com.example.nehagupta.todoapp;

public class Expense {

    public String name;
    public  int amount;
    public long id;
    public String camera;
    public String ram;
    public long date;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Expense(String name, int amount,String camera,String ram) {

        this.name = name;
        this.amount = amount;
        this.camera=camera;
        this.ram=ram;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {

        return name;
    }

    public int getAmount() {
        return amount;
    }


}

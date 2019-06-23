package com.example.myswipe.lib;

public enum CustomizedHeader {

    latitude("latitude"),
    longitude("longitude"),
    address("address");

    private String name;

    CustomizedHeader(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}

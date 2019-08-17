package com.example.myswipe.lib;

public enum CustomHeader {

    latitude("latitude"),
    longitude("longitude"),
    address("address");

    private String name;

    CustomHeader(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}

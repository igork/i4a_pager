package com.andilib;

class ListItem {
    public String key;
    public String[] value;
    //public String id;

    public ListItem(String key,String[] value){
        this.key = key;
        this.value = value;
        //this.id = id;
    }

    public String getValues(){
        String res = "";
        if (value!=null){

            for(String s: value){
                res = res + s + " ";
            }
        }
        return res;
    }
}

package com.example.myswipe.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class CustomizedProperties extends Properties {

    Enum baseEnum;

    //public CustomizedProperties(enum baseEnum){
    //    this.baseEnum = baseEnum;
    //}

    public CustomizedProperties(){

    }

    public CustomizedProperties(Enum baseEnum){
        this.baseEnum = baseEnum;
    }

    /*
    public void add(CustomizedHeader header,String value){
        if (value!=null && !value.isEmpty())
            put(header.getName(),value);
    }
    */




    public void add(Enum header,String value){

        /*

         */
        if (value!=null && !value.isEmpty())
            put(header.name(),value);
    }

    public String toString(){
        String result = "";

        /*
        for( CustomizedHeader header: CustomizedHeader.values()){

            result+= header.getName() + ": " + getProperty(header.getName());

        }
        */
        Set<String> set = stringPropertyNames();
        List<String> sorted = new ArrayList<>();
        sorted.addAll(set);
        Collections.sort(sorted);

        for( String name:sorted){
            result+= name + ": " + getProperty(name) + "\n";
        }

        //java8    api24
        //List<String> personsSorted = set.stream().collect(Collectors.toCollection());

        //for( String name:stringPropertyNames()){
         //   result+= name + ": " + getProperty(name) + "\n";
        //}
        return result;
    }
    /*
    Run -> Edit Configurations..., click the plus (+) button and select Application.
    Set the Name to Desktop.
    Set the field Use classpath of module to desktop, then click on the button of the Main class field and select the DesktopLauncher class.
    Set the Working directory to your android/assets/ (or your_project_path/core/assets/) folder!
    Click Apply and then OK.
     */
    public static void main(String[] arg){
        System.out.println("ddddddddddd");

        CustomizedProperties prop = new CustomizedProperties();
        prop.add(CustomizedHeader.address,"address");
        prop.add(CustomizedHeader.latitude,"latti");

        System.out.println("ddddddddddd" + prop.toString());

    }
}

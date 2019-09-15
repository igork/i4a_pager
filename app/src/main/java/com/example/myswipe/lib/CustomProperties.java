package com.example.myswipe.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import android.support.annotation.NonNull;

public class CustomProperties extends Properties {

    Enum baseEnum;

    final static String separator = ": ";
    final static String linefeed = "\n";

    //public CustomProperties(enum baseEnum){
    //    this.baseEnum = baseEnum;
    //}

    public CustomProperties(){

    }

    public CustomProperties(Enum baseEnum){

        this.baseEnum = baseEnum;
    }

    /*
    public void add(CustomHeader header,String value){
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

    public void add(Enum header, CustomProperties value){

        /*

         */
        if (value!=null && !value.isEmpty())
            put(header.name(),value);

    }

    @Override
    public String getProperty(String key) {

        //return super.getProperty(key);
        Object obj = super.get(key);

        if (obj instanceof String){
            return (String)obj;
        }
        if (obj instanceof CustomProperties){
            return ((CustomProperties)obj).toString();
        }
        return null;
    }

    @Override
    public Object get(Object key) {

        //return super.get(key);

        if (key instanceof String) {

            Object obj = super.get(key);

            /*
            if (obj instanceof String) {
                return (String) obj;
            }
            if (obj instanceof CustomProperties) {
                return ((CustomProperties) obj);
            }
            */
            return obj;
        }
        return null;
    }


    @Override
    public int size(){
        return super.size();
    }

    @Override
    @NonNull
    public Set<Object> keySet(){
        return super.keySet();

    }


    @Override
    public String toString(){
        return toString("");
    }

    //reverse toString
    public static String extractValue(String string, String substring){

        String lookingFor  = substring + separator;
        int index = string.indexOf(lookingFor);
        if (index>=0){

            String restString  = string.substring(index + lookingFor.length());
            int second = restString.indexOf(linefeed);

            return restString.substring(0,second);


        }
        return null;
    }

    //

    public String toString(String prefix){
        String result = "";

        //for(String name:sortedNames()){
        //    result += prefix + name + separator + getProperty(name) + "\n";
       // }

        for(String name:sortedNames()){

            Object obj = get(name);

            if (obj instanceof String){
                result += prefix + name + separator + obj + linefeed;
            }
            if (obj instanceof CustomProperties){
                result += ((CustomProperties)obj).toString(name + separator);
            }


        }

        //java8    api24
        //List<String> personsSorted = set.stream().collect(Collectors.toCollection());

        //for( String name:stringPropertyNames()){
        //   result+= name + ": " + getProperty(name) + "\n";
        //}
        return result;
    }

    public List<String> sortedNames(){
        Set<String> set = stringPropertyNames();
        List<String> sorted = new ArrayList<>();
        sorted.addAll(set);
        Collections.sort(sorted);
        return sorted;
    }

    /*
    public String toString(){
        String result = "";

        for(String name:sortedNames()){
            result += name + separator + getProperty(name) + "\n";
        }

        //java8    api24
        //List<String> personsSorted = set.stream().collect(Collectors.toCollection());

        //for( String name:stringPropertyNames()){
         //   result+= name + ": " + getProperty(name) + "\n";
        //}
        return result;
    }
    */

    public String[] getStringValues(){
        String[] result = new String[this.size()];


        /*
        for( CustomHeader header: CustomHeader.values()){

            result+= header.getName() + ": " + getProperty(header.getName());

        }
        */
        Set<String> set = stringPropertyNames();
        List<String> sorted = new ArrayList<>();
        sorted.addAll(set);
        Collections.sort(sorted);

        for(int i=0; i<sorted.size(); i++){
            result[i] = getProperty(sorted.get(i));
        }

        //java8    api24
        //List<String> personsSorted = set.stream().collect(Collectors.toCollection());

        //for( String name:stringPropertyNames()){
        //   result+= name + ": " + getProperty(name) + "\n";
        //}
        return result;
    }

    public Object[] getObjectValues(){
        Object[] result = new Object[this.size()];


        /*
        for( CustomHeader header: CustomHeader.values()){

            result+= header.getName() + ": " + getProperty(header.getName());

        }
        */
        List<String> sorted = sortedNames();
        for(int i=0; i<sorted.size(); i++){
            result[i] = get(sorted.get(i));
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

        CustomProperties prop = new CustomProperties();
        prop.add(CustomHeader.address,"address");
        prop.add(CustomHeader.latitude,"latti");

        System.out.println("ddddddddddd" + prop.toString());

    }
}

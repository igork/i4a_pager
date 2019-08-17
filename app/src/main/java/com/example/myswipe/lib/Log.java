package com.example.myswipe.lib;

import android.app.Activity;

public class Log {

    /* Java 9
    static StackWalker sw;
    public void foo() {
        Class<?> caller = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE)
                .getCallerClass();
    }
    */

    static String defaultTAG = "i4a Logger";

    //singleton for keeping log in file
    static private Activity activity;
    public void init(Activity activity){
        this.activity = activity;
    }
    static private void writeLog(String tag, String string){
        writeLog(tag + ": " + string);
    }
    static private void writeLog(String string){

        if (activity==null || string==null || string.isEmpty()) {
            return;
        }
         StorageUtil.writeFile(activity,string);
    }


    public static String getCaller(){

        String callerName = Thread.currentThread().getStackTrace()[2].getClassName();

        try {
            Class<?> caller = Class.forName(callerName);

            // Do something with it ...
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return callerName;
    }

    public static void log(String text) {

        String caller = getCaller();
        if (caller==null || caller.isEmpty()){
            caller = defaultTAG;
        }
        logging(caller,text);
    }

    public static void log(String tag, String text) {

        logging(tag,text);
    }

    public static void d(String tag, String text) {

        logging(tag,text);
    }

    public static void logging(String tag, String text) {

        if (text==null || text.isEmpty()){
            return;
        }
        //Logcat
        android.util.Log.i(tag,text);
        //Console
        System.out.println(tag + ": " + text);
        //file?
    }

    public static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Log.class.getName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
                return ste.getClassName();
            }
        }
        return null;
    }

    public static String getCallerCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Log.class.getName())&& ste.getClassName().indexOf("java.lang.Thread")!=0) {
                if (callerClassName==null) {
                    callerClassName = ste.getClassName();
                } else if (!callerClassName.equals(ste.getClassName())) {
                    return ste.getClassName();
                }
            }
        }
        return null;
    }
}

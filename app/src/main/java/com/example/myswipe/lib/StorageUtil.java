package com.example.myswipe.lib;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class StorageUtil {

    //final static String TESTSTRING = new String("Hello Android");

     //Writing a file...
    static public void writeFile(Activity activity, String txt) {


        try {
            // catches IOException below


            /* We have to use the openFileOutput()-method
             * the ActivityContext provides, to
             * protect your file from others and
             * This is done for security-reasons.
             * We chose MODE_WORLD_READABLE, because
             *  we have nothing to hide in our file */
            FileOutputStream fOut = activity.openFileOutput("samplefile.txt",
                    MODE_PRIVATE);

            // file goes to "/data/data/your_project_package_structure/files/samplefile.txt"

            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            // Write the string to the file
            osw.write(txt);

            /* ensure that everything is
             * really written out and close */
            osw.flush();
            osw.close();

            //Log.d("File Writing", "success = ok");
        } catch (Exception e){

        }
    }

    //Reading the file back...
    static public String readFile(Activity activity) {

        try{

            /* We have to use the openFileInput()-method
             * the ActivityContext provides.
             * Again for security reasons with
             * openFileInput(...) */

            FileInputStream fIn = activity.openFileInput("samplefile.txt");
            InputStreamReader isr = new InputStreamReader(fIn);

            //DIRECTLY FROM
            /* Prepare a char-Array that will
             * hold the chars we read back in. */
            /*
            char[] inputBuffer = new char[TESTSTRING.length()];

            // Fill the Buffer with data from the file
            isr.read(inputBuffer);

            //test
            // Transform the chars to a String
            String readString = new String(inputBuffer);
            return readString;
            */

            //VIA WRAPPING TO BufferReader
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(fIn));
            String line,line1 = "";
            try
            {
                while ((line = buffreader.readLine()) != null)
                    line1+=line;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return line1;
            }


            // Check if we read back the same chars that we had written out
            //boolean isTheSame = TESTSTRING.equals(readString);

            //Log.d("File Reading stuff", "success = " + isTheSame);

        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args){

    }
}

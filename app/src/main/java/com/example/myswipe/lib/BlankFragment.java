package com.example.myswipe.lib;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myswipe.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
https://stackoverflow.com/questions/45355531/android-listview-inside-dialog
 */

public class BlankFragment extends DialogFragment {

    private ListView    listView;
    private TextView    textView;

    private String          text;
    private String          title;
    private List<String>    list;

    Activity activity;

    public BlankFragment() {
    }


    public void setText(String text){
        this.text = text;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setList(List<String> list){
        this.list = list;
    }

    public TextView getTextView(){
        return textView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = view.findViewById(R.id.f_blank_title);

        List<String> strings = new ArrayList<String>();

        //add as text
        if (text != null && !text.isEmpty()) {
            strings.addAll(Arrays.asList(text));
        }
        //add as list elements
        if (list != null && !list.isEmpty()) {
            strings.addAll(list);
        }

        if (!strings.isEmpty()){

            try {
                listView = view.findViewById(R.id.f_blank_list);
                ArrayAdapter<String> arrayAdapter
                        = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strings);
                listView.setAdapter(arrayAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //add title
        try {
            //textView = view.findViewById(R.id.f_blank_title);
            textView.setText(title);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private static final String BLANK_FRAGMENT_TAG = "FRAGMENT_TAG";

    private FragmentActivity context;
    //FragmentManager fm2 = ((AppCompatActivity) activity).getSupportFragmentManager();

    @Override
    public void onAttach(Activity activity) {
        context = (FragmentActivity) activity;
        super.onAttach(activity);

        FragmentManager fm2 = ((AppCompatActivity) context).getSupportFragmentManager();
    }
    //FragmentManager fm = context.getSupportFragmentManager();
    public void loadFragment(View view, String title, String text) {
        //BlankFragment blankFragment = new BlankFragment();


        FragmentManager fm2 = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentManager fm = context.getSupportFragmentManager();

        setText(text);
        setTitle(title);
        show(context.getSupportFragmentManager(), BLANK_FRAGMENT_TAG);
    }

    public void loadFragment(AppCompatActivity activity, String title, String text) {
        //BlankFragment blankFragment = new BlankFragment();


        FragmentManager fm = activity.getSupportFragmentManager();
        //FragmentManager fm = context.getSupportFragmentManager();

        setText(text);
        setTitle(title);
        show(fm, BLANK_FRAGMENT_TAG);
    }

    public void loadFragment(View view, String title, String[] list) {
        //BlankFragment blankFragment = new BlankFragment();

        List<String> slist = Arrays.asList(list);
        this.setList(slist);

        this.setTitle(title);
        this.show(context.getSupportFragmentManager(), BLANK_FRAGMENT_TAG);
    }
    public void loadFragment(View view, String title, List<String> list) {
        //BlankFragment blankFragment = new BlankFragment();
        this.setList(list);

        this.setTitle(title);
        this.show(context.getSupportFragmentManager(), BLANK_FRAGMENT_TAG);
    }

}

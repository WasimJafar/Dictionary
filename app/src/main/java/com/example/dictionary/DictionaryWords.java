package com.example.dictionary;

import android.util.Log;

import java.io.*;
import java.util.*;
public class DictionaryWords {

    private Scanner s;
    TrieNode root;
    TrieManager manager ;

    DictionaryWords(TrieManager manager){
        this.manager = manager;
        this.root = manager.root;
    }

    public void openFile(InputStream file) {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while(line != null){
                Log.d("StackOverflow", line);
                String word = line.split(":")[0];
                String meaning = line.split(":")[1];
                manager.addWordsToTrieNode(root , word , meaning);
                line = reader.readLine();
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception :" + e);
        }



    }


}

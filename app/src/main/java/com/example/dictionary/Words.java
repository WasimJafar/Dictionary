package com.example.dictionary;


import java.io.InputStream;

public class Words {
    String word , meaning;
    Words(){}
    Words(String word , String meaning){
        this.word = word;
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public void addToTrieWords(TrieManager manager , InputStream file){

        DictionaryWords dictionaryWords = new DictionaryWords(manager);
        dictionaryWords.openFile(file);

    }
}

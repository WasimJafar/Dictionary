package com.example.dictionary;

import java.util.ArrayList;
public class TrieManager {

    TrieNode root;

    TrieManager(TrieNode root){
        this.root = root;
    }

    ArrayList<Words> searchWord(String word){
        TrieNode rootNode = searchUsingPrefix(root , word);
        ArrayList<Words> wordsArrayList = new ArrayList<>();
        findInTrieUsingPrefNode(rootNode , word , wordsArrayList);

        return wordsArrayList;
    }

    void findInTrieUsingPrefNode(TrieNode node , String word , ArrayList<Words> words){
        if(node == null) return;
        if(node.isTerminating){
            words.add(new Words(word , null));
//            Log.i("Word found : " , word );
        }

        for(char key : node.children.keySet()){
            findInTrieUsingPrefNode(node.children.get(key) , word + key , words);
        }

    }

    TrieNode searchUsingPrefix(TrieNode node , String word){
        if(word.length() == 0) {
            return node;
        }
        char ch = word.charAt(0);

        if(!node.children.containsKey(ch)){
            return null;
        }
        return searchUsingPrefix(node.children.get(ch) , word.substring(1));
    }


    void addWordsToTrieNode(TrieNode node , String word){

        if(word.length() == 0){
            node.isTerminating = true;
            return;
        }
        char ch = word.charAt(0);
        if(!node.children.containsKey(ch)){
            node.children.put(ch , new TrieNode(ch));
        }
        addWordsToTrieNode(node.children.get(ch) , word.substring(1));
    }

}

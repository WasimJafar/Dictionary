package com.example.dictionary;

import java.util.HashMap;

public class TrieNode {

    char data;
    HashMap<Character , TrieNode> children;
    String meaning = null;
    boolean isTerminating = false;

    TrieNode(char data){
        this.data = data;
        children = new HashMap<>();
    }
}

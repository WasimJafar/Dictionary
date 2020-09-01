package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.leo.simplearcloader.SimpleArcLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editSearch;
    ListView listView;
    SimpleArcLoader simpleArcLoader;
    TrieNode root = new TrieNode('\0');
    TrieManager manager = new TrieManager(root);;
    Words words;
    ArrayList<Words> wordsArrayList;
    InputStream file ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editSearch = findViewById(R.id.edtSearch);
        listView = findViewById(R.id.listView);
        simpleArcLoader = findViewById(R.id.loader);

        try{
            file = getAssets().open("write.txt.txt");
        }catch (Exception e){

        }

        words = new Words();
        words.addToTrieWords(manager , file);
        wordsArrayList = manager.searchWord("");

        CustomAdapter customAdapter = new CustomAdapter(wordsArrayList);
        listView.setAdapter(customAdapter);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("Pressed " , "beforeTextChanged" + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("Pressed " , "onTextChanged" + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("Pressed " , "afterTextChanged" + s.toString());
                ArrayList<Words> newWordList = manager.searchWord(s.toString());
                for(Words word : newWordList)
                    System.out.println("newWords : "+word.getWord());
                CustomAdapter customAdapter1 = new CustomAdapter(newWordList);
                listView.setAdapter(customAdapter1);
            }
        });

    }

    private void fetchData(String string){

       // TrieNode node = manager.searchWord(string);

    }

    class CustomAdapter extends BaseAdapter {
        ArrayList<Words> adapterList;

        CustomAdapter(ArrayList<Words> adapterList){
            this.adapterList = adapterList;
        }

        @Override
        public int getCount() {
//            System.out.println("size of list "+wordsArrayList.size());
            return adapterList.size();
        }

        @Override
        public Words getItem(int position) {
            return adapterList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = getLayoutInflater().inflate(
                    R.layout.list_word,
                    parent,
                    false
            );              //Inflate View

            TextView tvWord = itemView.findViewById(R.id.tvWord);
            TextView tvMeaning = itemView.findViewById(R.id.tvMeaning);
            tvWord.setText(getItem(position).getWord());
            tvMeaning.setText(getItem(position).getMeaning());

            return itemView;
        }
    }

}

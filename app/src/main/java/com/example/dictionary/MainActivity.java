package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.SimpleArcLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    EditText editSearch;
    ListView listView;
    SimpleArcLoader simpleArcLoader;
    TrieNode root = new TrieNode('\0');
    TrieManager manager = new TrieManager(root);;
    Words words;
    ArrayList<Words> wordsArrayList;
    InputStream file ;
    String url;
    CustomAdapter customAdapter;
    static String currentWord = "";
    static String res = "";
    String KEY_1 = "ENCRYPTION";
    static String TAG = "TAG_TAG";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editSearch = findViewById(R.id.edtSearch);
        listView = findViewById(R.id.listView);
        simpleArcLoader = findViewById(R.id.loader);
        simpleArcLoader.start();
        try{
            file = getAssets().open("write.txt.txt");
        }catch (Exception e){

        }
        words = new Words();
        words.addToTrieWords(manager , file);

        wordsArrayList = manager.searchWord("");
        simpleArcLoader.stop();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        simpleArcLoader.setVisibility(View.GONE);

        customAdapter = new CustomAdapter(wordsArrayList);

        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, "Clicked " + customAdapter.getItem(position).getWord(), Toast.LENGTH_SHORT).show();
                url = dictionaryEntries(customAdapter.getItem(position).getWord());
//                Log.i("Clicked :" , customAdapter.getItem(position).getWord());
                currentWord = customAdapter.getItem(position).getWord();
                Log.i(TAG + "Clicked :" , currentWord);
                res = "";

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(snapshot.getKey().equals("Dictionary")) {

                                for (DataSnapshot currentSnapShot : snapshot.getChildren()) {
//                                    Log.d("FROM DB" , currentSnapShot.getKey().toString() + " :: " + currentSnapShot.getValue().toString());

                                    if(currentSnapShot.getKey().equals(currentWord)) {
//                                        Log.d("FROM DB" , currentSnapShot.getValue().toString());
                                        String current =  currentSnapShot.getValue().toString();
                                        res += current;
                                        Log.d("FROM DB" , res + res.length());
                                        break;
                                    }

                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


//        FirebaseDatabase.getInstance().getReference().child("Dictionary").child("Hello2").setValue("Meaning2");
//        FirebaseDatabase.getInstance().getReference().child("Dictionary").child("Hello3").setValue("Meaning2");

                if(res.length() != 0) {
//                    Toast.makeText(MainActivity.this, "FROM OLD" +res, Toast.LENGTH_LONG).show();
                    nextActivity(currentWord + ":" + res);
                }else {
                    RequestAPI();
                    Log.d(TAG + " Main res " , res);
                    if(res.length() != 0)
                        nextActivity(currentWord + ":" + res);
                }
            }
        });

//        simpleArcLoader.setVisibility(View.GONE);
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

                String currentString = s.toString().toLowerCase();
//                Log.i("Pressed " , "afterTextChanged" + s.toString());
                ArrayList<Words> newWordList = manager.searchWord(currentString);
                wordsArrayList = newWordList;
//                for(Words word : newWordList)
//                    System.out.println("newWords : "+word.getWord());

                if(newWordList.size() == 0)
                    newWordList.add(new Words(currentString, null));

                customAdapter = new CustomAdapter(wordsArrayList);

                listView.setAdapter(customAdapter);

            }
        });

    }


    private void nextActivity(String string){
        Log.d("Inside next " , string);
        Intent i = new Intent(MainActivity.this, DetailsActivity.class);
        i.putExtra(KEY_1 , string);
        startActivity(i);

    }

//    private void searchInDataBase() {
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    if(snapshot.getKey().equals("Dictionary")) {
//                        for (DataSnapshot currentSnapShot : snapshot.getChildren()) {
//                            Log.d("FROM DB" , currentSnapShot.getKey().toString() + " :: " + currentSnapShot.getValue().toString());
//
//                            if(currentSnapShot.getKey().equals(currentWord)) {
//                                Log.d("FROM DB" , currentSnapShot.getValue().toString());
//                                String current =  currentSnapShot.getValue().toString();
//                                res += current;
//                                Log.d("FROM DB" , res + res.length());
//                                break;
//                            }
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
////        FirebaseDatabase.getInstance().getReference().child("Dictionary").child("Hello2").setValue("Meaning2");
////        FirebaseDatabase.getInstance().getReference().child("Dictionary").child("Hello3").setValue("Meaning2");
//
//
//    }

    private void RequestAPI(){
        MyDictionaryRequest myDictionaryRequest = new MyDictionaryRequest(this  , currentWord );
        myDictionaryRequest.execute(url);
    }

    private String dictionaryEntries(String word) {
        final String language = "en-gb";
        final String fields = "definitions";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        final String url = "https://od-api.oxforddictionaries.com:443/api/v2/entries/";
        return url + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
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
            tvWord.setText(getItem(position).getWord());

            return itemView;
        }
    }

}

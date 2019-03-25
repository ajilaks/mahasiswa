package com.project.mahasiswa;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    String kk, value;
    EditText inputNama, updateNama;
    Button submit;
    private ListView mListView;
    mahasiswaModel keyInfo;
    List<mahasiswaModel> mahasiswaList = new ArrayList<>();;
    ArrayList<String> keyList = new ArrayList<>();
    ArrayList<String> mList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_item);
        databaseReference = FirebaseDatabase.getInstance().getReference("mahasiswa");
        inputNama = (EditText) findViewById(R.id.inputNama);
        inputNama.clearFocus();
        submit = (Button) findViewById(R.id.submit);
//        String[] mobileArray = convert(arrlist);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mList);



        inputNama.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submit.performClick();
                    return true;
                }
                return false;
            }
        });





        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList.clear();
                dataSnapshot.getKey();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) { //foreach memanggil data
                    mahasiswaModel post = postSnapshot.getValue(mahasiswaModel.class);
                    String parent = postSnapshot.getKey();
                    mList.add(post.getNama());
                    mahasiswaList.add(post);
                    Log.v("lihat",parent);

                }
                mListView.setAdapter(adapter);
               // hello.setText(post.nama);

//                System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 value = mListView.getItemAtPosition(position).toString();
                keyInfo =  mahasiswaList.get(position);
                keyInfo.getKey();


                final View v = getLayoutInflater().inflate(R.layout.dialog_topup, null);
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(value)
                        .setCancelable(false)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                             updateNama = v.findViewById(R.id.update);
                             String update = updateNama.getText().toString();
                                update(value);
//                                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getApplicationContext(),  keyInfo.getKey(), Toast.LENGTH_SHORT).show();
                               databaseReference.child( keyInfo.getKey()).child("nama").setValue(update);
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .setView(v)
                        .show();



            }
        });
    }

    public void post(View view){
        //mahasiswaModel k = new mahasiswaModel(inputNama.getText().toString()); //refrensikan variable
        String id = databaseReference.push().getKey();
        mahasiswaModel p = new mahasiswaModel(inputNama.getText().toString(),id);//refrensikan variable ke id
        databaseReference.child(id).setValue(p);

        inputNama.setText(""); //set Textview
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
        Toast.makeText(this, "Berhasil diPost", Toast.LENGTH_SHORT).show();


    }

    public void update(final String key){

        databaseReference.orderByChild("nama").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot datas: dataSnapshot.getChildren()){
                   kk=datas.getKey();
                    keyList.add(kk);

                }
               ;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}

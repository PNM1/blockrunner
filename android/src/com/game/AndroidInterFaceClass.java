package com.game;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import static android.content.ContentValues.TAG;

public class AndroidInterFaceClass implements FireBaseInterface {
    FirebaseDatabase database;
    DatabaseReference l5;
    DatabaseReference l1;
    DatabaseReference l2;
    DatabaseReference l3;
    DatabaseReference l4;
    DatabaseReference l6;
    DatabaseReference l7;
    DatabaseReference l8;
    DatabaseReference l9;
    DatabaseReference l10;
    DatabaseReference l11;
    DatabaseReference l12;
    DatabaseReference l13;
    DatabaseReference l14;
    DatabaseReference l15;
    DatabaseReference l16;
    DatabaseReference l17;
    DatabaseReference l18;
    DatabaseReference l19;
    DatabaseReference l20;


    public AndroidInterFaceClass()
    {
        database = FirebaseDatabase.getInstance("https://rrunc-aeced-default-rtdb.europe-west1.firebasedatabase.app");
        l5 = database.getReference("5");
        l1 = database.getReference("1");
        l2 = database.getReference("2");
        l3 = database.getReference("3");
        l4 = database.getReference("4");
        l6 = database.getReference("6");
        l7 = database.getReference("7");
        l8 = database.getReference("8");
        l9 = database.getReference("9");
        l10 = database.getReference("10");
        l11 = database.getReference("11");
        l12 = database.getReference("12");
        l13 = database.getReference("13");
        l14 = database.getReference("14");
        l15 = database.getReference("15");
        l16 = database.getReference("16");
        l17 = database.getReference("17");
        l18 = database.getReference("18");
        l19 = database.getReference("19");
        l20 = database.getReference("20");
    }

    @Override
    public void SetOnValueChangedListener(final DataHolderClass dataholder) {

        l1.addValueEventListener(new ValueEventListener() {
            // Read from the database

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value1 = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value 1 is: " + value1);
                dataholder.someValue = value1;
                dataholder.PrintSomeValue();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        l2.addValueEventListener(new ValueEventListener() {
            // Read from the database

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value 2 is: " + value);
                dataholder.someValue = value;
                dataholder.PrintSomeValue();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        l3.addValueEventListener(new ValueEventListener() {
            // Read from the database

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value 3 is: " + value);
                dataholder.someValue = value;
                dataholder.PrintSomeValue();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        l4.addValueEventListener(new ValueEventListener() {
            // Read from the database

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value 4 is: " + value);
                dataholder.someValue = value;
                dataholder.PrintSomeValue();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        l5.addValueEventListener(new ValueEventListener() {
            // Read from the database

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value5 = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value 5 is: " + value5);
                dataholder.someValue5 = value5;
                dataholder.PrintSomeValue();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void SetValueInDb(String target, String value) {



        l5 = database.getReference(target);
        l5.setValue(value);



    }



}

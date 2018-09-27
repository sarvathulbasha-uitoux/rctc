package com.google.firebase.example.fireeats.firestoreconfigs;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class FireStoreConfig {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build();

}

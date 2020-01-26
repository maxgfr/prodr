package com.maxgfr.prodr.model;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class FirebaseService {

    private static FirebaseService single_instance = null;
    private FirebaseFirestore db = null;

    private FirebaseService() {
        db = FirebaseFirestore.getInstance();
    }

    public static FirebaseService getInstance() {
        if (single_instance == null)
            single_instance = new FirebaseService();

        return single_instance;
    }

    public void findUser(String id, final AsyncLogin myInterface) {
        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println(document.getId() + " => " + document.getData());
                        myInterface.onSuccess("Login successful", LoginType.RECONNECT);
                    } else {
                        myInterface.onSuccess("Login successful", LoginType.CREATE);
                    }
                } else {
                    System.out.println("Get failed with " + task.getException());
                    myInterface.onFailure("Error login");
                }
            }
        });
    }

    public void createUser(String documentId, Map<String, Object> docData, final AsyncCreate myInterface) {
        DocumentReference docRef = db.collection("users").document(documentId);
        docRef.set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        myInterface.onSuccess("Data modified in database");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myInterface.onFailure("Error writing document");
                    }
                });
    }

    public void getData(String documentId, String collectionName, final AsyncGet myInterface) {
        DocumentReference docRef = db.collection(collectionName).document(documentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println(document.getId() + " => " + document.getData());
                        myInterface.onSuccess(document.getId(), document.getData());
                    } else {
                        myInterface.onFailure("Error");
                    }
                } else {
                    System.out.println("Get failed with " + task.getException());
                    myInterface.onFailure("Error");
                }
            }
        });
    }

    public void modifyData(String documentId, String collectionName, Map<String, Object> docData, final AsyncModify myInterface) {
        DocumentReference docRef = db.collection(collectionName).document(documentId);
        docRef.set(docData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        myInterface.onSuccess("DocumentSnapshot successfully modified!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myInterface.onFailure("Error writing document");
                    }
                });
    }

    public void getCollection(String collectionName, final AsyncCollectionGet myInterface) {
        CollectionReference collectionReference = db.collection(collectionName);
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, Map<String, Object>> data = new HashMap<>();
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            data.put(doc.getId(), doc.getData());
                        }
                        myInterface.onSuccess(data);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myInterface.onFailure("Error");
                    }
                });
    }

}

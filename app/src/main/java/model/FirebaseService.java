package model;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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


    public void createUser(String documentId, String collectionName, Map<String, Object> docData, final AsyncCreate myInterface) {
        DocumentReference docRef = db.collection(collectionName).document(documentId);
        docRef.set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        myInterface.onSuccess("DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myInterface.onFailure("Error writing document");
                    }
                });
    }
}
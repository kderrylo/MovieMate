package com.example.moviemate.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviemate.R;
import com.example.moviemate.repositories.MostPopularTVShowRepositories;
import com.example.moviemate.viewmodels.MostPopularTVShowViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    TextView username;
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    String userID;

    private MostPopularTVShowViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowViewModel.class);
        getMostPopularTVShow();


        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        username = findViewById(R.id.textHelloUsername);
        userID = auth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                username.setText(documentSnapshot.getString("username"));
            }
        });

    }

    private void getMostPopularTVShow(){
        viewModel.getMostPopularTVShow(0).observe(this, mostPopularTVShowResponse ->
                        Toast.makeText(
                                getApplicationContext(),
                                "Total Pages" + mostPopularTVShowResponse.getPages(),
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }
}
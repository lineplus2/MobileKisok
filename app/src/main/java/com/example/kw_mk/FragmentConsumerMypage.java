package com.example.kw_mk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;
import static com.example.kw_mk.LoginActivity.pref;

public class FragmentConsumerMypage extends Fragment {

    TextView name, email;

    private Context context;

    EditText modifyPassword;
    Button btn1, btn2, logout;
    TextView textview;

    DocumentReference docRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.example_consumer, container, false);

        final FirebaseUser user = App.mAuth.getCurrentUser();
        docRef = App.db.collection("User_Info").document(user.getEmail());

        context = container.getContext();

        modifyPassword = rootView.findViewById(R.id.example_editText);
        btn1 = rootView.findViewById(R.id.example_button);
        btn2 = rootView.findViewById(R.id.example_button2);
        textview = rootView.findViewById(R.id.example_textView);
        logout = rootView.findViewById(R.id.logout);


        if (user != null) {
            String email = user.getEmail();
            textview.setText(email);
        } else {
            Toast.makeText(context, "유저 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String newPassword = modifyPassword.getText().toString();
                // 비밀번호 변경
                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                }
                            }
                        });
                docRef.update("pw", newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

            }
        });


        // 로그아웃
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return rootView;

    }


    void logout() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ID", "");
        editor.putString("PWD", "");

        editor.commit();
        ConsumerMainActivity.AActivity.finish();
    }
}
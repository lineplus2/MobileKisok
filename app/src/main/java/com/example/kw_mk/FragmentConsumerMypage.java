package com.example.kw_mk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.kw_mk.LoginActivity.pref;

public class FragmentConsumerMypage extends Fragment {

    private Context context;

    EditText modifyPassword;
    Button btn1, logout;
    TextView textview;

    public static Button foreStart, foreStop;

    DocumentReference docRef;

    Intent serviceIntent;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.example_consumer, container, false);

        final FirebaseUser user = App.mAuth.getCurrentUser();
        docRef = App.db.collection("User_Info").document(user.getEmail());

        context = container.getContext();

        modifyPassword = rootView.findViewById(R.id.example_editText);
        btn1 = rootView.findViewById(R.id.example_button);
        textview = rootView.findViewById(R.id.example_textView);
        logout = rootView.findViewById(R.id.logout);
        foreStart = rootView.findViewById(R.id.example_foregroundStart);
        foreStop = rootView.findViewById(R.id.example_foregroundStop);


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
                // 비밀번호 변경 ( 변경 성공시 로그아웃 )
                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                                    // user_profile pw 변경
                                    docRef.update("pw", newPassword);
                                    logout();
                                }
                            }
                        });
            }
        });

        foreStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceIntent = new Intent(context, ServiceActivity.class);
                Toast.makeText(context, "서비스 시작", Toast.LENGTH_SHORT).show();
                getActivity().startService(serviceIntent);
            }
        });

        foreStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "서비스 종료", Toast.LENGTH_SHORT).show();
                getActivity().stopService(serviceIntent);
            }
        });


        // 로그아웃 버튼
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return rootView;

    }


    // 로그아웃
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
package a7aent.com.artour.registration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import a7aent.com.artour.R;

public class SignUp extends AppCompatActivity {

    private EditText mFirstname, mLastname, mEmail, mPassword, mConfirmPassword;
    private Button mSignup;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirstname = (EditText) findViewById(R.id.first_name);
        mLastname = (EditText) findViewById(R.id.last_name);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        mSignup = (Button) findViewById(R.id.sign_up);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference();

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mFirstname.getText().toString()) && !TextUtils.isEmpty(mLastname.getText().toString())
                        && !TextUtils.isEmpty(mEmail.getText().toString())
                && !TextUtils.isEmpty(mPassword.getText().toString()) && !TextUtils.isEmpty(mConfirmPassword.getText().toString())){

                    mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                                mUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),
                                                "A verification email is sent", Toast.LENGTH_LONG).show();

                                        HashMap<String, String> user = new HashMap<>();
                                        user.put("firstname", mFirstname.getText().toString());
                                        user.put("lastname", mLastname.getText().toString());
                                        user.put("email", mEmail.getText().toString());
                                        user.put("password", mPassword.getText().toString());

                                        DatabaseReference putUserData = mRef.child("users").child(mUser.getUid());
                                        putUserData.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Toast.makeText(getApplicationContext(), "Ready", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        Intent intent = new Intent(getApplicationContext(), AfterCofirmation.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });


                            }


                    });

                }
            }
        });

    }
}

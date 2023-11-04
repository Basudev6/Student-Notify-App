package com.example.studentnotifyapp.Student;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentnotifyapp.BaseFragment;
import com.example.studentnotifyapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileFragment extends BaseFragment {


    private TextView txtfullname,txtusername,txtaddress,txtphone;
    private ImageView editAddress,editPhone,editPassword;
    private LinearLayout layout;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        txtfullname = view.findViewById(R.id.stufullname);
        txtusername = view.findViewById(R.id.stuusername);
        txtaddress = view.findViewById(R.id.stuAddress);
        txtphone = view.findViewById(R.id.stuPhone);
        layout = view.findViewById(R.id.profile);

        editAddress = view.findViewById(R.id.edit_address);
        editPhone = view.findViewById(R.id.edit_phone);
        editPassword = view.findViewById(R.id.edit_password);

        String username = getContext().getSharedPreferences("login",MODE_PRIVATE).getString("username","");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot userSnapshot = snapshot.child(username);

                txtfullname.setText(userSnapshot.child("fullname").getValue(String.class));
                txtusername.setText(userSnapshot.child("username").getValue(String.class));
                txtaddress.setText(userSnapshot.child("address").getValue(String.class));
                txtphone.setText(userSnapshot.child("phone").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Update Address");

                final EditText address = new EditText(getContext());
                address.setHint("Enter address here");
                dialogBuilder.setView(address);

                dialogBuilder.setNeutralButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newAddress = address.getText().toString().trim();
                        if(!newAddress.isEmpty())
                        {
                            databaseReference.child(username).child("address").setValue(newAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    txtaddress.setText(newAddress);
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Address updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed to update address", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            address.setError("Please fill this field");
                        }
                    }
                });
            }
        });

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Update Phone");

                final EditText phone = new EditText(getContext());
                phone.setInputType(InputType.TYPE_CLASS_PHONE);
                phone.setHint("Enter phone number here");
                dialogBuilder.setView(phone);

                dialogBuilder.setNeutralButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newPhone = phone.getText().toString().trim();
                        Pattern pattern = Pattern.compile("^98\\d{8}$");
                        Matcher matcher = pattern.matcher(newPhone);
                        if(matcher.matches())
                        {
                            databaseReference.child(username).child("phone").setValue(newPhone).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    txtphone.setText(newPhone);
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Phone number updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed to update phone number", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            phone.setError("Invalid phone number");

                        }
                    }
                });
            }
        });
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Update Password");

                // Create a LinearLayout to hold the EditText fields
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText pass = new EditText(getContext());
                pass.setHint("Enter new password here");
                pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                layout.addView(pass);

                final EditText confirmpass = new EditText(getContext());
                confirmpass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                confirmpass.setHint("Enter confirm password here");
                layout.addView(confirmpass);

                dialogBuilder.setView(layout);

                dialogBuilder.setNeutralButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newPass = pass.getText().toString().trim();
                        String confirmPass = confirmpass.getText().toString().trim();
                        if(newPass.length()>7)
                        {
                            if(newPass.equals(confirmPass))
                            {
                                String StuHashPass;
                                try {
                                    StuHashPass = hashPassword(newPass);
                                } catch (NoSuchAlgorithmException e) {
                                    throw new RuntimeException(e);
                                }
                                databaseReference.child(username).child("password").setValue(StuHashPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                pass.setError("Password and confirm password is not same");
                            }
                        }
                        else {
                            pass.setError("Password must contain at least 8 characters");
                        }
                    }
                });
            }
        });

        return view;
    }

    public String hashPassword(String pass) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(pass.getBytes());
        BigInteger bigInt = new BigInteger(1,messageDigest);
        return bigInt.toString(16);
    }
}
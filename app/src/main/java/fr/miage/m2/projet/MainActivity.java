package fr.miage.m2.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FloatingActionButton nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav=findViewById(R.id.navigation);

    }

    @Override
    public void onClick(View view) {
        //Intent i = new Intent(Intent.ACTION_VIEW,
         //       Uri.parse("google.navigation:q=\t44.823462,\t-0.556514&mode=w"));
        Intent i = new Intent(this,MapsActivity.class);
        i.setPackage("com.google.android.apps.maps");

        if(i.resolveActivity(getPackageManager()) !=null){
            startActivity(i);

        }


    }
}
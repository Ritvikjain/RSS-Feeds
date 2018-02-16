package com.example.user.rssfeed;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Categories extends AppCompatActivity {

    RadioGroup rg;
    RadioButton r1,r2,r3,r4;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        rg=findViewById(R.id.rg);
        r1=findViewById(R.id.radioButton);
        r2=findViewById(R.id.radioButton2);
        r3=findViewById(R.id.radioButton3);
        r4=findViewById(R.id.radioButton4);
        next=findViewById(R.id.next);

        
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i=rg.getCheckedRadioButtonId();
                RadioButton r=findViewById(i);
                if(r!=null){
                    if(r.getText().toString().equalsIgnoreCase("Top Stories"))
                    {
                        //Toast.makeText(Categories.this, "Top Stories", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Categories.this,MainActivity.class);
                        intent.putExtra("category","topstories");
                        startActivity(intent);
                    }
                    else if (r.getText().toString().equalsIgnoreCase("India"))
                    {
                        //Toast.makeText(Categories.this, "India", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Categories.this,MainActivity.class);
                        intent.putExtra("category","india");
                        startActivity(intent);
                    }
                    else if (r.getText().toString().equalsIgnoreCase("Sports"))
                    {
                        //Toast.makeText(Categories.this, "Sports", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Categories.this,MainActivity.class);
                        intent.putExtra("category","sports");
                        startActivity(intent);
                    }
                    else if (r.getText().toString().equalsIgnoreCase("Business"))
                    {
                        //Toast.makeText(Categories.this, "Business", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Categories.this,MainActivity.class);
                        intent.putExtra("category","business");
                        startActivity(intent);
                    }

                }
                else {
                    Toast.makeText(Categories.this, "Select any option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

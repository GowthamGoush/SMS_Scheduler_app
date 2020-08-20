package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private TextView textView;
    private Button buttonTime,buttonSend,buttonCancel;
    private EditText editNum,editMsg;
    private String time;
    private Calendar c;
    private boolean Permission_granted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.timeText);
        buttonTime = findViewById(R.id.timeButton);
        buttonSend = findViewById(R.id.sendButton);
        buttonCancel = findViewById(R.id.cancelButton);
        editNum = findViewById(R.id.editTextPhone);
        editMsg = findViewById(R.id.editTextMsg);

        buttonCancel.setVisibility(View.GONE);

        CheckPermission();

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time picker");
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textView.getText().toString().isEmpty() && !editMsg.getText().toString().isEmpty() && !editNum.getText().toString().isEmpty() && Permission_granted){

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(MainActivity.this, AlertReceiver.class);
                    intent.putExtra("extra",editNum.getText().toString());
                    intent.putExtra("extra2",editMsg.getText().toString());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this ,1, intent,PendingIntent.FLAG_CANCEL_CURRENT);
                    if (c.before(Calendar.getInstance())) {
                        c.add(Calendar.DATE, 1);
                    }

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

                    buttonCancel.setVisibility(View.VISIBLE);

                    Toast toast = Toast.makeText(getApplicationContext(),"Your SMS has been scheduled successfully !",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    if(!Permission_granted){
                        Toast toast = Toast.makeText(getApplicationContext(),"Grant permission to proceed !",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Fields cannot be empty !", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(MainActivity.this, AlertReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, intent, 0);
                    alarmManager.cancel(pendingIntent);
                    buttonCancel.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(), "Your SMS has been cancelled successfully !", Toast.LENGTH_SHORT);
                    toast.show();
            }
        });

    }

    public void CheckPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)){

            }
            else {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.SEND_SMS},0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 0 :{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Permission_granted = true;
                    Toast toast = Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Permission was denied !",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            break;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        if(minute<10){
            time = hourOfDay+":0"+minute;
        }
        else {
            time =  hourOfDay+":"+minute;
        }

        textView.setText(time);

    }

}
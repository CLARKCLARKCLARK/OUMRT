package com.skypan.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;

    private EditText et_startTime;
    private EditText et_endTime;

    private Date startTime = new Date();
    private Date endTime = new Date();

    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.setButtonCustomDialog();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_House,R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_test)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setButtonCustomDialog(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        View v = getLayoutInflater().inflate(R.layout.set_custom_dialog_layout_with_button,null);
        alertDialog.setView(v);
        Button btOK = v.findViewById(R.id.button_ok);
        Button btC  = v.findViewById(R.id.buttonCancel);
        final EditText editText_name = v.findViewById(R.id.name);
        final EditText editText_start = v.findViewById(R.id.start);
        final EditText editText_end = v.findViewById(R.id.end);
        final EditText editText_startTime = v.findViewById(R.id.et_startTime);
        final EditText editText_endTime = v.findViewById(R.id.et_endTime);
        final AlertDialog dialog = alertDialog.create();
        String s = String.valueOf(editText_name);
        dialog.show();
        btOK.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                AlertDialog.Builder twoDialog = new AlertDialog.Builder(MainActivity.this);
                twoDialog.setTitle("這是疊上去的AlertDialog");
                twoDialog.setMessage(editText_name.getText() + "\n" + editText_start.getText() + "\n" + editText_end.getText() + "\n"
                        + editText_startTime.getText() + "\n"+ editText_endTime.getText() + "\n");
                twoDialog.setPositiveButton("瞭解", (new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                    }
                }));
                twoDialog.show();
            }
        }));
        btC.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                dialog.dismiss();
            }
        }));

        //time
        et_startTime = v.findViewById(R.id.et_startTime);
        et_endTime = v.findViewById(R.id.et_endTime);
        et_startTime.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                if (pvTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startTime);
                    pvTime.setDate(calendar);
                    pvTime.show(v1);
                }
            }
        }));
        et_endTime.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                if (pvTime != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(endTime);
                    pvTime.setDate(calendar);
                    pvTime.show(v1);
                }
            }
        }));

        initTimePicker();
    }


    private void setToast(String input){
        Toast.makeText(getBaseContext(),input,Toast.LENGTH_SHORT).show();
    }
    private void initTimePicker() {

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //如果是開始時間的EditText
                if(v.getId() == R.id.et_startTime){
                    startTime = date;
                }else {
                    endTime = date;
                }
                EditText editText = (EditText)v;
                editText.setText(getTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isDialog(true)
                .build();


        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改動畫樣式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部顯示
            }
        }
    }

    private String getTime(Date date) {//可根據需要自行擷取資料顯示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
        return format.format(date);
    }
}
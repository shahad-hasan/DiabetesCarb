package com.example.diabetescarbapp.activities.options;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.diabetescarbapp.R;
import com.example.diabetescarbapp.daos.BolusID_ReadingDao;
import com.example.diabetescarbapp.daos.CarbCoefficientDao;
import com.example.diabetescarbapp.db.MyDatabase;
import com.example.diabetescarbapp.models.BolusID_Reading;
import com.example.diabetescarbapp.services.SignInInfo;
import com.example.diabetescarbapp.utils.Converter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BolusInsulinDoseActivity extends AppCompatActivity {

    // declare views vars..
    private EditText before1Et, after1Et, before2Et, after2Et, before3Et, after3Et;
    private ImageButton infoIBtn, editIBtn, cancelIBtn, saveIBtn;

    private Dialog noteDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolus_insulin_dose);

        // initialize views vars..
        initViews();

        // retrieve data..
        retrieveData();

        // listening to views events..
        listenViewsEvents();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, FollowUpLogActivity.class));
        finish();
    }

    private void initViews() {
        before1Et = findViewById(R.id.et_before1);
        after1Et = findViewById(R.id.et_after1);
        before2Et = findViewById(R.id.et_before2);
        after2Et = findViewById(R.id.et_after2);
        before3Et = findViewById(R.id.et_before3);
        after3Et = findViewById(R.id.et_after3);
        infoIBtn = findViewById(R.id.ib_info);
        editIBtn = findViewById(R.id.ib_edit);
        cancelIBtn = findViewById(R.id.ib_cancel);
        saveIBtn = findViewById(R.id.ib_save);

        changeEditingMode(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void listenViewsEvents() {
        infoIBtn.setOnClickListener(v -> {
            showInfoDialog();
        });

        editIBtn.setOnClickListener(v -> {
            changeEditingMode(true);
        });

        cancelIBtn.setOnClickListener(v -> {
            changeEditingMode(false);
        });

        saveIBtn.setOnClickListener(v -> {
            checkInputsData();
        });
    }

    private void changeEditingMode(boolean mode) {
        before1Et.setEnabled(mode);
        after1Et.setEnabled(mode);
        before2Et.setEnabled(mode);
        after2Et.setEnabled(mode);
        before3Et.setEnabled(mode);
        after3Et.setEnabled(mode);

        editIBtn.setEnabled(!mode);
        cancelIBtn.setEnabled(mode);
        saveIBtn.setEnabled(mode);
    }

    private void refreshViews(float beforeMorning, float afterMorning, float beforeNoon, float afterNoon, float beforeNight, float afterNight) {
        before1Et.setText(String.valueOf(beforeMorning));
        after1Et.setText(String.valueOf(afterMorning));
        before2Et.setText(String.valueOf(beforeNoon));
        after2Et.setText(String.valueOf(afterNoon));
        before3Et.setText(String.valueOf(beforeNight));
        after3Et.setText(String.valueOf(afterNight));
    }

    private void showNoteMessageDialog() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkInputsData() {
        String beforeMorning = before1Et.getText().toString();
        String afterMorning = after1Et.getText().toString();
        String beforeNoon= before2Et.getText().toString();
        String afterNoon = after2Et.getText().toString();
        String beforeNight = before3Et.getText().toString();
        String afterNight = after3Et.getText().toString();

        if (beforeMorning.trim().isEmpty()) {
            before1Et.setError("this field is required..");
            before1Et.requestFocus();
            return;
        }

        if (afterMorning.trim().isEmpty()) {
            after1Et.setError("this field is required..");
            after1Et.requestFocus();
            return;
        }

        if (beforeNoon.trim().isEmpty()) {
            before3Et.setError("this field is required..");
            before3Et.requestFocus();
            return;
        }

        if (afterNoon.trim().isEmpty()) {
            after2Et.setError("this field is required..");
            after2Et.requestFocus();
            return;
        }

        if (beforeNight.trim().isEmpty()) {
            before3Et.setError("this field is required..");
            before3Et.requestFocus();
            return;
        }

        if (afterNight.trim().isEmpty()) {
            after3Et.setError("this field is required..");
            after3Et.requestFocus();
            return;
        }

        saveData(beforeMorning, afterMorning, beforeNoon, afterNoon, beforeNight, afterNight);
    }

    private void showInfoDialog() {
        noteDialog = new Dialog(this);
        noteDialog.setCancelable(false);
        noteDialog.setContentView(R.layout.note_dialog);

        TextView noteMessageTv = noteDialog.findViewById(R.id.tv_noteMessage);
        Button okBtn = noteDialog.findViewById(R.id.btn_ok);

        noteMessageTv.setText("سريع المفعول : ادخل قراءتك للسكر على مدار 3 أيام للتأكد من معامل الكارب و سيتم تعديلها عند الحاجة");

        okBtn.setOnClickListener(v -> {
            noteDialog.dismiss();
        });

        noteDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void retrieveData() {
        BolusID_ReadingDao dao = MyDatabase.getInstance(this).bolusID_readingDao();

        LocalDate now = LocalDate.now();

        String date = now.getDayOfMonth() + " " + now.getMonth().getValue() + " " + now.getYear();

        dao.getBolusInsulinDosesForUserAt(SignInInfo.getUserID(), date).observe(this, readings -> {
            if (readings != null) {
                refreshViews(
                        readings.beforeMorning,
                        readings.afterMorning,
                        readings.beforeNoon,
                        readings.afterNoon,
                        readings.beforeNight,
                        readings.afterNight
                );
            } else {
                refreshViews(
                        0f,
                        0f,
                        0f,
                        0f,
                        0f,
                        0f
                );
            }
        });

        CarbCoefficientDao carbDao = MyDatabase.getInstance(this).carbCoefficientDao();

        carbDao.getCarbCoefficientDateOfUser(SignInInfo.getUserID())
                .observe(this, lastDate -> {
                    if (checkPeriod(now, lastDate)) {
                        dao.getAfterNoonReadingAt(
                                SignInInfo.getUserID(),
                                Converter.getStrFromDate(now.minusDays(1))
                        ).observe(this, reading -> {
                            String strFromDate = Converter.getStrFromDate(now.plusDays(1));
                            System.out.println(strFromDate);
                            System.out.println(reading);
                            if (reading != null) {
                                updateCarb(checkState(reading), strFromDate);
                            }
                        });
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int checkState(float value) {
        if (value < 80) return -1;
        else if (value > 180) return 1;
        else return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkPeriod(LocalDate now, String date) {
        LocalDate nowDate = LocalDate.of(now.getYear(), now.getMonth().getValue(), now.getDayOfMonth());

        String[] lastDateStr = date.split(" ");
        LocalDate lastDate = LocalDate.of(Integer.parseInt(lastDateStr[2]), Integer.parseInt(lastDateStr[1]), Integer.parseInt(lastDateStr[0]));

        long between = ChronoUnit.DAYS.between(lastDate, nowDate);
        return between == 3;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveData(String beforeMorning, String afterMorning, String beforeNoon, String afterNoon, String beforeNight, String afterNight) {
        BolusID_ReadingDao dao = MyDatabase.getInstance(this).bolusID_readingDao();

        BolusID_Reading dose = new BolusID_Reading();
        dose.userId = SignInInfo.getUserID();

        dose.beforeMorning = Float.parseFloat(beforeMorning);
        dose.afterMorning = Float.parseFloat(afterMorning);
        dose.beforeNoon = Float.parseFloat(beforeNoon);
        dose.afterNoon = Float.parseFloat(afterNoon);
        dose.beforeNight = Float.parseFloat(beforeNight);
        dose.afterNight = Float.parseFloat(afterNight);

        LocalDate date = LocalDate.now();
        dose.date = date.getDayOfMonth() + " " + date.getMonth().getValue() + " " + date.getYear();

        MyDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(dose);
        });

        changeEditingMode(false);
    }

    private void updateCarb(int state, String date) {
        CarbCoefficientDao dao = MyDatabase.getInstance(this).carbCoefficientDao();

        System.out.println("update");
        MyDatabase.databaseWriteExecutor.execute(() -> {
            int value = dao.getValue(SignInInfo.getUserID());
            int newInc = (Math.round(value / 10f) * state);
            dao.update(SignInInfo.getUserID(), value + newInc, date);
            runOnUiThread(() -> showNoteDialog(newInc));
        });
    }

    private void showNoteDialog(int value) {
        noteDialog = new Dialog(this);
        noteDialog.setCancelable(false);
        noteDialog.setContentView(R.layout.note_dialog);

        TextView noteMessageTv = noteDialog.findViewById(R.id.tv_noteMessage);
        Button okBtn = noteDialog.findViewById(R.id.btn_ok);

        String message;

        if (value < 0) {
            message = "تم تقليل معامل الكارب بمقدار " + "(" + value + ")";
        } else if (value > 0) {
            message = "تم زيادة معامل الكارب بمقدار " + "(" + value + ")";
        } else {
            message = "لم يتم تقليل أو زيادة معامل الكارب";
        }

        noteMessageTv.setText(message);

        okBtn.setOnClickListener(v -> {
            noteDialog.dismiss();
        });

        noteDialog.show();
    }

}
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
import com.example.diabetescarbapp.daos.BasalIDDao;
import com.example.diabetescarbapp.daos.BasalID_ReadingDao;
import com.example.diabetescarbapp.db.MyDatabase;
import com.example.diabetescarbapp.models.BasalID_Reading;
import com.example.diabetescarbapp.services.SignInInfo;
import com.example.diabetescarbapp.utils.AlarmManagerBuilder;
import com.example.diabetescarbapp.utils.Converter;
import com.example.diabetescarbapp.utils.Request;
import com.example.diabetescarbapp.utils.TimePickerBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class BasalInsulinDoseActivity extends AppCompatActivity {

    // declare views vars..
    private EditText beforeEt, afterEt;
    private TextView doseValueTv, doseTimeTv;
    private ImageButton infoIBtn, editIBtn, cancelIBtn, saveIBtn;

    private Dialog noteDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basal_insulin_dose);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initViews() {
        beforeEt = findViewById(R.id.et_before);
        afterEt = findViewById(R.id.et_after);
        doseValueTv = findViewById(R.id.tv_doseValue);
        doseTimeTv = findViewById(R.id.tv_doseTime);
        infoIBtn = findViewById(R.id.ib_info);
        editIBtn = findViewById(R.id.ib_edit);
        cancelIBtn = findViewById(R.id.ib_cancel);
        saveIBtn = findViewById(R.id.ib_save);

        TimePickerBuilder.getInstance().build(this, doseTimeTv, this::update);

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
        beforeEt.setEnabled(mode);
        afterEt.setEnabled(mode);

        editIBtn.setEnabled(!mode);
        cancelIBtn.setEnabled(mode);
        saveIBtn.setEnabled(mode);
    }

    private void refreshViews(float beforeSleep, float afterSleep) {
        beforeEt.setText(String.valueOf(beforeSleep));
        afterEt.setText(String.valueOf(afterSleep));
    }

    private void refreshViews(int value, String time) {
        doseValueTv.setText(String.valueOf(value));
        doseTimeTv.setText(time);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkInputsData() {
        String before = beforeEt.getText().toString();
        String after = afterEt.getText().toString();

        if (before.trim().isEmpty()) {
            beforeEt.setError("this field is required..");
            beforeEt.requestFocus();
            return;
        }

        if (after.trim().isEmpty()) {
            afterEt.setError("this field is required..");
            afterEt.requestFocus();
            return;
        }

        saveData(before, after);
    }

    private void showNoteDialog(int extraValue) {
        noteDialog = new Dialog(this);
        noteDialog.setCancelable(false);
        noteDialog.setContentView(R.layout.note_dialog);

        TextView noteMessageTv = noteDialog.findViewById(R.id.tv_noteMessage);
        Button okBtn = noteDialog.findViewById(R.id.btn_ok);

        String message;

        switch (extraValue) {
            case -1:
                message = "تم تقليل الجرعة بمقدار " + "(-1 unit)";
                break;
            case 1:
                message = "تم زيادة الجرعة بمقدار " + "(1 unit)";
                break;
            default:
                message = "لم يتم تقليل أو زيادة الجرعة";
        }

        noteMessageTv.setText(message);

        okBtn.setOnClickListener(v -> {
            noteDialog.dismiss();
        });

        noteDialog.show();
    }

    private void showInfoDialog() {
        noteDialog = new Dialog(this);
        noteDialog.setCancelable(false);
        noteDialog.setContentView(R.layout.note_dialog);

        TextView noteMessageTv = noteDialog.findViewById(R.id.tv_noteMessage);
        Button okBtn = noteDialog.findViewById(R.id.btn_ok);

        noteMessageTv.setText("سريع المفعول : ادخل قراءتك للسكر على مدار 3 أيام للتأكد من معامل الكارب و سيتم تعديلها عند الحاجة");
        noteMessageTv.setText("طويل المفعول : يومياً قبل تناول أول وجبة لابد من قياس مستوى السكر و إدخال القراءة بالجدول");

        okBtn.setOnClickListener(v -> {
            noteDialog.dismiss();
        });

        noteDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void retrieveData() {
        BasalID_ReadingDao basalInsulinDoseDao = MyDatabase.getInstance(this).basalID_readingDao();

        LocalDate now = LocalTime.now().getHour() < 18 ? LocalDate.now() : LocalDate.now().plusDays(1);

        String date = now.getDayOfMonth() + " " + now.getMonth().getValue() + " " + now.getYear();

        basalInsulinDoseDao.getBasalInsulinDosesForUserAt(SignInInfo.getUserID(), date).observe(this, dose -> {
            if (dose != null) {
                refreshViews(dose.beforeSleep, dose.afterSleep);
            } else {
                refreshViews(0f, 0f);
            }
        });

        BasalIDDao dao = MyDatabase.getInstance(this).basalIDDao();

        dao.getCarbCoefficientDaoOfUser(SignInInfo.getUserID()).observe(this, basalID -> {
            refreshViews(basalID.value, basalID.time);

            if (checkPeriod(now.minusDays(1), basalID.date)) {
                basalInsulinDoseDao
                        .getBasalInsulinDosesForUserAt(SignInInfo.getUserID(), Converter.getStrFromDate(now.minusDays(1)))
//                        .getBasalInsulinDosesForUserAt(SignInInfo.getUserID(), Converter.getStrFromDate(now))
                        .observe(this, dose -> {
                            System.out.println(dose);
                    if (dose != null) {
                        int extraValue = 0;

                        if (dose.afterSleep < 80) {
                            extraValue = -1;
                        } else if (dose.afterSleep > 180) {
                            extraValue = 1;
                        }

                        update(extraValue, Converter.getStrFromDate(now));
//                        update(extraValue, Converter.getStrFromDate(now.plusDays(1)));
                    }
                });
            }
        });
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
    private void saveData(String before, String after) {
        BasalID_ReadingDao dao = MyDatabase.getInstance(this).basalID_readingDao();

        BasalID_Reading dose = new BasalID_Reading();
        dose.userId = SignInInfo.getUserID();
        dose.beforeSleep = Float.parseFloat(before);
        dose.afterSleep = Float.parseFloat(after);

        LocalDate date = LocalTime.now().getHour() < 18 ? LocalDate.now() : LocalDate.now().plusDays(1);
        dose.date = date.getDayOfMonth() + " " + date.getMonth().getValue() + " " + date.getYear();

        MyDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(dose);
        });

        changeEditingMode(false);
    }

    private void update(int extraValue, String date) {
        BasalIDDao dao = MyDatabase.getInstance(this).basalIDDao();

        MyDatabase.databaseWriteExecutor.execute(() -> {
            dao.update(SignInInfo.getUserID(), extraValue, date);
            runOnUiThread(() -> showNoteDialog(extraValue));
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void update(String time) {
        BasalIDDao dao = MyDatabase.getInstance(this).basalIDDao();

        MyDatabase.databaseWriteExecutor.execute(() -> {
            dao.updateTime(SignInInfo.getUserID(), time);

            updateAlarmData(time);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateAlarmData(String timeStr) {
        String[] time = timeStr.split(" ");
        AlarmManagerBuilder.getInstance().build(this,
                Converter.getTimeFromStr(time[0], time[1]),
                Request.BASAL_ID,
                "جرعة طويل المفعول",
                "إنه وقت أخذ جرعة طويل المفعول!!");
    }
}
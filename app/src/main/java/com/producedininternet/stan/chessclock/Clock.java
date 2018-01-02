package com.producedininternet.stan.chessclock;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class Clock extends AppCompatActivity implements View.OnFocusChangeListener {
    static int p2time[] = {0, 0, 0}, p1time[] = {0, 0, 0}, delay[] = {0, 0, 0}; //time of player 2 and player 1 and delay per move time
    CountDownTimer p2timer = null, p1timer = null, delaytimer = null;
    int player = 0; // current player move false -p1, true p2
    int increment[] = {0, 0, 0}, p1[] = {0, 0, 0}, p2[] = {0, 0, 0};
    int gameType = 0; //0 - time for game, 1 time for move
    boolean delayed = false, incremented = false, sameTime = true;
    static boolean gameOn = false, delayOn = false;
    Button buttonP1, buttonP2;
    TextView player1timeTV, player2timeTV;
    long pattern[] = {0, 200, 100, 200, 100, 200};
    Vibrator vibr;
    String p1name = null, p2name = null;
    EditText player1ET, player2ET;
    int page = 0;
    private AdView mAdView;


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (hasFocus) {
            imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);

        }
        if (!hasFocus) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putIntArray("p1time", p1time);
        savedInstanceState.putIntArray("p2time", p2time);
        savedInstanceState.putIntArray("p1", p1);
        savedInstanceState.putIntArray("p2", p2);
        savedInstanceState.putIntArray("delay", delay);
        savedInstanceState.putIntArray("increment", increment);
        savedInstanceState.putInt("game_type", gameType);

        savedInstanceState.putBoolean("delayed", delayed);
        savedInstanceState.putBoolean("incremented", incremented);
        savedInstanceState.putBoolean("sameTime", sameTime);
        savedInstanceState.putString("p1name", p1name);
        savedInstanceState.putString("p2name", p2name);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        p1time = savedInstanceState.getIntArray("p1time");
        p2time = savedInstanceState.getIntArray("p2time");
        p1 = savedInstanceState.getIntArray("p1");
        p2 = savedInstanceState.getIntArray("p2");
        delay = savedInstanceState.getIntArray("delay");
        increment = savedInstanceState.getIntArray("increment");
        gameType = savedInstanceState.getInt("game_type");

        delayed = savedInstanceState.getBoolean("delayed");
        incremented = savedInstanceState.getBoolean("incemented");
        sameTime = savedInstanceState.getBoolean("sameTime");
        p1name = savedInstanceState.getString("p1name");
        p2name = savedInstanceState.getString("p2name");
    }

    @Override
    public void onPause(){
    pause();
    super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings);
        vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        showAD();
        player1ET = (EditText) findViewById(R.id.player1);
        player2ET = (EditText) findViewById(R.id.player2);
        player1ET.setOnFocusChangeListener(this);
        player2ET.setOnFocusChangeListener(this);
    }

    public void gameType0(View view) {
        CheckBox delayCB = (CheckBox) findViewById(R.id.delay_time_CB);
        LinearLayout delayLayout = (LinearLayout) findViewById(R.id.delay_layout);
        CheckBox incrementCB = (CheckBox) findViewById(R.id.increment_time_CB);
        LinearLayout incrementLayout = (LinearLayout) findViewById(R.id.increment_layout);
        View line1 = findViewById(R.id.horizontal_line1);
        View line2 = findViewById(R.id.horizontal_line2);

        gameType = 0;
        delayCB.setVisibility(View.VISIBLE);
        if (delayCB.isChecked())
            delayLayout.setVisibility(View.VISIBLE);
        incrementCB.setVisibility(View.VISIBLE);
        if (incrementCB.isChecked())
            incrementLayout.setVisibility(View.VISIBLE);
        line1.setVisibility(View.VISIBLE);
        line2.setVisibility(View.VISIBLE);
    }

    public void gameType1(View view) {
        CheckBox delayCB = (CheckBox) findViewById(R.id.delay_time_CB);
        LinearLayout delayLayout = (LinearLayout) findViewById(R.id.delay_layout);
        CheckBox incrementCB = (CheckBox) findViewById(R.id.increment_time_CB);
        LinearLayout incrementLayout = (LinearLayout) findViewById(R.id.increment_layout);
        View line1 = findViewById(R.id.horizontal_line1);
        View line2 = findViewById(R.id.horizontal_line2);

        gameType = 1;
        delayCB.setVisibility(View.GONE);
        delayLayout.setVisibility(View.GONE);
        incrementCB.setVisibility(View.GONE);
        incrementLayout.setVisibility(View.GONE);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
    }


    public void start(View view) {
        EditText player1 = (EditText) findViewById(R.id.player1);
        EditText player2 = (EditText) findViewById(R.id.player2);
//        CharSequence player1name, player2name;
        p1name = player1.getText().toString();
        p2name = player2.getText().toString();
        p1timer = null;
        p2timer = null;

        setContentView(R.layout.activity_clock);
        page = 1;
        buttonP1 = findViewById(R.id.player1_button);
        buttonP2 = findViewById(R.id.player2_button);
        TextView player1txt = (TextView) findViewById(R.id.player1_name);
        TextView player2txt = (TextView) findViewById(R.id.player2_name);
        player1txt.setText(p1name + " " + getString(R.string.time));
        player2txt.setText(p2name + " " + getString(R.string.time));
        for (int i = 0; i < 3; i++) {
            p1time[i] = p1[i];
            if (sameTime)
                p2time[i] = p1[i];
            else
                p2time[i] = p2[i];
        }
        if (gameType == 0) {
            if (incremented && (increment[0] == 0 && increment[1] == 0 && increment[2] == 0)) {
                incremented = false;
            }
            if (delayed && (delay[0] == 0 && delay[1] == 0 && delay[2] == 0)) {
                delayed = false;
            }
            if (delayed) {
                player1txt = (TextView) findViewById(R.id.delay_time1);
                player2txt = (TextView) findViewById(R.id.delay_time2);
                player1txt.setVisibility(View.VISIBLE);
                player2txt.setVisibility(View.VISIBLE);
                player1txt.setText(int3toStr(delay));
                player2txt.setText(int3toStr(delay));
            }
        } else if (gameType == 1) {
            delayed = false;
            incremented = false;
            for (int i = 0; i < 3; i++) {
                p2[i] = p2time[i];
            }
        }
        player1timeTV = (TextView) findViewById(R.id.player1_time);
        player2timeTV = (TextView) findViewById(R.id.player2_time);

        player1timeTV.setText(int3toStr(p1time));
        player2timeTV.setText(int3toStr(p2time));

        gameOn = true;
    }

    public void player1TimerStart() {
        p1timer = new CountDownTimer((p1time[0] * 3600 + p1time[1] * 60 + p1time[2]) * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                player1timeTV.setText(millisToString(millisUntilFinished, 1));
            }

            public void onFinish() {
                player1timeTV.setText("Time's up!");
                theEnd();

            }
        }.start();

    }

    public void player2TimerStart() {
        p2timer = new CountDownTimer((p2time[0] * 3600 + p2time[1] * 60 + p2time[2]) * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                player2timeTV.setText(millisToString(millisUntilFinished, 2));

            }

            public void onFinish() {
                player2timeTV.setText("Time's up!");
                theEnd();
            }
        }.start();

    }


    protected void startDelay() {
        delayOn = true;
        if (player == 2) {
            delaytimer = new CountDownTimer((delay[0] * 3600 + delay[1] * 60 + delay[2]) * 1000, 1000) {
                TextView delayTime = (TextView) findViewById(R.id.delay_time1);

                public void onTick(long millisUntilFinished) {
                    delayTime.setText("Delay:" + millisToString(millisUntilFinished, 10));
                }

                public void onFinish() {
                    delayTime.setText("Delay: 00:00:00");
                    Clock.delayOn = false;
                    player1TimerStart();
                }
            }.start();
        }
        if (player == 1) {
            delaytimer = new CountDownTimer((delay[0] * 3600 + delay[1] * 60 + delay[2]) * 1000, 1000) {
                TextView delayTime = (TextView) findViewById(R.id.delay_time2);

                public void onTick(long millisUntilFinished) {
                    delayTime.setText("Delay:" + millisToString(millisUntilFinished, 10));
                }

                public void onFinish() {
                    delayTime.setText("Delay: 00:00:00");
                    Clock.delayOn = false;
                    player2TimerStart();

                }
            }.start();

        }
    }

    /**
     * this function stops player1 counters and starts player 2 counters with all fireworks like delay and increment
     *
     * @param view android view
     */

    public void player1(View view) throws InterruptedException {

        if ((player == 1 || player == 0) && gameOn) {
            buttonP1.setBackgroundColor(Color.argb(100, 255, 152, 0));
            buttonP2.setBackgroundColor(Color.rgb(255, 152, 0));
            if (gameType == 1) {
                for (int i = 0; i < 3; i++) {
                    p2time[i] = p2[i];
                }

            }
            if (p2timer != null && incremented && gameType == 0) {
                for (int i = 0; i < 3; i++) {
                    p2time[i] += increment[i];
                    if (p2time[i] > 60 && i > 0) {
                        p2time[i] -= 60;
                        p2time[i - 1]++;
                    }
                }

            }
            if (delayOn) {
                delaytimer.cancel();
            } else if (p1timer != null)
                p1timer.cancel();
            if (delayed) {
                player = 1;
                startDelay();
                player = 2;
            } else {
                player2TimerStart();
                player = 2;
            }
            vibr.vibrate(200);
        }
        if (!gameOn) {
            restart();
        }
    }

    /**
     * this function stops player2 counters adds tt2 amount of secs and starts player 1 counter
     *
     * @param view android view
     */
    public void player2(View view) {

        if ((player == 2 || player == 0) && gameOn) {
            buttonP2.setBackgroundColor(Color.argb(100, 255, 152, 0));
            buttonP1.setBackgroundColor(Color.rgb(255, 152, 0));
            if (gameType == 1) {
                for (int i = 0; i < 3; i++) {
                    p1time[i] = p1[i];
                }
            }
            if (p1timer != null && incremented && gameType == 0) {
                for (int i = 0; i < 3; i++) {
                    p1time[i] += increment[i];
                    if (p1time[i] > 60 && i > 0) {
                        p1time[i] -= 60;
                        p1time[i - 1]++;
                    }
                }
            }
            if (delayOn) {
                delaytimer.cancel();
            } else if (p2timer != null)
                p2timer.cancel();
            if (delayed) {
                player = 2;
                startDelay();
                player = 1;
            } else {
                player1TimerStart();
                player = 1;
            }
            vibr.vibrate(200);
        }

        if (!gameOn) {
            restart();
        }
    }

    /**
     * adds one to time value
     *
     * @param view
     */
    public void addOne(View view) {
        switch (view.getId()) {

            case R.id.add_delay_hour:
                TextView delay_hour = (TextView) findViewById(R.id.delay_hour);
                if (delay[0] < 12)
                    delay[0]++;
                else
                    delay[0] = 0;
                if (delay[0] < 10) {
                    delay_hour.setText("h: 0" + delay[0]);
                    break;
                }
                delay_hour.setText("h: " + delay[0]);
                break;

            case R.id.add_delay_min:
                TextView delay_min = (TextView) findViewById(R.id.delay_min);
                if (delay[1] < 59)
                    delay[1]++;
                else
                    delay[1] = 0;
                if (delay[1] < 10) {
                    delay_min.setText("min: 0" + delay[1]);
                    break;
                }
                delay_min.setText("min: " + delay[1]);
                break;

            case R.id.add_delay_sec:
                TextView delay_sec = (TextView) findViewById(R.id.delay_sec);
                if (delay[2] < 59)
                    delay[2]++;
                else
                    delay[2] = 0;
                if (delay[2] < 10) {
                    delay_sec.setText("sec: 0" + delay[2]);
                    break;
                }
                delay_sec.setText("sec: " + delay[2]);
                break;


            case R.id.add_increment_hour:
                TextView increment_hour = (TextView) findViewById(R.id.increment_hour);
                if (increment[0] < 12)
                    increment[0]++;
                else
                    increment[0] = 0;
                if (increment[0] < 10) {
                    increment_hour.setText("h: 0" + increment[0]);
                    break;
                }
                increment_hour.setText("h: " + increment[0]);
                break;

            case R.id.add_increment_min:
                TextView increment_min = (TextView) findViewById(R.id.increment_min);
                if (increment[1] < 59)
                    increment[1]++;
                else
                    increment[1] = 0;
                if (increment[1] < 10) {
                    increment_min.setText("min: 0" + increment[1]);
                    break;
                }
                increment_min.setText("min: " + increment[1]);
                break;

            case R.id.add_increment_sec:
                TextView increment_sec = (TextView) findViewById(R.id.increment_sec);
                if (increment[2] < 59)
                    increment[2]++;
                else
                    increment[2] = 0;
                if (increment[2] < 10) {
                    increment_sec.setText("sec: 0" + increment[2]);
                    break;
                }
                increment_sec.setText("sec: " + increment[2]);
                break;


            case R.id.add_p1_hour:
                TextView p1_hour = (TextView) findViewById(R.id.p1_hour);
                if (p1[0] < 12)
                    p1[0]++;
                else
                    p1[0] = 0;
                if (p1[0] < 10) {
                    p1_hour.setText("h: 0" + p1[0]);
                    break;
                }
                p1_hour.setText("h: " + p1[0]);
                break;

            case R.id.add_p1_min:
                TextView p1_min = (TextView) findViewById(R.id.p1_min);
                if (p1[1] < 59)
                    p1[1]++;
                else
                    p1[1] = 0;
                if (p1[1] < 10) {
                    p1_min.setText("min: 0" + p1[1]);
                    break;
                }
                p1_min.setText("min: " + p1[1]);
                break;

            case R.id.add_p1_sec:
                TextView p1_sec = (TextView) findViewById(R.id.p1_sec);
                if (p1[2] < 59)
                    p1[2]++;
                else
                    p1[2] = 0;
                if (p1[2] < 10) {
                    p1_sec.setText("sec: 0" + p1[2]);
                    break;
                }
                p1_sec.setText("sec: " + p1[2]);
                break;


            case R.id.add_p2_hour:
                TextView p2_hour = (TextView) findViewById(R.id.p2_hour);
                if (p2[0] < 12)
                    p2[0]++;
                else
                    p2[0] = 0;
                if (p2[0] < 10) {
                    p2_hour.setText("h: 0" + p2[0]);
                    break;
                }
                p2_hour.setText("h: " + p2[0]);
                break;

            case R.id.add_p2_min:
                TextView p2_min = (TextView) findViewById(R.id.p2_min);
                if (p2[1] < 59)
                    p2[1]++;
                else
                    p2[1] = 0;
                if (p2[1] < 10) {
                    p2_min.setText("min: 0" + p2[1]);
                    break;
                }
                p2_min.setText("min: " + p2[1]);
                break;

            case R.id.add_p2_sec:
                TextView p2_sec = (TextView) findViewById(R.id.p2_sec);
                if (p2[2] < 59)
                    p2[2]++;
                else
                    p2[2] = 0;
                if (p2[2] < 10) {
                    p2_sec.setText("sec: 0" + p2[2]);
                    break;
                }
                p2_sec.setText("sec: " + p2[2]);
                break;

        }
    }

    /**
     * remove one from time value
     *
     * @param view
     */
    public void minusOne(View view) {
        switch (view.getId()) {

            case R.id.rem_delay_hour:
                TextView delay_hour = (TextView) findViewById(R.id.delay_hour);
                if (delay[0] > 0)
                    delay[0]--;
                else
                    delay[0] = 12;
                if (delay[0] < 10) {
                    delay_hour.setText("h: 0" + delay[0]);
                    break;
                }
                delay_hour.setText("h: " + delay[0]);
                break;

            case R.id.rem_delay_min:
                TextView delay_min = (TextView) findViewById(R.id.delay_min);
                if (delay[1] > 0)
                    delay[1]--;
                else
                    delay[1] = 59;
                if (delay[1] < 10) {
                    delay_min.setText("min: 0" + delay[1]);
                    break;
                }
                delay_min.setText("min: " + delay[1]);
                break;

            case R.id.rem_delay_sec:
                TextView delay_sec = (TextView) findViewById(R.id.delay_sec);
                if (delay[2] > 0)
                    delay[2]--;
                else
                    delay[2] = 59;
                if (delay[2] < 10) {
                    delay_sec.setText("sec: 0" + delay[2]);
                    break;
                }
                delay_sec.setText("sec: " + delay[2]);
                break;


            case R.id.rem_increment_hour:
                TextView increment_hour = (TextView) findViewById(R.id.increment_hour);
                if (increment[0] > 0)
                    increment[0]--;
                else
                    increment[0] = 12;
                if (increment[0] < 10) {
                    increment_hour.setText("h: 0" + increment[0]);
                    break;
                }
                increment_hour.setText("h: " + increment[0]);
                break;

            case R.id.rem_increment_min:
                TextView increment_min = (TextView) findViewById(R.id.increment_min);
                if (increment[1] > 0)
                    increment[1]--;
                else
                    increment[1] = 59;
                if (increment[1] < 10) {
                    increment_min.setText("min: 0" + increment[1]);
                    break;
                }
                increment_min.setText("min: " + increment[1]);
                break;

            case R.id.rem_increment_sec:
                TextView increment_sec = (TextView) findViewById(R.id.increment_sec);
                if (increment[2] > 0)
                    increment[2]--;
                else
                    increment[2] = 59;
                if (increment[2] < 10) {
                    increment_sec.setText("sec: 0" + increment[2]);
                    break;
                }
                increment_sec.setText("sec: " + increment[2]);
                break;


            case R.id.rem_p1_hour:
                TextView p1_hour = (TextView) findViewById(R.id.p1_hour);
                if (p1[0] > 0)
                    p1[0]--;
                else
                    p1[0] = 12;
                if (p1[0] < 10) {
                    p1_hour.setText("h: 0" + p1[0]);
                    break;
                }
                p1_hour.setText("h: " + p1[0]);
                break;

            case R.id.rem_p1_min:
                TextView p1_min = (TextView) findViewById(R.id.p1_min);
                if (p1[1] > 0)
                    p1[1]--;
                else
                    p1[1] = 59;
                if (p1[1] < 10) {
                    p1_min.setText("min: 0" + p1[1]);
                    break;
                }
                p1_min.setText("min: " + p1[1]);
                break;

            case R.id.rem_p1_sec:
                TextView p1_sec = (TextView) findViewById(R.id.p1_sec);
                if (p1[2] > 0)
                    p1[2]--;
                else
                    p1[2] = 59;
                if (p1[2] < 10) {
                    p1_sec.setText("sec: 0" + p1[2]);
                    break;
                }
                p1_sec.setText("sec: " + p1[2]);
                break;


            case R.id.rem_p2_hour:
                TextView p2_hour = (TextView) findViewById(R.id.p2_hour);
                if (p2[0] > 0)
                    p2[0]--;
                else
                    p2[0] = 12;
                if (p2[0] < 10) {
                    p2_hour.setText("h: 0" + p2[0]);
                    break;
                }
                p2_hour.setText("h: " + p2[0]);
                break;

            case R.id.rem_p2_min:
                TextView p2_min = (TextView) findViewById(R.id.p2_min);
                if (p2[1] > 0)
                    p2[1]--;
                else
                    p2[1] = 59;
                if (p2[1] < 10) {
                    p2_min.setText("min: 0" + p2[1]);
                    break;
                }
                p2_min.setText("min: " + p2[1]);
                break;

            case R.id.rem_p2_sec:
                TextView p2_sec = (TextView) findViewById(R.id.p2_sec);
                if (p2[2] > 0)
                    p2[2]--;
                else
                    p2[2] = 59;
                if (p2[2] < 10) {
                    p2_sec.setText("sec: 0" + p2[2]);
                    break;
                }
                p2_sec.setText("sec: " + p2[2]);
                break;

        }
    }

    /**
     * logic behind incremented check box
     *
     * @param view checkbox view
     */
    public void isIncremented(View view) {
        CheckBox incrView = (CheckBox) view;
        LinearLayout layout = (LinearLayout) findViewById(R.id.increment_layout);
        if (incrView.isChecked()) {
            incremented = true;
            layout.setVisibility(View.VISIBLE);
        } else {
            incremented = false;
            layout.setVisibility(View.GONE);
        }

    }

    /**
     * logic behind delayed check box
     *
     * @param view checkbox view
     */
    public void isDeleyed(View view) {
        CheckBox incrView = (CheckBox) view;
        LinearLayout layout = (LinearLayout) findViewById(R.id.delay_layout);
        if (incrView.isChecked()) {
            delayed = true;
            layout.setVisibility(View.VISIBLE);
        } else {
            delayed = false;
            layout.setVisibility(View.GONE);
        }

    }

    /**
     * logic behind same time check box
     *
     * @param view checkbox view
     */
    public void isSame(View view) {
        CheckBox incrView = (CheckBox) view;
        LinearLayout layout = (LinearLayout) findViewById(R.id.player2_time_layout);
        TextView textP1 = (TextView) findViewById(R.id.player1_time_label);
        TextView textP2 = (TextView) findViewById(R.id.player2_time_label);
        if (incrView.isChecked()) {
            sameTime = true;
            layout.setVisibility(View.GONE);
            textP1.setText(getString(R.string.playerTime));
            textP2.setVisibility(View.GONE);
        } else {
            sameTime = false;
            layout.setVisibility(View.VISIBLE);
            textP1.setText(getString(R.string.p1time));
            textP2.setVisibility(View.VISIBLE);
            layout.invalidate();
        }

    }

    public void pause(View view) {
        pause();

    }

    private void pause() {
        if (player != 0) {
            buttonP1.setBackgroundColor(Color.rgb(255, 152, 0));
            buttonP2.setBackgroundColor(Color.rgb(255, 152, 0));
            if (delayOn) {
                delaytimer.cancel();
            } else if (player == 2) {
                p2timer.cancel();
            } else if (player == 1) {
                p1timer.cancel();
            }
            player = 0;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
                && !event.isCanceled() && event.getRepeatCount() == 0 && page == 1) {
            pause();
            setContentView(R.layout.settings);
            populateSettings();
            showAD();
            page = 0;


            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public String millisToString(long time, int option) {
        time -= (time % 1000);
        long hours = time / 3600000;
        time -= (hours * 3600000);

        long mins = time / 60000;
        time -= (mins * 60000);
        long sec = time / 1000;
        switch (option) {
            case (1):
                p1time[0] = (int) hours;
                p1time[1] = (int) mins;
                p1time[2] = (int) sec;
                return int3toStr(p1time);
            case (2):
                p2time[0] = (int) hours;
                p2time[1] = (int) mins;
                p2time[2] = (int) sec;
                return int3toStr(p2time);
            default:
                int work[] = {0, 0, 0};
                work[0] = (int) hours;
                work[1] = (int) mins;
                work[2] = (int) sec;
                return int3toStr(work);
        }

    }


    private void theEnd() {
        buttonP2.setBackgroundColor(Color.rgb(255, 152, 0));
        buttonP2.setBackgroundColor(Color.rgb(255, 152, 0));
        vibr.vibrate(pattern, -1);
        gameOn = false;

    }

    private void restart() {
        pause();
        player = 0;
        gameOn = true;
        for (int i = 0; i < 3; i++) {
            p1time[i] = p1[i];
            if (sameTime)
                p2time[i] = p1[i];
            else
                p2time[i] = p2[i];
        }
        player1timeTV.setText(int3toStr(p1time));
        player2timeTV.setText(int3toStr(p2time));
    }

    public void restart(View view) {
        restart();
    }

    private String int3toStr(int time[]) {
        String hr, min, sec;
        if (time[0] < 10)
            hr = "0" + time[0];
        else
            hr = "" + time[0];
        if (time[1] < 10)
            min = "0" + time[1];
        else
            min = "" + time[1];
        if (time[2] < 10)
            sec = "0" + time[2];
        else
            sec = "" + time[2];

        return hr + ":" + min + ":" + sec;
    }

    private void populateSettings() {
        TextView currentTV;
        CheckBox checkBox;

        /////populate delay view
        if (delayed) {
            checkBox = findViewById(R.id.delay_time_CB);
            checkBox.setChecked(true);
            isDeleyed(checkBox);
        }
        if (delay[0] > 0) {
            currentTV = (TextView) findViewById(R.id.delay_hour);
            if (delay[0] < 10)
                currentTV.setText("h: 0" + delay[0]);
            else
                currentTV.setText("h: " + delay[0]);
        }
        if (delay[1] > 0) {
            currentTV = (TextView) findViewById(R.id.delay_min);
            if (delay[1] < 10)
                currentTV.setText("min: 0" + delay[1]);
            else
                currentTV.setText("min: " + delay[1]);
        }
        if (delay[2] > 0) {
            currentTV = (TextView) findViewById(R.id.delay_sec);
            if (delay[2] < 10)
                currentTV.setText("sec: 0" + delay[2]);
            else
                currentTV.setText("sec: " + delay[2]);
        }


//////////////////populate increment view
        if (incremented) {
            checkBox = findViewById(R.id.increment_time_CB);
            checkBox.setChecked(true);
            isIncremented(checkBox);
        }
        if (increment[0] > 0) {
            currentTV = (TextView) findViewById(R.id.increment_hour);
            if (increment[0] < 10)
                currentTV.setText("h: 0" + increment[0]);
            else
                currentTV.setText("h: " + increment[0]);
        }
        if (increment[1] > 0) {
            currentTV = (TextView) findViewById(R.id.increment_min);
            if (increment[1] < 10)
                currentTV.setText("min: 0" + increment[1]);
            else
                currentTV.setText("min: " + increment[1]);
        }
        if (increment[2] > 0) {
            currentTV = (TextView) findViewById(R.id.increment_sec);
            if (increment[2] < 10)
                currentTV.setText("sec: 0" + increment[2]);
            else
                currentTV.setText("sec: " + increment[2]);
        }
/////////////////populate player 1 view
        checkBox = findViewById(R.id.same_time_CB);
        if (sameTime) {
            checkBox.setChecked(true);
            isSame(checkBox);
        } else {
            checkBox.setChecked(false);
            isSame(checkBox);
        }
        if (p1[0] > 0) {
            currentTV = (TextView) findViewById(R.id.p1_hour);
            if (p1[0] < 10)
                currentTV.setText("h: 0" + p1[0]);
            else
                currentTV.setText("h: " + p1[0]);
        }
        if (p1[1] > 0) {
            currentTV = (TextView) findViewById(R.id.p1_min);
            if (p1[1] < 10)
                currentTV.setText("min: 0" + p1[1]);
            else
                currentTV.setText("min: " + p1[1]);
        }
        if (p1[2] > 0) {
            currentTV = (TextView) findViewById(R.id.p1_sec);
            if (p1[2] < 10)
                currentTV.setText("sec: 0" + p1[2]);
            else
                currentTV.setText("sec: " + p1[2]);
        }

        ////populate player 2 timer
        if (p2[0] > 0) {
            currentTV = (TextView) findViewById(R.id.p2_hour);
            if (p2[0] < 10)
                currentTV.setText("h: 0" + p2[0]);
            else
                currentTV.setText("h: " + p2[0]);
        }
        if (p2[1] > 0) {
            currentTV = (TextView) findViewById(R.id.p2_min);
            if (p2[1] < 10)
                currentTV.setText("min: 0" + p2[1]);
            else
                currentTV.setText("min: " + p2[1]);
        }
        if (p2[2] > 0) {
            currentTV = (TextView) findViewById(R.id.p2_sec);
            if (p2[2] < 10)
                currentTV.setText("sec: 0" + p2[2]);
            else
                currentTV.setText("sec: " + p2[2]);
        }
//// populate players names
        EditText playerNameET;
        if (p1name != null) {
            playerNameET = (EditText) findViewById(R.id.player1);
            playerNameET.setText(p1name);
        }
        if (p1name!=null) {
            playerNameET = (EditText) findViewById(R.id.player2);
            playerNameET.setText(p2name);
        }
//// populate game type radio button
        RadioButton gameTypeRB;
        RadioGroup gameTypeRG = findViewById(R.id.time_type);
        if (gameType == 0) {
            gameTypeRB = findViewById(R.id.time_per_game);
            gameTypeRG.check(R.id.time_per_game);
            gameType0(gameTypeRB);
        }
        if (gameType == 1) {
            gameTypeRB = findViewById(R.id.time_per_move);
            gameTypeRG.check(R.id.time_per_move);
            gameType1(gameTypeRB);
        }


    }
    private void showAD(){
        MobileAds.initialize(this,
                "ca-app-pub-7656241846195885~7864041210");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}



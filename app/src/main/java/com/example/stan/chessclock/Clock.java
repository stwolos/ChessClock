package com.example.stan.chessclock;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;



public class Clock extends AppCompatActivity {
    static int p2time[] = {0, 0, 0}, p1time[] = {0, 0, 0}, delay[] = {0, 0, 0}; //time of player 2 and player 1 and delay per move time
    CountDownTimer p2timer = null, p1timer = null, delaytimer = null;
    int player = 0; // current player move false -p1, true p2
    int increment[] = {0, 0, 0}, p1[] = {0, 0, 0}, p2[] = {0, 0, 0};
    int gameType = 0; //0 - time for game, 1 time for move
    boolean delayed = false, incremented = false, sameTime = true;
    static boolean gameOn = false, delayOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.settings);
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
        if(delayCB.isChecked())
            delayLayout.setVisibility(View.VISIBLE);
        incrementCB.setVisibility(View.VISIBLE);
        if(incrementCB.isChecked())
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
        CharSequence player1name, player2name;
        player1name = player1.getText().toString();
        player2name = player2.getText().toString();

        setContentView(R.layout.activity_clock);

        TextView player1txt = (TextView) findViewById(R.id.player1_name);
        TextView player2txt = (TextView) findViewById(R.id.player2_name);
        player1txt.setText(player1name + " time:");
        player2txt.setText(player2name + " time:");
        for (int i = 0; i < 3; i++) {
            p1time[i] = p1[i];
            if (sameTime)
                p2time[i] = p1[i];
            else
                p2time[i] = p2[i];
        }
        if(gameType==0) {
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
            }
        }
        else if(gameType==1){
            delayed=false;
            incremented=false;
            for(int i=0;i<3;i++){
                p2[i]= p2time[i]; //use p2 as value memory no matter if same or different time for both players
            }
        }
        TextView player1time = (TextView) findViewById(R.id.player1_time);
        TextView player2time = (TextView) findViewById(R.id.player2_time);
        if (p1time[2] > 10)
            player1time.setText(p1time[0] + ":" + p1time[1] + ":" + p1time[2]);
        else
            player1time.setText(Clock.p1time[0] + ":" + Clock.p1time[1] + ":0" + Clock.p1time[2]);
        if (p2time[2] > 10)
            player2time.setText(p2time[0] + ":" + p2time[1] + ":" + p2time[2]);
        else
            player2time.setText(Clock.p2time[0] + ":" + Clock.p2time[1] + ":0" + Clock.p2time[2]);

        gameOn = true;
    }

    public void player1TimerStart() {
        p1timer = new CountDownTimer((p1time[0] * 3600 + p1time[1] * 60 + p1time[2]) * 1000, 1000) {
            TextView player1time = (TextView) findViewById(R.id.player1_time);

            public void onTick(long millisUntilFinished) {
                if (p1time[2] > 10) {

                    Clock.p1time[2]--;
                    player1time.setText(Clock.p1time[0] + ":" + Clock.p1time[1] + ":" + Clock.p1time[2]);

                } else if (Clock.p1time[2] > 0) {

                    Clock.p1time[2]--;
                    player1time.setText(Clock.p1time[0] + ":" + Clock.p1time[1] + ":0" + Clock.p1time[2]);

                } else {
                    if (Clock.p1time[1] > 0) {

                        Clock.p1time[1]--;
                        Clock.p1time[2] = 59;
                        player1time.setText(Clock.p1time[0] + ":" + Clock.p1time[1] + ":" + Clock.p1time[2]);

                    } else if (p1time[0] > 0) {

                        Clock.p1time[0]--;
                        Clock.p1time[1] = 59;
                        player1time.setText(Clock.p1time[0] + ":" + Clock.p1time[1] + ":" + Clock.p1time[2]);
                    }

                }
            }

            public void onFinish() {
                player1time.setText("Time's up!");
                Clock.gameOn = false;

            }
        }.start();

    }

    public void player2TimerStart() {
        p2timer = new CountDownTimer((p2time[0] * 3600 + p2time[1] * 60 + p2time[2]) * 1000, 1000) {
            TextView player2time = (TextView) findViewById(R.id.player2_time);

            public void onTick(long millisUntilFinished) {
                if (p2time[2] > 10) {

                    Clock.p2time[2]--;
                    player2time.setText(Clock.p2time[0] + ":" + Clock.p2time[1] + ":" + Clock.p2time[2]);

                } else if (Clock.p2time[2] > 0) {

                    Clock.p2time[2]--;
                    player2time.setText(Clock.p2time[0] + ":" + Clock.p2time[1] + ":0" + Clock.p2time[2]);

                } else {
                    if (Clock.p2time[1] > 0) {

                        Clock.p2time[1]--;
                        Clock.p2time[2] = 59;
                        player2time.setText(Clock.p2time[0] + ":" + Clock.p2time[1] + ":" + Clock.p2time[2]);

                    } else if (p2time[0] > 0) {

                        Clock.p2time[0]--;
                        Clock.p2time[1] = 59;
                        player2time.setText(Clock.p2time[0] + ":" + Clock.p2time[1] + ":" + Clock.p2time[2]);
                    }

                }
            }

            public void onFinish() {
                player2time.setText("Time's up!");
                Clock.gameOn = false;

            }
        }.start();

    }


    protected void startDelay() {
        delayOn=true;
        if(player==2) {
            delaytimer = new CountDownTimer((delay[0] * 3600 + delay[1] * 60 + delay[2]) * 1000, 1000) {
                TextView delayTime = (TextView) findViewById(R.id.delay_time1);
                int time[] = {Clock.delay[0], Clock.delay[1], Clock.delay[2]};

                public void onTick(long millisUntilFinished) {
                    if (time[2] > 10) {

                        time[2]--;
                        delayTime.setText("Delay:" + time[0] + ":" + time[1] + ":" + time[2]);

                    } else if (time[2] > 0) {

                        time[2]--;
                        delayTime.setText("Delay:" + time[0] + ":" + time[1] + ":0" + time[2]);

                    } else {
                        if (time[1] > 0) {

                            time[1]--;
                            time[2] = 59;
                            delayTime.setText("Delay:" + time[0] + ":" + time[1] + ":" + time[2]);

                        } else if (time[0] > 0) {

                            time[0]--;
                            time[1] = 59;
                            delayTime.setText("Delay:" + time[0] + ":" + time[1] + ":" + time[2]);
                        }

                    }

                }

                public void onFinish() {
                    delayTime.setText("Delay:" + time[0] + ":" + time[1] + ":" + "00");
                    Clock.delayOn = false;
                    player1TimerStart();
                }
            }.start();
        }
        if (player == 1) {
            delaytimer = new CountDownTimer((delay[0] * 3600 + delay[1] * 60 + delay[2]) * 1000, 1000) {
                TextView delayTime = (TextView) findViewById(R.id.delay_time2);
                int time[] = {Clock.delay[0], Clock.delay[1], Clock.delay[2]};

                public void onTick(long millisUntilFinished) {
                    if (time[2] > 10) {

                        time[2]--;
                        delayTime.setText("Delay:" + time[0] + ":" + time[1] + ":" + time[2]);

                    } else if (time[2] > 0) {

                        time[2]--;
                        delayTime.setText("Delay:" + time[0] + ":" + time[1] + ":0" + time[2]);

                    } else {
                        if (time[1] > 0) {

                            time[1]--;
                            time[2] = 59;
                            delayTime.setText("Delay:" + time[0] + ":" + time[1] + ":" + time[2]);

                        } else if (time[0] > 0) {

                            time[0]--;
                            time[1] = 59;
                            delayTime.setText("Delay:" + time[0] + ":" + time[1] + ":" + time[2]);
                        }

                    }

                }

                public void onFinish() {
                    delayTime.setText("Delay:" + time[0] + ":" + time[1] + ":" + "00");
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
            if(gameType==1){
                for(int i=0; i<3;i++){
                    p2time[i]=p2[i];
                }
            }
            if (p2timer != null && incremented && gameType==0) {
                for (int i = 0; i < 3; i++) {
                    p2time[i] += increment[i];
                    if(p2time[i]>60 && i>0){
                        p2time[i]-=60;
                        p2time[i-1]++;
                    }
                }

            }
            if(delayOn){
                delaytimer.cancel();
            }
            else if (p1timer != null)
                p1timer.cancel();
            if (delayed) {
                player=1;
                startDelay();
                player=2;
            } else if (p2timer == null || incremented || gameType==1) {
                player2TimerStart();
                player = 2;
            } else {
                p2timer.start();
                player = 2;
            }

        }
        // if(!gameOn){
        //TODO: add popup for reset timers on validation reset
        // }
    }
    /**
     * this function stops player2 counters adds tt2 amount of secs and starts player 1 counter
     *
     * @param view android view
     */
    public void player2(View view) {

        if ((player == 2 || player == 0) && gameOn) {
            if(gameType==1){
                for(int i=0; i<3;i++){
                    p1time[i]=p1[i];
                }
            }
            if (p1timer != null && incremented && gameType==0) {
                for (int i = 0; i < 3; i++) {
                    p1time[i] += increment[i];
                    if(p1time[i]>60 && i>0){
                        p1time[i]-=60;
                        p1time[i-1]++;
                    }
                }
            }
            if(delayOn){
                delaytimer.cancel();
            }
            else if (p2timer != null)
                p2timer.cancel();
            if (delayed) {
                player=2;
                startDelay();
                player=1;
            }
            else if (p1timer == null || incremented || gameType==1) {
                player1TimerStart();
                player = 1;
            } else {
                p1timer.start();
                player = 1;
            }

        }

     /*   if(!gameOn){
            //TODO: add popup for reset timers on validation reset
        }*/
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
                    delay_hour.setText("0" + delay[0]);
                    break;
                }
                delay_hour.setText("" + delay[0]);
                break;

            case R.id.add_delay_min:
                TextView delay_min = (TextView) findViewById(R.id.delay_min);
                if (delay[1] < 59)
                    delay[1]++;
                else
                    delay[1] = 0;
                if (delay[1] < 10) {
                    delay_min.setText("0" + delay[1]);
                    break;
                }
                delay_min.setText("" + delay[1]);
                break;

            case R.id.add_delay_sec:
                TextView delay_sec = (TextView) findViewById(R.id.delay_sec);
                if (delay[2] < 59)
                    delay[2]++;
                else
                    delay[2] = 0;
                if (delay[2] < 10) {
                    delay_sec.setText("0" + delay[2]);
                    break;
                }
                delay_sec.setText("" + delay[2]);
                break;


            case R.id.add_increment_hour:
                TextView increment_hour = (TextView) findViewById(R.id.increment_hour);
                if (increment[0] < 12)
                    increment[0]++;
                else
                    increment[0] = 0;
                if (increment[0] < 10) {
                    increment_hour.setText("0" + increment[0]);
                    break;
                }
                increment_hour.setText("" + increment[0]);
                break;

            case R.id.add_increment_min:
                TextView increment_min = (TextView) findViewById(R.id.increment_min);
                if (increment[1] < 59)
                    increment[1]++;
                else
                    increment[1] = 0;
                if (increment[1] < 10) {
                    increment_min.setText("0" + increment[1]);
                    break;
                }
                increment_min.setText("" + increment[1]);
                break;

            case R.id.add_increment_sec:
                TextView increment_sec = (TextView) findViewById(R.id.increment_sec);
                if (increment[2] < 59)
                    increment[2]++;
                else
                    increment[2] = 0;
                if (increment[2] < 10) {
                    increment_sec.setText("0" + increment[2]);
                    break;
                }
                increment_sec.setText("" + increment[2]);
                break;


            case R.id.add_p1_hour:
                TextView p1_hour = (TextView) findViewById(R.id.p1_hour);
                if (p1[0] < 12)
                    p1[0]++;
                else
                    p1[0] = 0;
                if (p1[0] < 10) {
                    p1_hour.setText("0" + p1[0]);
                    break;
                }
                p1_hour.setText("" + p1[0]);
                break;

            case R.id.add_p1_min:
                TextView p1_min = (TextView) findViewById(R.id.p1_min);
                if (p1[1] < 59)
                    p1[1]++;
                else
                    p1[1] = 0;
                if (p1[1] < 10) {
                    p1_min.setText("0" + p1[1]);
                    break;
                }
                p1_min.setText("" + p1[1]);
                break;

            case R.id.add_p1_sec:
                TextView p1_sec = (TextView) findViewById(R.id.p1_sec);
                if (p1[2] < 59)
                    p1[2]++;
                else
                    p1[2] = 0;
                if (p1[2] < 10) {
                    p1_sec.setText("0" + p1[2]);
                    break;
                }
                p1_sec.setText("" + p1[2]);
                break;


            case R.id.add_p2_hour:
                TextView p2_hour = (TextView) findViewById(R.id.p2_hour);
                if (p2[0] < 12)
                    p2[0]++;
                else
                    p2[0] = 0;
                if (p2[0] < 10) {
                    p2_hour.setText("0" + p2[0]);
                    break;
                }
                p2_hour.setText("" + p2[0]);
                break;

            case R.id.add_p2_min:
                TextView p2_min = (TextView) findViewById(R.id.p2_min);
                if (p2[1] < 59)
                    p2[1]++;
                else
                    p2[1] = 0;
                if (p2[1] < 10) {
                    p2_min.setText("0" + p2[1]);
                    break;
                }
                p2_min.setText("" + p2[1]);
                break;

            case R.id.add_p2_sec:
                TextView p2_sec = (TextView) findViewById(R.id.p2_sec);
                if (p2[2] < 59)
                    p2[2]++;
                else
                    p2[2] = 0;
                if (p2[2] < 10) {
                    p2_sec.setText("0" + p2[2]);
                    break;
                }
                p2_sec.setText("" + p2[2]);
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
                    delay_hour.setText("0" + delay[0]);
                    break;
                }
                delay_hour.setText("" + delay[0]);
                break;

            case R.id.rem_delay_min:
                TextView delay_min = (TextView) findViewById(R.id.delay_min);
                if (delay[1] > 0)
                    delay[1]--;
                else
                    delay[1] = 59;
                if (delay[1] < 10) {
                    delay_min.setText("0" + delay[1]);
                    break;
                }
                delay_min.setText("" + delay[1]);
                break;

            case R.id.rem_delay_sec:
                TextView delay_sec = (TextView) findViewById(R.id.delay_sec);
                if (delay[2] > 0)
                    delay[2]--;
                else
                    delay[2] = 59;
                if (delay[2] < 10) {
                    delay_sec.setText("0" + delay[2]);
                    break;
                }
                delay_sec.setText("" + delay[2]);
                break;


            case R.id.rem_increment_hour:
                TextView increment_hour = (TextView) findViewById(R.id.increment_hour);
                if (increment[0] > 0)
                    increment[0]--;
                else
                    increment[0] = 12;
                if (increment[0] < 10) {
                    increment_hour.setText("0" + increment[0]);
                    break;
                }
                increment_hour.setText("" + increment[0]);
                break;

            case R.id.rem_increment_min:
                TextView increment_min = (TextView) findViewById(R.id.increment_min);
                if (increment[1] > 0)
                    increment[1]--;
                else
                    increment[1] = 59;
                if (increment[1] < 10) {
                    increment_min.setText("0" + increment[1]);
                    break;
                }
                increment_min.setText("" + increment[1]);
                break;

            case R.id.rem_increment_sec:
                TextView increment_sec = (TextView) findViewById(R.id.increment_sec);
                if (increment[2] > 0)
                    increment[2]--;
                else
                    increment[2] = 59;
                if (increment[2] < 10) {
                    increment_sec.setText("0" + increment[2]);
                    break;
                }
                increment_sec.setText("" + increment[2]);
                break;


            case R.id.rem_p1_hour:
                TextView p1_hour = (TextView) findViewById(R.id.p1_hour);
                if (p1[0] > 0)
                    p1[0]--;
                else
                    p1[0] = 12;
                if (p1[0] < 10) {
                    p1_hour.setText("0" + p1[0]);
                    break;
                }
                p1_hour.setText("" + p1[0]);
                break;

            case R.id.rem_p1_min:
                TextView p1_min = (TextView) findViewById(R.id.p1_min);
                if (p1[1] > 0)
                    p1[1]--;
                else
                    p1[1] = 59;
                if (p1[1] < 10) {
                    p1_min.setText("0" + p1[1]);
                    break;
                }
                p1_min.setText("" + p1[1]);
                break;

            case R.id.rem_p1_sec:
                TextView p1_sec = (TextView) findViewById(R.id.p1_sec);
                if (p1[2] > 0)
                    p1[2]--;
                else
                    p1[2] = 59;
                if (p1[2] < 10) {
                    p1_sec.setText("0" + p1[2]);
                    break;
                }
                p1_sec.setText("" + p1[2]);
                break;


            case R.id.rem_p2_hour:
                TextView p2_hour = (TextView) findViewById(R.id.p2_hour);
                if (p2[0] > 0)
                    p2[0]--;
                else
                    p2[0] = 12;
                if (p2[0] < 10) {
                    p2_hour.setText("0" + p2[0]);
                    break;
                }
                p2_hour.setText("" + p2[0]);
                break;

            case R.id.rem_p2_min:
                TextView p2_min = (TextView) findViewById(R.id.p2_min);
                if (p2[1] > 0)
                    p2[1]--;
                else
                    p2[1] = 59;
                if (p2[1] < 10) {
                    p2_min.setText("0" + p2[1]);
                    break;
                }
                p2_min.setText("" + p2[1]);
                break;

            case R.id.rem_p2_sec:
                TextView p2_sec = (TextView) findViewById(R.id.p2_sec);
                if (p2[2] > 0)
                    p2[2]--;
                else
                    p2[2] = 59;
                if (p2[2] < 10) {
                    p2_sec.setText("0" + p2[2]);
                    break;
                }
                p2_sec.setText("" + p2[2]);
                break;

        }
    }

    /**
     * logic behind incremented check box
     *
     * @param view checkbox view
     */
    public void isIncremented(View view) {
        CheckBox incrView = (CheckBox) findViewById(R.id.increment_time_CB);
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
        CheckBox incrView = (CheckBox) findViewById(R.id.delay_time_CB);
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
        CheckBox incrView = (CheckBox) findViewById(R.id.same_time_CB);
        LinearLayout layout = (LinearLayout) findViewById(R.id.player2_time_layout);
        TextView textP1 = (TextView) findViewById(R.id.player1_time_label);
        TextView textP2 = (TextView) findViewById(R.id.player2_time_label);
        if (incrView.isChecked()) {
            sameTime = true;
            layout.setVisibility(View.GONE);
            textP1.setText("Timer per player:");
            textP2.setVisibility(View.GONE);
        } else {
            sameTime = false;
            layout.setVisibility(View.VISIBLE);
            textP1.setText("Player 1 time:");
            textP2.setVisibility(View.VISIBLE);
            layout.invalidate();
        }

    }
}


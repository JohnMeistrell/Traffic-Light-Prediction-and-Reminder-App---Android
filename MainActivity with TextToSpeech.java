package com.example.johnmeistrell.w10getgpsloc;

        import android.Manifest;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Criteria;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Environment;
        import android.provider.Settings;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.Editable;
        import android.util.Log;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.text.ParseException;
        import java.util.Calendar;
        import java.text.SimpleDateFormat;
        import android.text.format.DateFormat;
        import android.text.format.DateUtils;
        import java.sql.Time;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.Locale;
        import java.util.Objects;

        import android.speech.tts.TextToSpeech;
        import android.speech.tts.TextToSpeech.OnInitListener;

        import static android.os.Environment.*;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static final int RequestPermissionCode  = 1 ;
    Button buttonEnable, buttonGet ;

 // cntl shift + - collapes expand all

    TextView textViewLongitude, textViewLatitude, textViewSpeed;
    TextView textViewDirection, textViewCarLight;
    TextView textViewDLong, textViewDLat;
    TextView textViewHypot, textViewBearingToLight;

    String NearLight = "";

    EditText editTextInput1;

    Context context;
    Intent intent1 ;

    String sEditText = "";

    Location location;
    LocationManager locationManager ;

    boolean GpsStatus = false ;
    boolean HasSpeed = false ;

    String sRecType = "";

    float fspeed = 0 ;
    Criteria criteria ;
    String Holder;

    Double dRecNbr = 0.0;

    String sLightName = "";

    String sRecNbr = "";

    int iDirPrior = 0;  // Bearing before stop
    double dspeedLast = 0;
    double dspeedLastLog = 0;
    double fLatLast = 0;
    double fLongLast = 0;

    String sCarLight = "";

    String sLastOutRecPart2 = "";

    double dfeet = 0;

    double dspeed =0;

    double dDifLng = 0;
    double dDifLat = 0;

    String sdatetime = "";

    String sCompass = "x";
    String sCompassC = "x";
    double dAtLng = 0;
    double dAtLat = 0;
    double fLat = 0;
    double fLong =0;
    double tLat = 0;
    double tLong = 0;

    double dspeedSetEditText = 0;

    float fBearing = 0;

    double dDirRC = 0;

    int iDirR = 0;
    int iDirRC = 0;

    double dhypotenuse = 0;

    double dLightLng = 0;
    double dLightLat = 0;

    int iSpeedZeroCount = 0;
    int iiSpeedZeroCount = 0;

    int ifeetToLightLastLog = 0;

    TextToSpeech t1;

    Boolean bSaidApproachingStop = false;
    Boolean bSaidEnteredDebugText = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EnableRuntimePermission();

        buttonEnable = (Button)findViewById(R.id.button);
        buttonGet = (Button)findViewById(R.id.button2);

        textViewLongitude = (TextView)findViewById(R.id.textView);
        textViewLatitude = (TextView)findViewById(R.id.textView2);
        textViewSpeed = (TextView)findViewById(R.id.textView3);
        textViewDirection = (TextView)findViewById(R.id.textView4);
        textViewCarLight = (TextView)findViewById(R.id.textView5);
        textViewDLat = (TextView)findViewById(R.id.textView6);
        textViewDLong = (TextView)findViewById(R.id.textView7);
        textViewHypot = (TextView)findViewById(R.id.textView8);
        textViewBearingToLight = (TextView) findViewById(R.id.textView9);


        editTextInput1 = (EditText) findViewById(R.id.et_input1);


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        Holder = locationManager.getBestProvider(criteria, false);

        context = getApplicationContext();

        CheckGpsStatus();


        buttonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);


            }
        });


        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckGpsStatus();

                if (GpsStatus == true) {
                    if (Holder != null) {
                        if (ActivityCompat.checkSelfPermission(
                                MainActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                &&
                                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(Holder);
                        locationManager.requestLocationUpdates(Holder, 0, 0, MainActivity.this);
                    }
                } else {

                    Toast.makeText(MainActivity.this, "Please Enable GPS First", Toast.LENGTH_LONG).show();

                }

                // debug
                getEditInputValue();


            }
        });


        TryVariousFileIO();

        //sRecType = "TEST";

        // AppendToTrafficLogFile();

        TestNoGPS();

    }

    public void getEditInputValue() {

        //final Editable sEditText = editTextInput1.getText();
        //sEditText = editTextInput1.getText().toString();
        // if (Objects.equals(sEditText, "1")) {

        // Debug, supply GPS Location values
        editTextInput1 = (EditText) findViewById(R.id.et_input1);
        final Editable text11 = editTextInput1.getText();
        sEditText = text11.toString();
        if (text11.toString().equalsIgnoreCase("1")) {
            dspeedSetEditText = 10;
            bSaidEnteredDebugText = true;
        }
        else
        if (text11.toString().equalsIgnoreCase("2")) {
            dspeedSetEditText = 15;
            bSaidEnteredDebugText = true;
        }
        else
        {
            dspeedSetEditText = 0;

        }

        if (text11.toString().equalsIgnoreCase("")) {
            bSaidEnteredDebugText = false;
        }

        if (bSaidEnteredDebugText) {
            bSaidEnteredDebugText = true;
            String sSayIt = "Entered debug text.";
            String toSpeak = sSayIt.toString();
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

        }



    }

    public void allLocationGPSinfoDebug(){
        dAtLng = 0;
        dAtLat = 0;
        fspeed = 0;
        fBearing = 0;
    }

    public void allLocationGPSinfo(){

        dAtLng = location.getLongitude();
        dAtLat = location.getLatitude();
        fspeed = location.getSpeed();
        fBearing = location.getBearing();
    }

    @Override
    public void onLocationChanged(Location location) {
    //------------------------------------------------

        //allLocationGPSinfo();

       dAtLng = location.getLongitude();
        dAtLat = location.getLatitude();
        fspeed = location.getSpeed();
        fBearing = location.getBearing();
        processit();

    }

    public void processit(){


        // Time
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdatetime = df.format(c.getTime());

        // Car at-----------------------------------------------------------
        //dAtLng = location.getLongitude();
        //dAtLat = location.getLatitude();


        /**
         // Test 200 feet before west end
         dAtLng = -74.15452115;
         dAtLat = 39.95236206;

         // test bus yard
         dAtLng = -74.23070557;
         dAtLat = 39.90481731;
         **/



        // Speed ----------------------------------------------
        //fspeed = location.getSpeed();
        dspeed = Math.round(fspeed) * 2.2;

        if (dspeedSetEditText == 0) {
        }
        else {
            dspeed = dspeedSetEditText;
        }



        // Bearing heading N S E W ---------------------------------------------
        ///fBearing = location.getBearing();
        Long ll = Math.round((fBearing / 22.5));

        String wmyspeed = String.format("%.0f", dspeed);

        String wmyspeedLast = String.format("%.0f", dspeedLast);

        String myspeed = Double.toString(dspeed);
        String smyspeedlast = Double.toString(dspeedLast);

        Long lDirR = Math.round((fBearing / 45.0)); // 22.5
        iDirR = lDirR.intValue();

        String sDir = Float.toString(fBearing);

        int  iiMySpeed = Math.round(fspeed);
        if (iDirR == 0) {
            if (iiMySpeed > 0) {
                iDirR = 8;

            }
        }

        sCompassNSEW(iDirR);

        BearningCarToLight();

        // Loop thru all lights
        FindClosestLight();


        TimeToRedGreen();

        CalcDifTimeThisWorks();
        CalcDifTime2();

        // Count to show if parked. zero mph for a while.
        iiSpeedZeroCount ++;
        if (dspeed > 0) {
            iiSpeedZeroCount = 0;
        }

        // -------------------------------------------------------------
        // Check if there is a light by tracking speed changes. 
        // When speed changes 5mph indicating red green lite and not parked zero mph.
        // -------------------------------------------------------------

        double ddSpeedChange = dspeed - dspeedLastLog;

        String sddSpeedChange = String.format("%.0f", ddSpeedChange);
        String ssdspeed = String.format("%.0f", dspeed);
        String ssdspeedLastLog = String.format("%.0f", dspeedLastLog);

        textViewHypot.setText("Chg: " + sddSpeedChange + " C-L:" + ssdspeed + "-" + ssdspeedLastLog);

        if (dspeed < 20) {

            if ( dspeed == 0) {
                if ( iiSpeedZeroCount < 50) {
                    sRecType = "STOP";
                    AppendToTrafficLogFile();
                }
            }

            if ( ddSpeedChange > 5) {
                sRecType = "INCR";
                AppendToTrafficLogFile();
            }
            if (ddSpeedChange < -5) {
                sRecType = "DECR";
                AppendToTrafficLogFile();
            }

        }
        // -------------------------------------------------------------

        // http://docs.appcelerator.com/platform/latest/#!/guide/Debugging_in_Studio
        ///https://www.mkyong.com/android/android-prompt-user-input-dialog-example/



        textViewLongitude.setText("Lng:" + dAtLng + " Wrote#:" + sRecNbr + " " +sRecType );
        textViewLatitude.setText("Lat:" + dAtLat + " EText:" + sEditText);
        textViewSpeed.setText("Speed:" + wmyspeed + " Last:" + wmyspeedLast);
        textViewDirection.setText("Car bearing: " + sCompass + " Before stop: " + sCompassC + " " + iDirPrior);

        PriorBearningSpeedEtc();

        CalcTimeDuration();

    }


    //public void {
    //------------------------------------------------------------------------------------------------
    //}

    public void sCompassNSEW(int iDirR) {
    //------------------------------------------------------------------------------------------------

        // https://www.javaworld.com/article/2077424/learn-java/does-java-pass-by-reference-or-pass-by-value.html
        // https://stackoverflow.com/questions/9411902/pass-by-reference-in-java

        sCompass = "";

        if (iDirR == 1) {
            sCompass = "ne";
        }
        ;
        if (iDirR == 2) {
            sCompass = "E";
        }
        ;
        if (iDirR == 3) {
            sCompass = "se";
        }
        ;
        if (iDirR == 4) {
            sCompass = "S";
        }
        ;
        if (iDirR == 5) {
            sCompass = "sw";
        }
        ;
        if (iDirR == 6) {
            sCompass = "W";
        }
        ;
        if (iDirR == 7) {
            sCompass = "nw";
        }
        ;
        if (iDirR == 8) {
            sCompass = "N";
        }
        ;

    }

    public void BearningCarToLight() {
    //------------------------------------------------------------------------------------------------


        // Bearning two points ----------------------------------

        /**
        float dDifLng1 = Math.tofloat(dAtLng-dLightLng);
        float dDifLat1 = dAtLat-dLightLat;
        float possibleAzimuth = (Math.PI * .5f) - Math.atan(dDifLat1 / dDifLng1);
        **/

        fLat = degreeToRadians(dAtLat);
        fLong = degreeToRadians(dAtLng);
        tLat = degreeToRadians(dLightLat);
        tLong = degreeToRadians(dLightLng);

        double dLon = (tLong - fLong);

        double degree = radiansToDegree(Math.atan2(Math.sin(dLon) * Math.cos(tLat),
                Math.cos(fLat) * Math.sin(tLat) - Math.sin(fLat) * Math.cos(tLat) * Math.cos(dLon)));

        double dDegreeReturnd = 0;
        if (degree >= 0) {
            dDegreeReturnd = degree;
        } else {
            dDegreeReturnd = degree + 360;
        }
        ;

        dDirRC = Math.round((dDegreeReturnd / 45.0)); // 22.5

        CompassNSEW();

        String sDegreeReturnd = String.format("%.0f", dDegreeReturnd);
        textViewBearingToLight.setText(sCompassC + " " + sDegreeReturnd  + "to: " + sLightName );

    }

    public void CompassNSEW() {
    //------------------------------------------------------------------------------------------------

        iDirRC = (int) Math.round(dDirRC);

        sCompassC = "";
        if (iDirRC == 0) {
            iDirRC = 8;
        }
        ;

        if (iDirRC == 1) {
            sCompassC = "ne";
        }
        ;
        if (iDirRC == 2) {
            sCompassC = "E";
        }
        ;
        if (iDirRC == 3) {
            sCompassC = "se";
        }
        ;
        if (iDirRC == 4) {
            sCompassC = "S";
        }
        ;
        if (iDirRC == 5) {
            sCompassC = "sw";
        }
        ;
        if (iDirRC == 6) {
            sCompassC = "W";
        }
        ;
        if (iDirRC == 7) {
            sCompassC = "nw";
        }
        ;
        if (iDirRC == 8) {
            sCompassC = "N";
        }
        ;

    }

    public void FindClosestLight() {

        double dfeetClosest = 99999 * 99999;
        String slightClosest = "";

        sLightName = "";

       //------------------------------------------------------------------ 
       // Light at
       //------------------------------------------------------------------ 
        dLightLat = 39.96339221598682;
        dLightLng = -74.20059504855163 ;
        sLightName = "r37 main";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.96328202534696 ;
        dLightLng = 74.18746123656166 ;
        sLightName = "r37 hooper";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.96039469136955 ;
        dLightLng = 74.17720453589948 ;
        sLightName = "r37 clifton";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.95739114774732 ;
        dLightLng = 74.17006321126155 ;
        sLightName = "r37 batchler";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.95417803547045 ;
        dLightLng = 74.16234320111692 ;
        sLightName = "r37 washington";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.952177861790744;
        dLightLng = 74.15314773028734 ;
        sLightName = "r37 west end";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.95173042290854 ;
        dLightLng = 74.14973292557056 ;
        sLightName = "r37 garfield";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.95077104999166 ;
        dLightLng = 74.13949575642647 ;
        sLightName = "r37 coolage";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.95029790860171 ;
        dLightLng = 74.13117544795193 ;
        sLightName = "r37 garfield";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.949968789560856;
        dLightLng = 74.12423681543635 ;
        sLightName = "fisher";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.949321115220016;
        dLightLng = 74.11508743315733 ;
        sLightName = "bridge";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.94281343640306 ;
        dLightLng = 74.0893269145717  ;
        sLightName = "catalina";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.940044245478525;
        dLightLng = 74.07559551762301 ;
        sLightName = "central hamplton";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.9414146794933  ;
        dLightLng = 74.0753684954127  ;
        sLightName = "central summer";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.94279166654124 ;
        dLightLng = 74.0751298860398  ;
        sLightName = "central grant";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.94266663655519 ;
        dLightLng = 74.07345532928798 ;
        sLightName = "bulavard grant";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.94124109232596 ;
        dLightLng = 74.07371196302688 ;
        sLightName = "bulavard summer";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.897983329753956;
        dLightLng = 74.22110080718994 ;
        sLightName = "double trouble";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.89221478005739 ;
        dLightLng = 74.21118392936478 ;
        sLightName = "central parkway";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.89280473343495 ;
        dLightLng = 74.2057488236851  ;
        sLightName = "school";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }

        dLightLat =39.89087746373868 ;
        dLightLng = 74.19163364756207 ;
        sLightName = "western veterines";
        ToLightLatLongDistance();
        CloseToThisLight();
        NearLight();
        if (dfeet < dfeetClosest) {
            dfeetClosest = dfeet;
            slightClosest = sLightName;
        }


      //------------------------------------------------------------------ 
      // Show the closest light
      //------------------------------------------------------------------ 
        sLightName = "";

        if ( slightClosest == "r37 main" ) {

            dLightLat = 39.96339221598682;
            dLightLng = -74.20059504855163 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();

        }
        if ( slightClosest == "r37 hooper" ) {
            dLightLat =39.96328202534696 ;
            dLightLng = 74.18746123656166 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();

        }
        if ( slightClosest == "r37 clifton" ) {
            dLightLat =39.96039469136955 ;
            dLightLng = 74.17720453589948 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "r37 batchler" ) {
            dLightLat =39.95739114774732 ;
            dLightLng = 74.17006321126155 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "r37 washington" ) {
            dLightLat =39.95417803547045 ;
            dLightLng = 74.16234320111692 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "r37 west end" ) {
            dLightLat =39.952177861790744;
            dLightLng = 74.15314773028734 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "r37 garfield" ) {
            dLightLat =39.95173042290854 ;
            dLightLng = 74.14973292557056 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();


            if (bSaidApproachingStop) {

                bSaidApproachingStop = true;
                String sSayIt = "Approching stop.";
                String toSpeak = sSayIt.toString();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

            }




        }
        if ( slightClosest == "r37 coolage" ) {
            dLightLat =39.95077104999166 ;
            dLightLng = 74.13949575642647;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "r37 garfield" ) {
            dLightLat =39.95029790860171 ;
            dLightLng = 74.13117544795193 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "fisher" ) {
            dLightLat =39.949968789560856;
            dLightLng = 74.12423681543635 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "bridge" ) {
            dLightLat =39.949321115220016;
            dLightLng = 74.11508743315733 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "catalina" ) {
            dLightLat =39.94281343640306 ;
            dLightLng = 74.0893269145717  ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "central hamplton" ) {
            dLightLat =39.940044245478525;
            dLightLng = 74.07559551762301 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "central summer" ) {
            dLightLat =39.9414146794933  ;
            dLightLng = 74.0753684954127  ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "central grant" ) {
            dLightLat =39.94279166654124 ;
            dLightLng = 74.0751298860398  ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "bulavard grant" ) {
            dLightLat =39.94266663655519 ;
            dLightLng = 74.07345532928798 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "bulavard summer" ) {
            dLightLat =39.94124109232596 ;
            dLightLng = 74.07371196302688 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "double trouble" ) {
            dLightLat =39.897983329753956;
            dLightLng = 74.22110080718994 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "central parkway" ) {
            dLightLat =39.89221478005739 ;
            dLightLng = 74.21118392936478 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "school" ) {
            dLightLat =39.89280473343495 ;
            dLightLng = 74.2057488236851  ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }
        if ( slightClosest == "western veterines" ) {
            dLightLat =39.89087746373868 ;
            dLightLng = 74.19163364756207 ;
            sLightName = slightClosest;
            ToLightLatLongDistance();
            CloseToThisLight();
            NearLight();
        }

      //------------------------------------------------------------------ 



    }


    public void ToLightLatLongDistance() {
    //------------------------------------------------------------------------------------------------

        // Car Light Dif Lat Long --------------------------------------------
        dDifLng = (Math.abs(dAtLng) - (Math.abs(dLightLng)));
        dDifLat = (Math.abs(dAtLat) - (Math.abs(dLightLat)));

        String sDifLng = String.format("%.10f", dDifLng);
        String sDifLat = String.format("%.10f", dDifLat);

        textViewDLong.setText("NS:" + sDifLng);
        textViewDLat.setText("EW:" + sDifLat);

    }

    public void CloseToThisLight() {
    //------------------------------------------------------------------------------------------------

        // Distance car to light
        dDifLat = (Math.abs(dAtLat) - (Math.abs(dLightLat)));
        dDifLng = (Math.abs(dAtLng) - (Math.abs(dLightLng)));
        dhypotenuse = Math.sqrt((dDifLng * dDifLng) + (dDifLat * dDifLat));
        // double dfeet = dhypotenuse*0.000274484;
        // textViewHypot.setText("Hypot open:");
    }

    public void NearLight() {
    //------------------------------------------------------------------------------------------------

        double dMilesInLadLng = 69;
        double dFeetInMile = 5280;
        dfeet = dhypotenuse * dMilesInLadLng * dFeetInMile;

        sCarLight = Double.toString(dfeet);
        sCarLight = String.format("%.02f", dfeet) + "feet";
        NearLight = "";

        // Count to show if parked. zero mph for a while.
        iSpeedZeroCount ++;
        if (dspeed > 0) {
            iSpeedZeroCount = 0;
        }

        if ((dfeet > 0) & (dfeet < 500)) {

            sRecType = "NEAR";
            NearLight = sLightName;
            AppendToTrafficLogFile();
        }

        double dmiles = 0;
        if (dfeet > 1000) {
            if (dfeet == 0) {
                dmiles = 0;
            }
            else {
                dmiles = dfeet / dFeetInMile;
                sCarLight = String.format("%.02f", dmiles) + "miles";

            }


        }

        textViewCarLight.setText("Near:" + sCarLight + ":" + NearLight);


    }

    public void TimeToRedGreen() {
    //------------------------------------------------------------------------------------------------


       //* Time to RED or GREEN -------------------------------------------------

        /**
         N = (T-R) / D remainder off by

         R reset light time
         D duration red to red

         N number cycles
         T current time
         **/

        //nn double = 0.0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");


        String string1 = "05:00:00 PM";
        Date time1 = null;
        try {
            time1 = new SimpleDateFormat("HH:mm:ss aa").parse(string1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(time1);
        String string2 = "09:00:00 AM";
        Date time2 = null;
        try {
            time2 = new SimpleDateFormat("HH:mm:ss aa").parse(string2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(time2);
        calendar2.add(Calendar.DATE, 1);

        Date x = calendar1.getTime();
        Date xy = calendar2.getTime();

        long diff = x.getTime() - xy.getTime();

        long diffMinutes = diff / (60 * 1000);
        float diffHours = diffMinutes / 60;
        System.out.println("diff hours" + diffHours);

    }

    public void CalcDifTimeThisWorks() {
    //------------------------------------------------------------------------------------------------

        //--------------------------- this works

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse("22:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date endDate = null;
        try {
            endDate = simpleDateFormat.parse("07:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = endDate.getTime() - startDate.getTime();

        if (difference < 0) {

            Date dateMax = null;
            try {
                dateMax = simpleDateFormat.parse("24:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date dateMin = null;
            try {
                dateMin = simpleDateFormat.parse("00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime())
            ;
        }

        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        Log.i("log_tag", "Hours: " + hours + ", Mins: " + min);


    }

    public void CalcDifTime2() {
    //------------------------------------------------------------------------------------------------

        Date date2 = new Date();
        Date date1 = new Date();

        String dd = "7:02:48 AM";
        try {
            Date Time1 = new SimpleDateFormat("HH:mm:ss aa").parse(dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difff = date2.getTime() - date1.getTime();
        //rr double = 0.0;
        //dd double = 0.0;

        Date d = new Date();
        Calendar cal = Calendar.getInstance();
        Date ddd = new Date();

        //long hours = Time.parse(08:00:00"");
        //long hhh = ChronoUnit.HOURS.;

        String s2 = "09:01:01 AM";
        try {
            Date time22 = new SimpleDateFormat("HH:mm:ss aa").parse(s2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormatx = new SimpleDateFormat(pattern);

        try {
            Date date = simpleDateFormatx.parse("2012-12-24");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String pattern2 = "HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern2);

        try {
            Date date = simpleDateFormat.parse("2012-12-24");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void PriorBearningSpeedEtc() {
    //------------------------------------------------------------------------------------------------

        //Prior Bearning

        if (iDirPrior == iDirR) {
        } else {
         iDirPrior = iDirR;
        }
        if (dspeedLast == dspeed) {

        }
        else {
            dspeedLast = dspeed;
        }

        fLatLast = fLat;
        fLongLast = fLong;

    }

    public void CalcTimeDuration() {
    //------------------------------------------------------------------------------------------------

 
        /**
        java.sql.Time  curnentTime = Calendar.getInstance().getTime();

        Time now = new Time();
        now.setToNow();
        stime = Long.toString(now.toMillis(false)));

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        Time currentTime = new Time();

        **/

        //* currentTime.setToNow();


        //* Time to RED or GREEN

        /**
         N = (T-R) / D remainder off by

         R reset light time
         D duration red to red

         N number cycles
         T current time
         **/

         /**
         String NearLight = "";
         if ( (39.952147 -location.getLongitude() < 230 ) & ( location.getLatitude() - 74.153187 < 1502 ) ) {
            if ((39.952147 - location.getLongitude() > 0) & (location.getLatitude() - 74.153187 > 0)) {
                NearLight = "West End";
            };
         };
         textViewDirection.setText("Dir:" + myBearing + "NSEW:" + MyC + "At:" + NearLight + "end");
         **/
         
         // if ((lAtBearingInt % 2) == 0) {
         // number is even

    //------------------------------------------------------------------------------------------------
    }

    private static double degreeToRadians(double latLong) {
    return (Math.PI * latLong / 180.0);
    }

    private static double radiansToDegree(double latLong) {
    return (latLong * 180.0 / Math.PI);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    public void AppendToTrafficLogFile() {
        //-------------------------------------


        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);


        int imyspeed = (int) Math.round(dspeed);
        int imyspeedLast = (int) Math.round(dspeedLast);

        String smyspeed = Integer.toString(imyspeed);
        String smyspeedlast = Integer.toString(imyspeedLast);

        String sWriteLog = "Y";

        String sfeetToLight = "";

        int ifeetToLight = (int) Math.round(dfeet);
        if (ifeetToLight < 3000) {
            sfeetToLight = Integer.toString(ifeetToLight) + "Feet";
        }

        if (sRecType.toString().equalsIgnoreCase(("NEAR"))){
            if (ifeetToLight > 0) {
                if (ifeetToLightLastLog > ifeetToLight) {
                    sWriteLog = "N";
                }
            }
        }


        String sAtLng = Double.toString(dAtLat);
        String sAtLong= Double.toString(dAtLng);


        File bobFF = new File(path, "AAA.txt");


        // Create file output stream
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(bobFF,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //if (text11.toString().equalsIgnoreCase("1")) {

        String sOutRecPart2 = sRecType + ":," + smyspeed + ",mph," + smyspeedlast + ",last," ;
        String sOutRecPart3 = sCompass + ",dir," + sCompassC + ",dirC," + sfeetToLight + "," + sAtLng + "," + sAtLong + "," + NearLight ;


        if (sLastOutRecPart2.toString().equalsIgnoreCase(sOutRecPart2.toString())) {
                sWriteLog = "N";
        }

        if (sWriteLog == "Y") {

        // --------------------------------------------------------------------
        // Write a line to the file
        // --------------------------------------------------------------------
 
           //-------------------
           try {
               dRecNbr ++ ;
               sRecNbr = Double.toString(dRecNbr);
 
               String sOutRec = sRecNbr + "," + sdatetime + "," + sOutRecPart2 + "," + sOutRecPart3 + "\n\r";
   
               fos.write(sOutRec.getBytes());
           } catch (IOException e) {
               e.printStackTrace();
           }
           //-------------------
           String newline="\r\n";  // new line
           try {
               fos.write(newline.getBytes());
           } catch (IOException e) {
               e.printStackTrace();
           }
            // ----------------------

            dspeedLastLog = dspeed;
            ifeetToLightLastLog = ifeetToLight;
            sLastOutRecPart2 = sOutRecPart2;

        }
 
       // \n corresponds to ASCII char 0xA, which is 'LF' or line feed
       // \r corresponds to ASCII char 0xD, which is 'CR' or carriage return



        //String newline="\r\n";
            // Write a line to the file
        //try {
        //    fos.write(newline.getBytes());
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}





//        byte [] b = new byte[content.length];
  //      b= content.getBytes();
    //    streamOut.write(b);

        // Close the file output stream
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void CheckGpsStatus(){
    //---------------------------
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {

            Toast.makeText(MainActivity.this,"ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access GPS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access GPS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            final boolean b = imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void TestNoGPS() {
    //-----------------------

        /** ------- to test in library

        // Light at --------------------------------------------------------
        sLightName= "R37 & West End";
        double dLightLng= -74.1532722477859 ;
        double dLightLat=39.9520694750104;

        // Car at-----------------------------------------------------------
        double dAtLng= 0;
        double dAtLat= 0;

        // Test 200 feet before west end
        dAtLng = -74.15452115;
        dAtLat = 39.95236206;

        // test bus yard
        dAtLng = -74.23070557;
        dAtLat = 39.90481731;


        // Car Light Dif Lat Long --------------------------------------------
        double dDifLng = 0;
        double dDifLat = 0;
        dDifLng = (Math.abs(dAtLng)-(Math.abs(dLightLng)));
        dDifLat = (Math.abs(dAtLat)-(Math.abs(dLightLat)));
        textViewDLong.setText("NS:" + dDifLng);
        textViewDLat.setText("EW:" + dDifLat);

        // Distance car to light
        dDifLat = (Math.abs(dAtLat)-(Math.abs(dLightLat)));
        dDifLng = (Math.abs(dAtLng)-(Math.abs(dLightLng)));
        double dhypotenuse = Math.sqrt((dDifLng*dDifLng)+(dDifLat*dDifLat));
        double dMilesInLadLng = 69;
        double dFeetInMile = 5280;
        double dfeet = dhypotenuse*dMilesInLadLng*dFeetInMile;

        String sCarLight = Double.toString(dfeet);
        String NearLight = "";
        if ( (dfeet > 0) & (dfeet < 500) ) { NearLight = "West End"; };
        double xxx2 = 0;
    **/
    }

    public void TryVariousFileIO() {

        //** -------------------------------------------
        //** write file

        File internalStorageDir = getFilesDir();

        File alice = new File(internalStorageDir, "alice.csv");

        // Create file output stream
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(alice,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Write a line to the file
        try {
            fos.write("Alice,25,1".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
 
       // Close the file output stream
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //**-------------
        // append
        File file = new File(internalStorageDir,"Hello.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(file,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.write("more");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        //**--------------------------
        // file card

        if(getExternalStorageState()
                .equals(MEDIA_MOUNTED)) {
            // External storage is usable
        } else {
            // External storage is not usable
            // Try again later
        }


        File bob = new File(getExternalFilesDir(null), "bob.txt");

        File bobInPictures = new File(
                getExternalStoragePublicDirectory(
                        DIRECTORY_PICTURES),
                "bob.txt"
        );


 
        File bobDL = new File(
                getExternalStoragePublicDirectory(
                        DIRECTORY_DCIM),
                "bobD.txt"


        );


    }

}
// https://www.gps-coordinates.net/
// 39.96339221598682,-74.20059504855163 r37 main
// 39.96328202534696,-74.18746123656166 r37 hooper
// 39.96039469136955,-74.17720453589948 r37 clifton
// 39.95739114774732,-74.17006321126155 r37 peter batchler
// 39.95417803547045,-74.16234320111692 r37 washington
// 39.952177861790744,-74.15314773028734 r37 west end
// 39.95173042290854,-74.14973292557056 r37 garfield
// 39.95077104999166,-74.13949575642647 r37 coolage
// 39.95029790860171,-74.13117544795193 r37 garfield
// 39.949968789560856,-74.12423681543635 fisher
// 39.949321115220016,-74.11508743315733 bridge
// 39.94281343640306,-74.0893269145717 catalina
// 39.940044245478525,-74.07559551762301 central hamplton
// 39.9414146794933,-74.0753684954127 summer
// 39.94279166654124,-74.0751298860398 grant
// 39.94266663655519,-74.07345532928798 bulavard grant
// 39.94124109232596,-74.07371196302688 summer
// 39.897983329753956,-74.22110080718994 double trouble
// 39.89221478005739,-74.21118392936478 central parkway entrance
// 39.89280473343495,-74.2057488236851 school
// 39.89087746373868,-74.19163364756207 western veterines


// String wmyspeed = String.format("%.0f", dspeed);   double: round two decimals






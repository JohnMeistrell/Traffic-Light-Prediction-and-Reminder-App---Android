package com.example.johnmeistrell.sqliteapp;

        import android.os.Environment;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import android.app.AlertDialog;
        import android.database.Cursor;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import android.os.Bundle;
        //import android.support.design.widget.FloatingActionButton;
        //import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.math.BigDecimal;

        import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {
//public class MainActivity extends ActionBarActivity {
    DatabaseHelper myDb;
    EditText editName,editSurname,editMarks ,editTextId;
    Button btnAddData;
    Button btnviewAll;
    Button btnDelete;
    Button btnviewUpdate;

 /* Timing_table */
     String sID_T = "";
     String sTIMING_GUID_T = "";
     String sROUTE_GUID_T = "";
     String sHOUR_T_T = "";
     String sMINUTE_T_T = "";
     String sSTATE_EACH_SECOND_T = "";
/* Location_table */
     String sID_L = "";
     String sLOCAITON_GUID_L = "";
     String sNAME_L = "";
     String gLONGITUDE_L =  "";
     String gLATITUDE_L =  "";
     String sTYPE_L = "";
/* Lane_table */
     String sID_A = "";
     String sLANE_GUID_A = "";
     short hBEARING_A = 0;
     String sLANE_A = "";
     short hDELAY_SECONDS_A = 0;
     short hSPEED_LIMIT_A = 0;
     String sVOICE_NOTE_A = "";
     String sLOCAITON_GUID_A = "";
     String sTIMING_GUID_A = "";
/* Route_table */
     String sID_R = "";
     String sROUTE_GUID_R = "";
     String sROUTE_NAME_GUID_R = "";
     String sLANE_GUID_R = "";
     int iROUTE_ORDER_R = 0;
/* Route_Name_table */
     String sID_N = "";
     String sROUTE_NAME_GUID_N = "";
     String sNAME_N = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        editName = (EditText)findViewById(R.id.editText_name);
        editSurname = (EditText)findViewById(R.id.editText_surname);
        editMarks = (EditText)findViewById(R.id.editText_Marks);
        editTextId = (EditText)findViewById(R.id.editText_id);
        btnAddData = (Button)findViewById(R.id.button_add);
        btnviewAll = (Button)findViewById(R.id.button_viewAll);
        btnviewUpdate= (Button)findViewById(R.id.button_update);
        btnDelete= (Button)findViewById(R.id.button_delete);
        AddData();
        viewAll();
        UpdateData();
        DeleteData();
    }
    public void DeleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData(editTextId.getText().toString());
                        if(deletedRows > 0)
                            Toast.makeText(MainActivity.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void UpdateData() {
        btnviewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = myDb.updateData(editTextId.getText().toString(),
                                editName.getText().toString(),
                                editSurname.getText().toString(), editMarks.getText().toString());
                        if(isUpdate == true)
                            Toast.makeText(MainActivity.this,"Data Update",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"Data not Updated",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(editName.getText().toString(),
                                editSurname.getText().toString(),
                                editMarks.getText().toString() );
                        if(isInserted == true)
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void viewAll() {
        btnviewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if (res.getCount() == 0) {
                            // show message
                            showMessage("Error", "Nothing found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :" + res.getString(0) + "\n");
                            buffer.append("Name :" + res.getString(1) + "\n");
                            buffer.append("Surname :"+ res.getString(2)+"\n");
                            buffer.append("Marks :"+ res.getString(3)+"\n\n");
                        }

                        // Show all data
                        showMessage("Data",buffer.toString());

                        /* ------------------------------------------------------------*/
                        Cursor resL = myDb.getAllDataLocation();
                        if (resL.getCount() == 0) {
                            // show message
                            showMessage("Error", "Nothing found");

                        } else {

                                StringBuffer bufferL = new StringBuffer();
                                while (resL.moveToNext()) {
                                    bufferL.append("Id :" + resL.getString(0) + "\n");
                                    bufferL.append("Guid :" + resL.getString(1) + "\n");
                                    bufferL.append("Name :" + resL.getString(2) + "\n");
                                    bufferL.append("Long :"+ resL.getString(3)+"\n");
                                    bufferL.append("Lat :"+ resL.getString(4)+"\n\n");
                                    bufferL.append("Type :"+ resL.getString(5)+"\n\n");

                                }

                                // Show all data
                                showMessage("Data",bufferL.toString());

                           
                        }





//---------------------------------------------------
// load light data

 
sLOCAITON_GUID_L  = "83F40176F04C46CA";
sNAME_L  = "r37 main";
gLONGITUDE_L  = "-74.20059504855163";
gLATITUDE_L  = "39.96339221598682";
sTYPE_L  = "L";
boolean isInserted1 = myDb.insertDataLITELocation(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "DFB1A93EEBD2ECA4";
sNAME_L  = "r37 hooper";
gLONGITUDE_L  = "74.18746123656166";
gLATITUDE_L  =  "9.96328202534696";
sTYPE_L  = "L";
boolean isInserted2 = myDb.insertDataLITELocation(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
/*
sLOCAITON_GUID_L  = "DCC42704675929F8";
sNAME_L  = "r37 clifton";
gLONGITUDE_L  = 74.17720453589948;
gLATITUDE_L  = 39.96039469136955;
sTYPE_L  = "L";
boolean4 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "F9FCF2EE92D86CF5";
sNAME_L  = "r37 batchler";
gLONGITUDE_L  = 74.17006321126155;
gLATITUDE_L  = 39.95739114774732;
sTYPE_L  = "L";
boolean5 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "AD5071B3D83FD32F";
sNAME_L  = "r37 washington";
gLONGITUDE_L  = 74.16234320111692;
gLATITUDE_L  = 39.95417803547045;
sTYPE_L  = "L";
boolean6 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "60207BD854AAB946";
sNAME_L  = "r37 west end";
gLONGITUDE_L  = 74.15314773028734;
gLATITUDE_L  = 39.952177861790744;
sTYPE_L  = "L";
boolean7 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "C4B9EFB1ECC93F80";
sNAME_L  = "r37 garfield";
gLONGITUDE_L  = 74.14973292557056;
gLATITUDE_L  = 39.95173042290854;
sTYPE_L  = "L";
boolean8 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "B982DF809A15796B";
sNAME_L  = "r37 coolage";
gLONGITUDE_L  = 74.13949575642647;
gLATITUDE_L  = 39.95077104999166;
sTYPE_L  = "L";
boolean9 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "A346F9F22DBBFFF6";
sNAME_L  = "r37 garfield";
gLONGITUDE_L  = 74.13117544795193;
gLATITUDE_L  = 39.95029790860171;
sTYPE_L  = "L";
boolean10 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "0F2CE16569E3DF04";
sNAME_L  = "fisher";
gLONGITUDE_L  = 74.12423681543635;
gLATITUDE_L  = 39.949968789560856;
sTYPE_L  = "L";
boolean11 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "9CE388EC80DA5898";
sNAME_L  = "bridge";
gLONGITUDE_L  = 74.11508743315733;
gLATITUDE_L  = 39.949321115220016;
sTYPE_L  = "L";
boolean12 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "68C37EF9125D106D";
sNAME_L  = "catalina";
gLONGITUDE_L  = 74.0893269145717;
gLATITUDE_L  = 39.94281343640306;
sTYPE_L  = "L";
boolean13 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "52ADF4802A04ACE4";
sNAME_L  = "central hamplton";
gLONGITUDE_L  = 74.07559551762301;
gLATITUDE_L  = 39.940044245478525;
sTYPE_L  = "L";
boolean14 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "AE298D689F9B97BB";
sNAME_L  = "central summer";
gLONGITUDE_L  = 74.0753684954127;
gLATITUDE_L  = 39.9414146794933;
sTYPE_L  = "L";
boolean15 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "7A4FA58FAD4C1789";
sNAME_L  = "central grant";
gLONGITUDE_L  = 74.0751298860398;
gLATITUDE_L  = 39.94279166654124;
sTYPE_L  = "L";
boolean16 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "7DCFBEF71B012865";
sNAME_L  = "bulavard grant";
gLONGITUDE_L  = 74.07345532928798;
gLATITUDE_L  = 39.94266663655519;
sTYPE_L  = "L";
boolean17 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "9830E1FDC4D076BE";
sNAME_L  = "bulavard summer";
gLONGITUDE_L  = 74.07371196302688;
gLATITUDE_L  = 39.94124109232596;
sTYPE_L  = "L";
boolean18 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "F0586F8CD8397BC1";
sNAME_L  = "double trouble";
gLONGITUDE_L  = 74.22110080718994;
gLATITUDE_L  = 39.897983329753956;
sTYPE_L  = "L";
boolean19 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "B17C433E066806F5";
sNAME_L  = "central parkway";
gLONGITUDE_L  = 74.21118392936478;
gLATITUDE_L  = 39.89221478005739;
sTYPE_L  = "L";
boolean20 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "4DD351B9D90FBFA5";
sNAME_L  = "school";
gLONGITUDE_L  = 74.2057488236851;
gLATITUDE_L  = 39.89280473343495;
sTYPE_L  = "L";
boolean21 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "A0BAD9F7244A12D2";
sNAME_L  = "western veterines";
gLONGITUDE_L  = 74.19163364756207;
gLATITUDE_L  = 39.89087746373868;
sTYPE_L  = "L";
boolean22 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "83697D1A2C7717DC";
sNAME_L  = "GSP bayville exit";
gLONGITUDE_L  = 74.21213965410061;
gLATITUDE_L  = 39.88983741683131;
sTYPE_L  = "N";
boolean23 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "B61C0228D7FB28C4";
sNAME_L  = "GSP Route 72";
gLONGITUDE_L  = 74.27935328474854;
gLATITUDE_L  = 39.711549081450485;
sTYPE_L  = "N";
boolean24 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "7C474000D3739D8E";
sNAME_L  = "R9 Indian Head";
gLONGITUDE_L  = -74.21129493292091;
gLATITUDE_L  = 39.99277185154804;
sTYPE_L  = "L";
boolean25 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "D5925C83A8C37A9A";
sNAME_L  = "R9 Whitty";
gLONGITUDE_L  = -74.21233841727911;
gLATITUDE_L  = 40.000248441992284;
sTYPE_L  = "L";
boolean26 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "3D233413659260A9";
sNAME_L  = "R9 Church";
gLONGITUDE_L  = -74.21592094361762;
gLATITUDE_L  = 40.00924578794255;
sTYPE_L  = "L";
boolean27 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "4A651B8057CB2235";
sNAME_L  = "Church";
gLONGITUDE_L  = -74.20661625325414;
gLATITUDE_L  = 40.01843945121839;
sTYPE_L  = "L";
boolean28 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "10577F5C46DAD845";
sNAME_L  = "New Hampshire Church";
gLONGITUDE_L  = -74.19832281148603;
gLATITUDE_L  = 40.01680634752928;
sTYPE_L  = "L";
boolean29 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "247C436EC69D2334";
sNAME_L  = "New Hampshire Greenbriar";
gLONGITUDE_L  = -74.20113590766334;
gLATITUDE_L  = 40.03369433321855;
sTYPE_L  = "L";
boolean30 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "BEB7F29672C0838F";
sNAME_L  = "New Hampshire Locust";
gLONGITUDE_L  = -74.19747582487127;
gLATITUDE_L  = 40.0456600329797;
sTYPE_L  = "L";
boolean31 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "36D553140586B429";
sNAME_L  = "R70 New Hampshire";
gLONGITUDE_L  = -74.19669493050855;
gLATITUDE_L  = 40.05044421943642;
sTYPE_L  = "L";
boolean32 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "4587C43F4ACB4539";
sNAME_L  = "New Hampshire Oak";
gLONGITUDE_L  = -74.1949992125941;
gLATITUDE_L  = 40.06038285780912;
sTYPE_L  = "L";
boolean33 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "692A596526D4BE47";
sNAME_L  = "Barnegat Bay";
gLONGITUDE_L  = -74.24328214836589;
gLATITUDE_L  = 39.75765254129542;
sTYPE_L  = "L";
boolean34 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "39A0CD4FE5723555";
sNAME_L  = "Bay Acme";
gLONGITUDE_L  = -74.24709539125854;
gLATITUDE_L  = 39.75848804453883;
sTYPE_L  = "L";
boolean35 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "E91A6C101A11A60F";
sNAME_L  = "Bay Parkway N yeld";
gLONGITUDE_L  = -74.24892959306146;
gLATITUDE_L  = 39.75977385852594;
sTYPE_L  = "L";
boolean36 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "D14FC48FB83C958C";
sNAME_L  = "Bay Parkway N";
gLONGITUDE_L  = -74.24914374061939;
gLATITUDE_L  = 39.75898125884403;
sTYPE_L  = "L";
boolean37 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "454115975FB46CE5";
sNAME_L  = "Bay Wawa";
gLONGITUDE_L  = -74.25422853779713;
gLATITUDE_L  = 39.76000314156515;
sTYPE_L  = "L";
boolean38 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "74B8F727EA1D136E";
sNAME_L  = "Bay Nautilus";
gLONGITUDE_L  = -74.26160261122942;
gLATITUDE_L  = 39.7616064524374;
sTYPE_L  = "L";
boolean39 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "9E1EA0B4BD0F838D";
sNAME_L  = " Western Manchester";
gLONGITUDE_L  = -74.19617361804592;
gLATITUDE_L  = 39.86409021159322;
sTYPE_L  = "L";
boolean40 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "792532F67F34E8B6";
sNAME_L  = "Manchester Lacy";
gLONGITUDE_L  = -74.20523373719038;
gLATITUDE_L  = 39.85902566066131;
sTYPE_L  = "L";
boolean41 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "E79ED9F66BB49E28";
sNAME_L  = "Lacy Deerhead";
gLONGITUDE_L  = -74.20281731029433;
gLATITUDE_L  = 39.85625301250412;
sTYPE_L  = "L";
boolean42 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "CC056FDEF6502014";
sNAME_L  = "R9 Lakeside";
gLONGITUDE_L  = -74.19619818890322;
gLATITUDE_L  = 39.83395733565184;
sTYPE_L  = "L";
boolean43 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "6689F3094EF48E6E";
sNAME_L  = "R9 Taylor Wawa";
gLONGITUDE_L  = -74.1998862764188;
gLATITUDE_L  = 39.82916028389896;
sTYPE_L  = "L";
boolean44 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "8319A99EBF447315";
sNAME_L  = "R9 Wellsmill Wawa";
gLONGITUDE_L  = -74.19546819786205;
gLATITUDE_L  = 39.79133272595005;
sTYPE_L  = "L";
boolean45 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "B969F81C41A5363D";
sNAME_L  = "R9 Pancoast";
gLONGITUDE_L  = -74.20469413854675;
gLATITUDE_L  = 39.77912913124328;
sTYPE_L  = "L";
boolean46 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "1690178173DE6070";
sNAME_L  = "R9 Barnegat Blvd Naples";
gLONGITUDE_L  = -74.21353446032475;
gLATITUDE_L  = 39.768064610991146;
sTYPE_L  = "L";
boolean47 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "F11F2FA2572E0511";
sNAME_L  = "R9 Bay Sweet Jen";
gLONGITUDE_L  = -74.22308672700018;
gLATITUDE_L  = 39.75325578473349;
sTYPE_L  = "L";
boolean48 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "61BF6B0F7752BC12";
sNAME_L  = "R9 Gunning River";
gLONGITUDE_L  = -74.23275643688481;
gLATITUDE_L  = 39.74194091630944;
sTYPE_L  = "L";
boolean49 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "554FBC74AC7D80BD";
sNAME_L  = "R9 Georgetown";
gLONGITUDE_L  = -74.23748639963185;
gLATITUDE_L  = 39.7323816577469;
sTYPE_L  = "L";
boolean50 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "A452FAD37438AC26";
sNAME_L  = "R9 Hillard Wawa";
gLONGITUDE_L  = -74.25332986996585;
gLATITUDE_L  = 39.70617149764149;
sTYPE_L  = "L";
boolean51 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "B3D929BB883368B0";
sNAME_L  = "R9 Bay R72";
gLONGITUDE_L  = -74.25901725804544;
gLATITUDE_L  = 39.695414640110656;
sTYPE_L  = "L";
boolean52 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
sLOCAITON_GUID_L  = "3A7579B64239F5BA";
sNAME_L  = "R9 McKinley SR";
gLONGITUDE_L  = -74.2657140383061;
gLATITUDE_L  = 39.68750760745853;
sTYPE_L  = "L";
boolean53 = myDb.insertDataLITELocation_table(sLOCAITON_GUID_L,sNAME_L,gLONGITUDE_L,gLATITUDE_L,sTYPE_L);
 
*/

//---------------------------------------------------


            //--------------------------------------

                        File path = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DCIM);


                        File f=new File("/data/data/com.example.johnmeistrell.sqliteapp/databases/Traffic2.db");
                        FileInputStream fis=null;
                        FileOutputStream fos=null;

                        try
                        {
                            fis=new FileInputStream(f);

                            //--------------------------------------------
                            File bobFF = new File(path, "dbwwwAAA4_dump.db.txt");

                            //FileOutputStream fos = null;

                            try {
                                fos = new FileOutputStream(bobFF,true);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            //--------------------------------------------


                            //fos=new FileOutputStream("/mnt/sdcard/dbwww4_dump.db");
                            //fos=new FileOutputStream("/mnt/ext_card/dbwwwA4_dump.db");
                            while(true)
                            {
                                int i=fis.read();
                                if(i!=-1)
                                {fos.write(i);}
                                else
                                {break;}
                            }
                            fos.flush();
                            //makeText(this, "DB dump OK", LENGTH_LONG).show();
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                            //makeText(this, "DB dump ERROR", LENGTH_LONG).show();
                        }
                        finally
                        {
                            try
                            {
                                fos.close();
                                fis.close();
                            }
                            catch(IOException ioe)
                            {}
                        }






                        //----------------------------------------------------------------






                    }
                }
        );
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



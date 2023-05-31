package com.example.myapplication6;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication6.adapter.ListItem;
import com.example.myapplication6.db.AppExecuter;
import com.example.myapplication6.db.MyConstants;
import com.example.myapplication6.db.MyDbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditActivity extends AppCompatActivity {

    private final int PICK_IMAGE_CODE = 123;
    private ImageView imNewImage;
    private ConstraintLayout imageContainer;
    private FloatingActionButton fbAddImage;
    private ImageButton imEditImage, imDeleteImage;
    private EditText edTitle, edDisc;
    private MyDbManager myDbManager;
    private String tempUri = "empty";
    private boolean isEditState = true;
    private ListItem item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        getMyIntents();


    }
    @Override
    protected void onResume() {
        super.onResume();

        myDbManager.openDb();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_CODE && data !=null){

            tempUri = data.getData().toString();
            imNewImage.setImageURI(data.getData());
            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);

        }

    }


    private void init(){
        edTitle = findViewById(R.id.edTitle);
        edDisc = findViewById(R.id.edDisc);
        imNewImage = findViewById(R.id.imNewImage);
        fbAddImage = findViewById(R.id.fbAddImage);
        imageContainer = findViewById(R.id.imageContainer);
        imEditImage = findViewById(R.id.imEditImage);
        imDeleteImage = findViewById(R.id.imDeleteImage);
        myDbManager = new MyDbManager(this);
    }

    private void getMyIntents(){

        Intent i = getIntent();
        if(i != null){

            item = (ListItem) i.getSerializableExtra(MyConstants.LIST_ITEM_INTENT);
            isEditState = i.getBooleanExtra(MyConstants.EDIT_STATE, true);

            if(!isEditState){
                tempUri = item.getUri();
                edTitle.setText(item.getTitle());
                edDisc.setText(item.getDisc());
                if(!item.getUri().equals("empty")){
                    imageContainer.setVisibility(View.VISIBLE);
                    imNewImage.setImageURI(Uri.parse(item.getUri()));
                    imDeleteImage.setVisibility(View.GONE);
                    imEditImage.setVisibility(View.GONE);
                }

            }


        }

    }

    public void onClickSave(View view) {

        final String title = edTitle.getText().toString();
        final String disc = edDisc.getText().toString();

        if (title.equals("") || disc.equals("")) {

            Toast.makeText(this, R.string.text_empty, Toast.LENGTH_SHORT).show();

        } else {
            if (isEditState){

            AppExecuter.getInstance().getSubIO().execute(new Runnable() {
                @Override
                public void run(){
                    myDbManager.insertToDb(title, disc, tempUri );
                }
            });

            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();


        } else {

                myDbManager.updateItem(title, disc, tempUri, item.getId());
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            }
            myDbManager.closeDb();
            finish();
        }
    }

    public void onClickDeleteImage(View view){

        imNewImage.setImageResource(R.drawable.ic_baseline_fastfood_24);
        tempUri = "empty";
        imageContainer.setVisibility(View.GONE);
        fbAddImage.setVisibility(View.VISIBLE);

    }
    public void onClickAddImage(View view){
        imageContainer.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    public void onClickChooseImage(View view){
        Intent chooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooser.setType("image/*");
        startActivityForResult(chooser, PICK_IMAGE_CODE);
    }

}
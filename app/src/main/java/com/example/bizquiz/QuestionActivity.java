package com.example.bizquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class QuestionActivity extends AppCompatActivity {

    FirebaseDatabase database =FirebaseDatabase.getInstance();
    DatabaseReference myref =database.getReference();



    private TextView question;
    private TextView numIndicator;
    private TextView countDown;
    private LinearLayout optionContainer;
    private Button nextBtn,previousBtn;
    private int count =0;
    private List<QuestionModel> list;
    private int position=0;
    private int score =0;
    private String setId;
    private Dialog loadingDialog;
    private ImageView shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.home));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        question = findViewById(R.id.question);
        numIndicator = findViewById(R.id.no_indicator);
        countDown = findViewById(R.id.countDown);
        optionContainer = findViewById(R.id.optionContainer);
        nextBtn = findViewById(R.id.nextBtn);
        previousBtn = findViewById(R.id.previous);
        shareBtn = findViewById(R.id.shareBtn);



        setId = getIntent().getStringExtra("setId");

        list=new ArrayList<>();
       /* list.add(new QuestionModel("Who is the father of the Nation","Mahatma Gandi","Pt.JawaharLal Nehru","Rajendra Place","Narendra Modi","Narendra Modi"));
        list.add(new QuestionModel("Who was the father of the Nation","Mahatma Gandi","Pt.JawaharLal Nehru","Rajendra Place","Narendra Modi","Narendra Modi"));
        list.add(new QuestionModel("when was the father of the Nation","Mahatma Gandi","Pt.JawaharLal Nehru","Rajendra Place","Narendra Modi","Pt.JawaharLal Nehru"));
        list.add(new QuestionModel("When is the father of the Nation","Mahatma Gandi","Pt.JawaharLal Nehru","Rajendra Place","Narendra Modi","Narendra Modi"));
        list.add(new QuestionModel("Who is the father of the Country","Mahatma Gandi","Pt.JawaharLal Nehru","Rajendra Place","Narendra Modi","Mahatma Gandi"));
        list.add(new QuestionModel("Who is the father of the GandiBat","Mahatma Gandi","Pt.JawaharLal Nehru","Rajendra Place","Narendra Modi","Narendra Modi"));
        */







        list = new ArrayList<>();
        loadingDialog.show();
        FirebaseDatabase.getInstance().getReference()
                .child("SETS").child(setId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            String id = dataSnapshot1.getKey();
                            String question = dataSnapshot1.child("question").getValue().toString();
                            String optionA = dataSnapshot1.child("optionA").getValue().toString();
                            String optionB= dataSnapshot1.child("optionB").getValue().toString();
                            String optionC = dataSnapshot1.child("optionC").getValue().toString();
                            String optionD = dataSnapshot1.child("optionD").getValue().toString();
                            String answer = dataSnapshot1.child("correctAns").getValue().toString();

                            list.add(new QuestionModel(id,question,optionA,optionB,optionC,optionD,answer,setId));
                        }
                        if (list.size()>0){

                            for (int i=0;i<4;i++){
                                optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        checkAnswer((Button) v);
                                    }
                                });
                            }

                            playAnim(question,0,list.get(position).getQuestion());

                            nextBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    nextBtn.setEnabled(false);
                                    nextBtn.setAlpha(0.7f);
                                    enableOption(true);
                                    position++;
                                    if (position == list.size()) {
                                        //snd to score Activity
                                        Intent scoreIntent = new Intent(QuestionActivity.this,ScoreActivity.class);
                                        scoreIntent.putExtra("score",score);
                                        scoreIntent.putExtra("total",list.size());
                                        startActivity(scoreIntent);
                                        finish();
                                        return;
                                    }
                                    count=0;
                                    playAnim(question,0,list.get(position).getQuestion());
                                }
                            });


                          shareBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String body= list.get(position).getQuestion() + "\n"+
                                            list.get(position).getOptionA()+ "\n"+
                                            list.get(position).getOptionB()+ "\n"+
                                            list.get(position).getOptionC()+ "\n"+
                                            list.get(position).getOptionD();
                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    shareIntent.setType("text/plain");
                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"BizQuiz Challange || Download the App: https://www.youtube.com/");
                                    shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                                    startActivity(Intent.createChooser(shareIntent,"Share Via"));
                                }
                            });



                            previousBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    position--;
                                    previousOption(false);
                                    if (position==0 && position<list.size()) {
                                        finish();
                                        Toast.makeText(QuestionActivity.this,"Sorry ! There is no any previous Questions. Please select the Set first.",Toast.LENGTH_LONG).show();
                                    }
                                    //count=0;
                                    playAnim(question,0,list.get(position).getQuestion());
                                }
                            });

                        }else{
                            finish();
                            Toast.makeText(QuestionActivity.this,"No more Question is Found !",Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(QuestionActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        finish();
                    }
                });
    }


    private void playAnim(final View view, final int value, final String data){
            for (int i=0;i<4;i++){
                optionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }

        view.animate().alpha(value).scaleX(value).scaleX(value).setDuration(400).setStartDelay(50)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ///when animation start it looks blur
                if (value == 0 && count<4) {
                    String option = "";
                  if (count==0){
                      option = list.get(position).getOptionA();
                  }else if(count==1){
                      option = list.get(position).getOptionB();
                  }else if(count==2){
                      option = list.get(position).getOptionC();
                  }else if(count==3){
                      option = list.get(position).getOptionD();
                  }
                    playAnim(optionContainer.getChildAt(count),0,option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ////data changed animation

                if (value==0){
                    try {
                        ((TextView)view).setText(data);
                        numIndicator.setText(position+1+"/"+list.size());
                    }catch (ClassCastException e){
                        ((Button)view).setText(data);
                    }
                  view.setTag(data);
                    playAnim(view,1,data);
                }else {
                    enableOption(true);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button selectedOption){
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1f);
        if (selectedOption.getText().toString().equals(list.get(position).getAnswer())){
            //correct
            score++;
        selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }else{
            //incorrect
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F30606")));
          Button correctOption =  (Button) optionContainer.findViewWithTag(list.get(position).getAnswer());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
    }

    private void enableOption(boolean enable){
        for (int i=0;i<4;i++){
            optionContainer.getChildAt(i).setEnabled(enable);
            if (enable){
                optionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }

    private void previousOption(boolean enable){
       for (int i=0;i<4;i++){
                optionContainer.setEnabled(enable);
                if (enable){
                    optionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }

 /*
        for (int i=0;i<4;i++){
            optionContainer.getChildAt(i).setEnabled(disable);
            if (disable){
             Button correctOption =  (Button) optionContainer.findViewWithTag(list.get(position).getCorrectAns());
                correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }else{
                optionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
*/
    }
}

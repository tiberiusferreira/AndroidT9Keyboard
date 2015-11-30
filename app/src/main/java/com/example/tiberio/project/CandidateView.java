package com.example.tiberio.project;

/**
 * Created by tiberio on 16/11/2015.
 */
//
//src\com\example\android\softkeyboard\CandidateView.java
/*
 * Copyright (C) 2008-2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import android.view.View;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;


import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CandidateView extends View {

    Paint paint;
    String message;
    float a;
    SearchTree tree = new SearchTree();
    ArrayList<String> Candidates = new ArrayList<>();




    private Drawable mSelectionHighlight;

    private int mColorNormal;
    private int mVerticalPadding;
    private Paint mPaint;
    private SimpleIME mService;

    public CandidateView(Context context) {
        super(context);

        Candidates.add("");
        Candidates.add("");
        Candidates.add("");
        tree.create_dictionary(getResources().openRawResource(R.raw.dictionary));


        mSelectionHighlight = context.getResources().getDrawable(
                android.R.drawable.list_selector_background);


        Resources r = context.getResources();

        setBackgroundColor(r.getColor(R.color.candidate_background));

        mColorNormal = r.getColor(R.color.candidate_normal);

        mVerticalPadding = r.getDimensionPixelSize(R.dimen.candidate_vertical_padding);

        mPaint = new Paint();
        mPaint.setColor(mColorNormal);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(r.getDimensionPixelSize(R.dimen.candidate_font_height));
        mPaint.setStrokeWidth(0);


        //paint = new Paint();
        setHorizontalFadingEdgeEnabled(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        message = "This is ok!";
        a =mPaint.measureText(message);




    }

    /**
     * A connection back to the service to communicate with the text field
     * @param listener
     */
    public void setService(SimpleIME listener) {
        mService = listener;
    }
    public int IsDeadEnd(String word){
        return tree.isDeadEnd(word);
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //int measuredWidth = resolveSize(50, widthMeasureSpec);

        // Get the desired height of the icon menu view (last row of items does
        // not have a divider below)
        Rect padding = new Rect();
        mSelectionHighlight.getPadding(padding);
        final int desiredHeight = ((int)mPaint.getTextSize()) + mVerticalPadding
                + padding.top + padding.bottom;

        // Maximum possible width and desired height
        setMeasuredDimension(widthMeasureSpec,
                resolveSize(desiredHeight, heightMeasureSpec));
    }





    /**
     * If the canvas is null, then only touch calculations are performed to pick the target
     * candidate.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null) {
            super.onDraw(canvas);


            //mPaint.setTextAlign(Paint.Align.CENTER);
//            paint.setTextSize(50);
//            paint.setColor(Color.RED);

            System.out.print("Canvas Not NULL!!\nSize = " + a + "\n");
            canvas.drawText(Candidates.get(0), ((getWidth()/3)-a)/2, (((getHeight() - mPaint.getTextSize()) / 2) - mPaint.ascent()), mPaint);
            canvas.drawText(Candidates.get(1),getWidth()/3+((getWidth()/3)-a)/2 ,  (((getHeight() - mPaint.getTextSize()) / 2) - mPaint.ascent()), mPaint);
            canvas.drawText(Candidates.get(2),2*getWidth()/3+((getWidth()/3)-a)/2 ,  (((getHeight() - mPaint.getTextSize()) / 2) - mPaint.ascent()), mPaint);

        }


    }

    public ArrayList<String> getAllCombination(String already_typed, int number){
        /* Receives a string and number and returns all an array of Strings which are
        *  the combinations of the string plus the chars possible for the number. */
        //System.out.format("Number typed="+number+"\n\n");
        ArrayList<String> combinations = new ArrayList<>();
        String possible_chars;
        if(number==2){
            possible_chars="abc";
        }else if(number==3){
            possible_chars="def";
        }else if(number==4){
            possible_chars="ghi";
        }else if(number==5){
            possible_chars="jkl";
        }else if(number==6){
            possible_chars="mno";
        }else if(number==7){
            possible_chars="pqrs";
        }else if(number==8){
            possible_chars="tuv";
        }else if(number==9){
            possible_chars="wxyz";
        }else{
            return null;
        }
        int i;
            for (i=0; i < possible_chars.length(); i++) {
                already_typed = already_typed.concat(String.valueOf(possible_chars.charAt(i)));
                combinations.add(already_typed);
                already_typed = already_typed.substring(0, already_typed.length() - 1);
            }
        //System.out.format("All Combinations = " + combinations.toString()+"\n");
        return combinations;
    }

    public ArrayList<String> GetThreeBestCandidates(ArrayList<String> combinations,int depth){
        double highest_f1=0,highest_f2=0,highest_f3=0;
        ArrayList<String> BestCandidates = new ArrayList<>();
        BestCandidates.add("");
        BestCandidates.add("");
        BestCandidates.add("");
        ArrayList<String> CloseCandidates = new ArrayList<>();


        if(combinations==null){
            System.out.format("No best Candidates, null Combinations!\n");
            return BestCandidates;
        }
        for(String currentString : combinations){
            double current_word;
            current_word=this.tree.isWord(currentString);
            if(current_word>0) {
                System.out.format(currentString + " Is word!\n");
            }
            if(current_word>highest_f1){
                System.out.format("This word is the best choise ! "+current_word+ " \n");
                highest_f1=current_word;
                System.out.format("BestCandidates 0 = " + BestCandidates.get(0) + " at 1 = " + BestCandidates.get(1) + " at 2 = " + BestCandidates.get(2) + "\n");
                if(BestCandidates.get(2).compareTo("")!=0){
                    System.out.format("Moving " + BestCandidates.get(1) + " to " + BestCandidates.get(2) + " 1 to 2 \n");
                    BestCandidates.set(2, BestCandidates.get(1));
                    BestCandidates.set(1,BestCandidates.get(0));
                }else if(BestCandidates.get(1).compareTo("")!=0) {
                    System.out.format("Moving " + BestCandidates.get(0) + " to " + BestCandidates.get(1) + " 0 to 1 \n");
                    BestCandidates.set(2, BestCandidates.get(1));
                    BestCandidates.set(1, BestCandidates.get(0));
                }else if(BestCandidates.get(0).compareTo("")!=0) {
                    BestCandidates.set(1, BestCandidates.get(0));
                }
                BestCandidates.set(0,currentString);
            }else if(current_word>highest_f2){
                System.out.format("This word is the second best choise!"+current_word+ " \n");
                highest_f2=current_word;
                if(BestCandidates.get(2).compareTo("")!=0){
                    BestCandidates.set(2,BestCandidates.get(1));
                }
                BestCandidates.set(1,currentString);
            }else if(current_word>highest_f3){
                highest_f3=current_word;
                System.out.format("This word is the third best choise!"+current_word+ "\n");
                BestCandidates.set(2,currentString);
            }
        }
        if(highest_f3==0 && depth<5){
            System.out.println("Stuck at depth = "+depth);

            for(String currentString : combinations){

//                System.out.format("Current String "+ currentString+" and new Candidates are "+ tree.get_similar_word_depth(currentString,1)+ "\n\n");
                for(String new_words : tree.get_similar_word_depth(currentString,depth)){
                    CloseCandidates.add(new_words);
                }
            }
            for(String currentString : BestCandidates){
                CloseCandidates.add(currentString);
            }
//            System.out.format("Close CANDIDATES = " + CloseCandidates.toString());
//            System.out.flush();
                BestCandidates=GetThreeBestCandidates(CloseCandidates,depth+2);
        }
        System.out.format("Best candidates = "+BestCandidates.toString()+"\n");
        return BestCandidates;
    }


    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if(me.getAction()==MotionEvent.ACTION_DOWN) {

            float x = me.getX();
            float y = me.getY();
            if (x > 0 && x <= getWidth() / 3 && Candidates.size()>0) {
                System.out.println("Inputting "+ Candidates.get(0)+ "\n");
                mService.InputText(Candidates.get(0));
                Candidates.clear();
                Candidates.add("");
                Candidates.add("");
                Candidates.add("");
                mService.AllCombinations.clear();
                this.invalidate();
                System.out.print("First!");
                System.out.flush();
                return true;
            } else if (x > getWidth() / 3 && x < 2 * getWidth() / 3 && Candidates.size()>1) {
                System.out.println("Inputting " + Candidates.get(1)+ "\n");

                mService.InputText(Candidates.get(1));
                Candidates.clear();
                Candidates.add("");
                Candidates.add("");
                Candidates.add("");
                mService.AllCombinations.clear();
                System.out.print("Second!");
                System.out.flush();
                this.invalidate();
                return true;
            } else if (x > 2 * getWidth() / 3 && x < getWidth() && Candidates.size()>2) {
                System.out.println("Inputting "+ Candidates.get(2)+ "\n");

                mService.InputText(Candidates.get(2));
                Candidates.clear();
                Candidates.add("");
                Candidates.add("");
                Candidates.add("");
                mService.AllCombinations.clear();
                System.out.print("Third!");
                System.out.flush();
                this.invalidate();
                return true;
            }
        }
        return false;
    }







}



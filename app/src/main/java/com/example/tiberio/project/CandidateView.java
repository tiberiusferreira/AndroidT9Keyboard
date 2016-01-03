package com.example.tiberio.project;

/**
 * Created by tiberio on 16/11/2015.
 * This is the Candidates View. Here the suggestions are queried and drawn.
 * The SuggestionsGenerator provides the suggestions.
 * A reference to the main class (SimpleIME) is used here in order to send text to the screen.
 *
 */
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import java.util.ArrayList;


public class CandidateView extends View {
    /* Candidates hold the Candidates.
    * mService is the reference to the "main" class.
    * currentCandidatesPage holds the currently displayed page.*/
    private SuggestionsGenerator sugestionsgenerator = new SuggestionsGenerator();
    String typedNumbersSTR = "";
    ArrayList<String> Candidates = new ArrayList<>();
    private Drawable mSelectionHighlight;
    private int mVerticalPadding;
    private int currentCandidatesPage = 1;
    private Paint mPaint;
    private SimpleIME mService;
    Rect padding = new Rect();

    public CandidateView(Context context) {
        super(context);
        /*The app design is to always have at least 3 candidates, so it is not
        * necessary to always check for null pointers.*/
        Candidates.add("");
        Candidates.add("");
        Candidates.add("");
        /*The dictionary is a simple xls "excel" file with words and theirs frequency.*/
        sugestionsgenerator.setDictionary(getResources().openRawResource(R.raw.dictionary));
        /*From here to the end of this method is just Android boilerplate code.*/
        mSelectionHighlight=ContextCompat.getDrawable(context,android.R.drawable.list_selector_background);
        ContextCompat.getColor(context, R.color.candidate_background);
        Resources r = context.getResources();
        setBackgroundColor(ContextCompat.getColor(context, R.color.candidate_background));
        mVerticalPadding = r.getDimensionPixelSize(R.dimen.candidate_vertical_padding);
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context, R.color.candidate_normal));
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(r.getDimensionPixelSize(R.dimen.candidate_font_height));
        mPaint.setStrokeWidth(0);
        setHorizontalFadingEdgeEnabled(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
    }

    /**
     * A connection back to the service to communicate with the text field
     * @param listener
     */
    public void setService(SimpleIME listener) {
        mService = listener;
    }

    public void updateSugestions(int typed_number){
        currentCandidatesPage=1;
        typedNumbersSTR=typedNumbersSTR.concat((Integer.toString(typed_number)));
        Candidates=sugestionsgenerator.updateSugestions(typed_number);
        /*If all candidates are empty strings, reset the numbers displayed.*/
        for (String str : Candidates){
            if(str.compareTo("")!=0){
                break;
            }
            if(str.compareTo(Candidates.get(Candidates.size()-1))==0){
                typedNumbersSTR="";
            }
        }

        this.invalidate(); //invalidate triggers an app redraw.
    }

    public void getNextCandidates(){
        if(Candidates.size()<4){
            currentCandidatesPage=1;
            return;
        }
        if(currentCandidatesPage*3<Candidates.size()){
            currentCandidatesPage++;
            invalidate();
        }else{
            currentCandidatesPage=1;
            invalidate();
        }
    }

    public void ClearCandidates(){
        /*Clears the candidates, makes sure it's not empty
        * and triggers a redraw with invalidade.*/
        currentCandidatesPage=1;
        sugestionsgenerator.clearSugestion();
        typedNumbersSTR="";
        Candidates.clear();
        Candidates.add("");
        Candidates.add("");
        Candidates.add("");
        this.invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*Get the desired height of the icon menu view (last row of items does
         not have a divider below)*/
        mSelectionHighlight.getPadding(padding);
        final int desiredHeight = ((int)mPaint.getTextSize()) + mVerticalPadding
                + padding.top + padding.bottom;
        // Maximum possible width and desired height
        setMeasuredDimension(widthMeasureSpec, resolveSize(desiredHeight, heightMeasureSpec));
    }





    @Override
    protected void onDraw(Canvas canvas) {
        /*Here the candidates are drawn. They are down each at the center of their one third
        * of screen space. Their size is taken into account in order to draw them at the center.*/
        if (canvas != null) {
            super.onDraw(canvas);
            while(Candidates.size()<currentCandidatesPage*3){
                Candidates.add(""); //there might be 4 candidates, so the page might not be full.
            }
            System.out.println("TEXT = "+typedNumbersSTR);
            /*Draw the numbers typed.*/
            mPaint.setTextSize(mPaint.getTextSize() / 2);
            float text_size= mPaint.measureText(typedNumbersSTR);
            canvas.drawText(typedNumbersSTR, getWidth()/3+((getWidth()/3)-text_size)/2, (-mPaint.ascent()), mPaint);
            mPaint.setTextSize(mPaint.getTextSize()*2);
            /*Draw at the first horizontal third of the screen minus the (text size)/2 so it
            * stays centered.*/
            String CurrentCandidate = Candidates.get(3*(currentCandidatesPage-1));
            text_size=mPaint.measureText(CurrentCandidate);
            //This could be converted to a loop, but since it's only 3, it might not be worth it.
            canvas.drawText(CurrentCandidate, ((getWidth()/3)-text_size)/2, (( (getHeight() - mPaint.getTextSize()) / 2) - mPaint.ascent()), mPaint);
            CurrentCandidate = Candidates.get(3*(currentCandidatesPage-1)+1);
            text_size=mPaint.measureText(CurrentCandidate);
            canvas.drawText(CurrentCandidate, getWidth()/3+((getWidth()/3)-text_size)/2 ,  (((getHeight() - mPaint.getTextSize()) / 2) - mPaint.ascent()), mPaint);
            CurrentCandidate = Candidates.get(3*(currentCandidatesPage-1)+2);
            text_size=mPaint.measureText(CurrentCandidate);
            canvas.drawText(CurrentCandidate, 2 * getWidth() / 3 + ((getWidth() / 3) - text_size) / 2, (((getHeight() - mPaint.getTextSize()) / 2) - mPaint.ascent()), mPaint);

        }

    }



    @Override
    public boolean onTouchEvent(MotionEvent motionevent) {
        /*Here is detected if the used pressed and selected a candidate and
        * the corresponding candidate is sent to the text field.*/
        if(motionevent.getAction()==MotionEvent.ACTION_DOWN) {
            float x = motionevent.getX();
            if (x > 0 && x <= getWidth() / 3 && Candidates.size()>0) {
                mService.InputText(Candidates.get(3*(currentCandidatesPage-1)));
                ClearCandidates();
                return true;
            } else if (x > getWidth() / 3 && x < 2 * getWidth() / 3 && Candidates.size()>1) {
                mService.InputText(Candidates.get(3*(currentCandidatesPage-1)+1));
                ClearCandidates();
                return true;
            } else if (x > 2 * getWidth() / 3 && x < getWidth() && Candidates.size() > 2) {
                mService.InputText(Candidates.get(3*(currentCandidatesPage-1)+2));
                ClearCandidates();
                return true;
            }
        }
        return false;
    }

}



package com.example.tiberio.project;

/**
 * Created by tiberio on 16/11/2015.
 */
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
    private SugestionsGenerator sugestionsgenerator = new SugestionsGenerator();
    String message;
    float a;
    SearchTree tree = new SearchTree();
    ArrayList<String> Candidates = new ArrayList<>();
    private Drawable mSelectionHighlight;
    private int mColorNormal;
    private int mVerticalPadding;
    private int currentCandidatesPage = 1;
    private Paint mPaint;
    private SimpleIME mService;

    public CandidateView(Context context) {
        super(context);

        Candidates.add("");
        Candidates.add("");
        Candidates.add("");
        sugestionsgenerator.setDictionary(getResources().openRawResource(R.raw.dictionary));
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

    public void updateSugestions(int typed_number){
        currentCandidatesPage=1;
        Candidates=sugestionsgenerator.updateSugestions(typed_number);
        this.invalidate();
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
        Candidates.clear();
        Candidates.add("");
        Candidates.add("");
        Candidates.add("");
        this.invalidate();
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
            if(Candidates.size()<currentCandidatesPage*3){
                Candidates.add("");
                Candidates.add("");
            }else if(Candidates.size()<currentCandidatesPage*3+1) {
                Candidates.add("");
            }

            canvas.drawText(Candidates.get(3*(currentCandidatesPage-1)), ((getWidth()/3)-a)/2, (((getHeight() - mPaint.getTextSize()) / 2) - mPaint.ascent()), mPaint);
            canvas.drawText(Candidates.get(3*(currentCandidatesPage-1)+1),getWidth()/3+((getWidth()/3)-a)/2 ,  (((getHeight() - mPaint.getTextSize()) / 2) - mPaint.ascent()), mPaint);
            canvas.drawText(Candidates.get(3*(currentCandidatesPage-1)+2), 2 * getWidth() / 3 + ((getWidth() / 3) - a) / 2, (((getHeight() - mPaint.getTextSize()) / 2) - mPaint.ascent()), mPaint);

        }


    }



    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if(me.getAction()==MotionEvent.ACTION_DOWN) {

            float x = me.getX();
            float y = me.getY();
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



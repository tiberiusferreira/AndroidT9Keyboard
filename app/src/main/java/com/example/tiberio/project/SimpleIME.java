package com.example.tiberio.project;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by tiberio on 13/11/2015.
 * This is the "main" of the project.
 */

public class SimpleIME extends InputMethodService
        implements MyKeyboardView.OnKeyboardActionListener {

    private MyKeyboardView keyboardview;
    private Keyboard keyboard;
    private CandidateView candidateview;
    ArrayList<String> AllCombinations = new ArrayList<>();
    Calendar c = Calendar.getInstance();
    long seconds = System.currentTimeMillis();
    long last_input_seconds = System.currentTimeMillis();
    int last_char=0;
    int number_since_last_input=-1;

    /*These methods are necessary in order to implement MyKeyboardView.OnKeyboardActionListener */
    @Override
    public void onKey(int primaryCode, int[] keyCodes){

    }


    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

    @Override
    public View onCreateCandidatesView() {
        /*This is called once when the keyboard is created.
        * The service is used in order to provide a way for
        * the candidates view to input text, input a candidate
        * when chosen.*/
        setCandidatesViewShown(true);
        candidateview = new CandidateView(this);
        candidateview.setService(this);
        return candidateview;
    }

    @Override
    public View onCreateInputView() {
        /*Sets the keyboard view and this object as event listener.*/
        keyboardview = (MyKeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);

        keyboardview.setKeyboard(keyboard);
        keyboardview.setOnKeyboardActionListener(this);
        return keyboardview;
    }

    public void InputText(String input) {
        /*Method that pushes text to the user's text field.
        * It also adds a space after the text so the next
        * word can be written immediately.*/
        input = input.concat(" ");
        InputConnection ic = getCurrentInputConnection();
        ic.commitText(input, input.length());
    }

    public void ClearSugestions() {
        /*Sets keyboard to guess a new word.*/
        AllCombinations.clear();
        AllCombinations.add("");
        candidateview.ClearCandidates();
    }

    @Override
    public void onPress(final int primaryCode) {
        /*When the user presses a key this method is called.
        * Instead of always checking if AllCombinations is
        * null or empty, just keep it with an empty string.
        * This also facilitates the implementation of other
        * methods, since they don't need to treat special cases
        * anymore.*/
        seconds = System.currentTimeMillis();
    }
    @Override
    public void onRelease(int primaryCode) {

        InputConnection inputconnection = getCurrentInputConnection();
        if((System.currentTimeMillis()-seconds)>500){
            System.out.println("Got long");
            if(primaryCode==46){
                inputconnection.commitText("1", 1);
                return;
            }
            if(primaryCode==32){
                inputconnection.commitText("0", 1);
                return;
            }
            else if(primaryCode>=50 && primaryCode<=57) {
                char code = (char) primaryCode;
                inputconnection.commitText(String.valueOf(code), 1);
                return;
            }
        }
        if (AllCombinations.size() == 0) {
            AllCombinations.add("");
        }
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ClearSugestions();
                inputconnection.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_DONE:
                ClearSugestions();
                System.out.println("Got here!");
                if(inputconnection.performEditorAction(EditorInfo.IME_ACTION_GO)){
                    return;
                }

                break;
            default:
                char code = (char) primaryCode; //converting from ascii (int) to char.
                if(primaryCode==35){
                    candidateview.getNextCandidates();
                    return;
                }
                if ((!Character.isDigit(primaryCode))) { //if is not digit, just display it.
                    if((last_char==46 || last_char==44 || last_char==63) && (System.currentTimeMillis()-last_input_seconds)<1000){
                        inputconnection.deleteSurroundingText(1, 0);
                    }
                    last_char=primaryCode;
                    last_input_seconds = System.currentTimeMillis();
                    inputconnection.commitText(String.valueOf(code), 1);
                    ClearSugestions();
                    return;
                } else {
                    candidateview.updateSugestions(Character.getNumericValue(code));
                }
        }
    }
}
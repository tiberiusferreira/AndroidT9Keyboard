package com.example.tiberio.project;


import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import java.util.ArrayList;

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


    private boolean caps = false;


    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
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
    public View onCreateCandidatesView(){
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

    public void InputText(String input){
        /*Method that pushes text to the user's text field.
        * It also adds a space after the text so the next
        * word can be written immediately*/
        input=input.concat(" ");
        InputConnection ic = getCurrentInputConnection();
        ic.commitText(input,input.length());
    }
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        //playClick(primaryCode);
        //System.out.format("PrimaryCODE="+String.valueOf(primaryCode)+"\n\n");
        //List<String> messages = Arrays.asList("Hello", "World!", "How", "Are", "You");

        //candidateview.setSuggestions(messages,true,true);
        if(AllCombinations.size()==0){
            AllCombinations.add("");
        }
        ArrayList<String> temp = new ArrayList<>();
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                AllCombinations.clear();
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                keyboardview.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                AllCombinations.clear();
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char)primaryCode;
                if((!Character.isDigit(primaryCode) || code=='1' || code=='0')){
                    //ic.commitText(String.valueOf(primaryCode),1);
                    ic.commitText(String.valueOf(code), 1);
                    AllCombinations.clear();
                    return;
                }
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                for(String a : AllCombinations ){
                    for(String Three : (candidateview.getAllCombination(a, primaryCode - '0'))){
                        if(candidateview.IsDeadEnd(Three)==0){
                            temp.add(Three);
                        }

                    }
                }
                AllCombinations.clear();
                for(String All : temp){
                    AllCombinations.add(All);
                }
                /*send key to get a completion*/
                candidateview.Candidates= candidateview.GetThreeBestCandidates(AllCombinations,2);
                candidateview.invalidate();
                //current_word=current_word.concat(String.valueOf(primaryCode-'0'));
                //ic.commitCompletion(new CompletionInfo(0, 0, "ok"));
                //ic.commitText(String.valueOf(code), 1);
        }
    }


}
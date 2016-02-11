package com.example.tiberio.project;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by tiberio on 07/02/2016.
 */
interface SuggestionsInterface {
    public void clearSuggestion();
    public void setDictionary(InputStream dictionary);
    /*Updates the sugestions based on the typed number in the virtual keyboard.
    * Returns the new suggestions.*/
    public ArrayList<String> updateSuggestions(int typed_number);
    /*Returns all combinations of the string 'already_typed' and the new key press 'number' which
    represents 3 or 4 letters */
    public ArrayList<String> getAllCombination(String already_typed, int number);
    /* Returns the best candidates based on the word candidates 'combinations'. Depth is an optional
    parameter which can be used internally (if function is recursive) */
    public ArrayList<String> GetBestCandidates(ArrayList<String> combinations,int depth);
}

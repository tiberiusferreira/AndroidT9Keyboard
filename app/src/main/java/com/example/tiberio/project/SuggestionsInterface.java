package com.example.tiberio.project;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by tiberio on 07/02/2016.
 */
interface SuggestionsInterface {
    public void clearSuggestion();
    public void setDictionary(InputStream dictionary);
    public ArrayList<String> updateSuggestions(int typed_number);
    public ArrayList<String> getAllCombination(String already_typed, int number);
    public ArrayList<String> GetBestCandidates(ArrayList<String> combinations,int depth);
}

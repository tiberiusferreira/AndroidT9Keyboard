package com.example.tiberio.project;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by tiberio on 07/02/2016.
 */
interface DictionaryInterface {
    /*Takes InputStream and uses it to read an .xls file and construct it's dictionary*/
    public void create_dictionary (InputStream dic_stream);
    /*0 is the string 'start' is prefix of other words, 1 otherwise.*/
    public int isDeadEnd(String start);
    /*Returns similar words up to a depth of 'depth'. Depth means how many more characters the word
    can have in comparison to the original word 'start' */
    public ArrayList<String> get_similar_word_depth(String start, int depth);
    /*Return 1 if the 'word' is in the dictionary, 0 otherwise*/
    public double isWord(String word);
}

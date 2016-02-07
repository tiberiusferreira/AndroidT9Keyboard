package com.example.tiberio.project;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by tiberio on 07/02/2016.
 */
interface DictionaryInterface {
    public void create_dictionary (InputStream dic_stream);
    public int isDeadEnd(String start);
    public ArrayList<String> get_similar_word_depth(String start, int depth);
    public double isWord(String word);
}

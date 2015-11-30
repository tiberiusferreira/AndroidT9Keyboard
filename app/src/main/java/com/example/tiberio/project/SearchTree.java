package com.example.tiberio.project;


import java.io.InputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by tiberio on 19/11/2015.
 */


public class SearchTree {
    private Node root;
    ArrayList<String> WordSugestions = new ArrayList<>();
    SearchTree(){
        root = null;
    }
    private class Node{
        private char c;
        private Node left , mid, right;
        private double frequency=0;
    }
    public void print_tree(){
        System.out.format("Printing Tree \n\n");
        print_tree(root);
    }
    public ArrayList<String> get_similar_word(String start){
        WordSugestions.clear();
        if(start==null || start.isEmpty()){
            return WordSugestions;
        }
        return get_similar_word_internal(root, start, 0);
    }
    public int isDeadEnd(String start){
        if(start==null || start.isEmpty()){
            return 1;
        }
        return isDeadEnd_internal(root, start, 0);
    }
    public ArrayList<String> get_similar_word_depth(String start, int depth){
        WordSugestions.clear();
        if(start==null || start.isEmpty()){
            return WordSugestions;
        }
        //System.out.format("Got to get_similar_word_depth\n");
        System.out.flush();
        return get_similar_word_internal_depth(root, start, 0, depth);
    }
    private ArrayList<String> GetAllSugestions(Node node, String str, int came_from_mid, char prev, double prev_freq){
        /* Prints all the string that descend from the given node */
        if(node==null) //if gets to a null node
        {
            return WordSugestions;
        }
        /* A char is only part of the current string if I went down from it.
         * Also if the previous char was end of a word (prev_freq>0) it was
         * already printed and it's the char added. So don't add it and mark
         * that prev_freq is 0 for the next node.*/
        if(came_from_mid==1 && prev_freq<0.1){
            str=str.concat(String.valueOf(prev));
        }else {
            prev_freq=0;
        }
        /* If this node is the end of a word, add it's char, print the word,
        * and mark that it was the end of a word (prev_freq>0)*/
        if(node.frequency>0){
            str=str.concat(String.valueOf(node.c));
            WordSugestions.add(str);
            //System.out.format(str + "\n");
            prev_freq=node.frequency;
        }
        GetAllSugestions(node.mid, str, 1, node.c, prev_freq);
        /* If going to left or right, do not take into account the char added
        * when printing this node's word. This is because when going left or
        * right it means the word being created does not have this node's char
        * in it in this position.*/
        if(!str.isEmpty() && node.frequency>0){
            str=str.substring(0,str.length()-1);
        }
        GetAllSugestions(node.left, str, 0, node.c, prev_freq);
        GetAllSugestions(node.right, str, 0, node.c, prev_freq);
        return WordSugestions;
    }
    private ArrayList<String> GetAllSugestions(Node node, String str, int came_from_mid, char prev, double prev_freq,int depth){
        /* Prints all the string that descend from the given node */
        //System.out.format("Depth = "+ depth+ "\n\n");
        System.out.flush();
        if(node==null || depth<0) //if gets to a null node
        {
            return WordSugestions;
        }
        /* A char is only part of the current string if I went down from it.
         * Also if the previous char was end of a word (prev_freq>0) it was
         * already printed and it's the char added. So don't add it and mark
         * that prev_freq is 0 for the next node.*/
        if(came_from_mid==1 && prev_freq<0.1){
            str=str.concat(String.valueOf(prev));
        }else {
            prev_freq=0;
        }
        /* If this node is the end of a word, add it's char, print the word,
        * and mark that it was the end of a word (prev_freq>0)*/
        if(node.frequency>0){
            str=str.concat(String.valueOf(node.c));
            WordSugestions.add(str);
            //System.out.format(str + "\n");
            prev_freq=node.frequency;
        }
        GetAllSugestions(node.mid, str, 1, node.c, prev_freq,depth-1);
        /* If going to left or right, do not take into account the char added
        * when printing this node's word. This is because when going left or
        * right it means the word being created does not have this node's char
        * in it in this position.*/
        if(!str.isEmpty() && node.frequency>0){
            str=str.substring(0,str.length()-1);
        }
        GetAllSugestions(node.left, str, 0, node.c, prev_freq,depth-1);
        GetAllSugestions(node.right, str, 0, node.c, prev_freq,depth-1);
        return WordSugestions;
    }
    public double isWord(String word){
        if(word==null || word.isEmpty()){
            return 0;
        }
        word=word.toLowerCase();
        return isWord_internal(root,word,0);
    }

        public double isWord_internal(Node node, String word, int current_word_pos){
        /*Verifies if a word given is in dictonary, if is, returns it's frequency.*/
        char current_char=word.charAt(current_word_pos);
        /*If at some points gets to a null node, means the semi word given does not exist in the
        * tree. Return WordSugestions, which should be empty. */
        if (node == null) {
            //System.out.format("Not found\n");
            return 0;
        }
        /*If the node we are looking has a char greater than the one we want, go left.
        * If it has a smaller one, go right. Else, if word end of word, go middle.
        * If it is end of word, call the function to get the suggestions*/
        if (node.c > current_char) { //go left
            return isWord_internal(node.left, word, current_word_pos);
        }else if (node.c < current_char) { //go right
            return isWord_internal(node.right, word, current_word_pos);
        }else if (current_word_pos<word.length()-1) { //char is equal to the searched one
            return isWord_internal(node.mid, word, current_word_pos + 1);
        }else{ //if finished going throught string, get possible completions
            //System.out.format("Found "+node.c+"\n");
            return node.frequency;
        }
    }


    public ArrayList<String> get_similar_word_internal(Node node, String start, int current_word_pos){
        /*Gets to the node in the tree which corresponds to the last char in the semi word given
        * (start) and from there calls another function to get all the words that have the given
        * semi word as beginning.*/
        char current_char=start.charAt(current_word_pos);
        /*If at some points gets to a null node, means the semi word given does not exist in the
        * tree. Return WordSugestions, which should be empty. */
        if (node == null) {
            System.out.format("Not found\n");
            return WordSugestions;
        }
        /*If the node we are looking has a char greater than the one we want, go left.
        * If it has a smaller one, go right. Else, if word end of word, go middle.
        * If it is end of word, call the function to get the suggestions*/
        if (node.c > current_char) { //go left
            return get_similar_word_internal(node.left, start, current_word_pos);
        }else if (node.c < current_char) { //go right
            return get_similar_word_internal(node.right, start, current_word_pos);
        }else if (current_word_pos<start.length()-1) { //char is equal to the searched one
            return get_similar_word_internal(node.mid, start, current_word_pos + 1);
        }else{ //if finished going throught string, get possible completions
            return GetAllSugestions(node.mid, start, 0, ' ', 0);
        }
    }



    public int isDeadEnd_internal(Node node, String start, int current_word_pos){
        /*Gets to the node in the tree which corresponds to the last char in the semi word given
        * (start) and from there calls another function to get all the words that have the given
        * semi word as beginning.*/
        char current_char=start.charAt(current_word_pos);
        /*If at some points gets to a null node, means the semi word given does not exist in the
        * tree. Return WordSugestions, which should be empty. */
        if (node == null) {
            System.out.format("Not found\n");
            return 1;
        }
        /*If the node we are looking has a char greater than the one we want, go left.
        * If it has a smaller one, go right. Else, if word end of word, go middle.
        * If it is end of word, call the function to get the suggestions*/
        if (node.c > current_char) { //go left
            return isDeadEnd_internal(node.left, start, current_word_pos);
        }else if (node.c < current_char) { //go right
            return isDeadEnd_internal(node.right, start, current_word_pos);
        }else if (current_word_pos<start.length()-1) { //char is equal to the searched one
            return isDeadEnd_internal(node.mid, start, current_word_pos + 1);
        }else{ //if finished going throught string, get possible completions
            return 0;
        }
    }

    public ArrayList<String> get_similar_word_internal_depth(Node node, String start, int current_word_pos,int depth){
        /*Gets to the node in the tree which corresponds to the last char in the semi word given
        * (start) and from there calls another function to get all the words that have the given
        * semi word as beginning.*/
        char current_char=start.charAt(current_word_pos);
        /*If at some points gets to a null node, means the semi word given does not exist in the
        * tree. Return WordSugestions, which should be empty. */
        if (node == null) {
            System.out.format("Not found\n");
            return WordSugestions;
        }
        /*If the node we are looking has a char greater than the one we want, go left.
        * If it has a smaller one, go right. Else, if word end of word, go middle.
        * If it is end of word, call the function to get the suggestions*/
        if (node.c > current_char) { //go left
            return get_similar_word_internal_depth(node.left, start, current_word_pos,depth);
        }else if (node.c < current_char) { //go right
            return get_similar_word_internal_depth(node.right, start, current_word_pos,depth);
        }else if (current_word_pos<start.length()-1) { //char is equal to the searched one
            return get_similar_word_internal_depth(node.mid, start, current_word_pos + 1, depth);
        }else{ //if finished going throught string, get possible completions
            //System.out.format("DepthBefore!\n");
            System.out.flush();
            return GetAllSugestions(node.mid, start, 0, ' ', 0,depth);
        }
    }
    public void print_tree(Node node){
        if(node==null || node.c=='~'){
            return;
        }
        System.out.format("Node "+node.c+" Freq = " +node.frequency+ ".\n");
        if(node.left!=null){
            System.out.format("Left of " + node.c + " = " + node.left.c + "\n");

        }
        if(node.mid!=null){
            System.out.format("Middle of " + node.c + " = "+ node.mid.c + "\n");

        }
        if(node.right!=null){
            System.out.format("Right of " + node.c + " = "+ node.right.c + "\n\n");

        }
        print_tree(node.left);
        print_tree(node.mid);
        print_tree(node.right);

    }
    public void insert_word(String new_word, double frequency){
        //System.out.format("OK!");
        root=insert_word_internally(root,new_word,frequency,0);
    }
    private Node insert_word_internally(Node node,String new_word, double frequency, int current_word_pos) {
        char current_char=new_word.charAt(current_word_pos);
        if (node == null) {
            node=new Node();
            node.c=current_char;
        }
        if (node.c > current_char) { //go left
            node.left=insert_word_internally(node.left, new_word, frequency, current_word_pos);

        } else if (node.c < current_char) { //go right
            node.right=insert_word_internally(node.right, new_word, frequency, current_word_pos);
        }else if (current_word_pos<new_word.length()-1) {
            node.mid=insert_word_internally(node.mid, new_word, frequency, current_word_pos+1);
        }else{
            node.frequency=frequency;
            return node;
        }

        return node;

    }

    public void create_dictionary (InputStream dic_stream){
        try {
            /*This is not easy for me to do, but after long hours trying to find a clean solution
            * Android does not allow POI xlsx's libraries to be imported for various reasons ranging
            * from limit of number of methods to Gradle detox errors.*/

            //POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(dic_file));
            HSSFWorkbook wb = new HSSFWorkbook(dic_stream);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;
            int rows = sheet.getPhysicalNumberOfRows();
            int column=0;
            for(int r = 0; r < rows; r++) {
                row = sheet.getRow(r);
                if(row != null) {
                    cell = row.getCell(column);
                    if(cell != null) {
                        this.insert_word(cell.getStringCellValue().trim(), (row.getCell(column + 1)).getNumericCellValue());
                    }

                }
            }
        } catch(Exception ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }




}

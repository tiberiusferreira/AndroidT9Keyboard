package com.example.tiberio.project;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by tiberio on 30/11/2015.
 */
public class SuggestionsGenerator {
    ArrayList<String> AllCombinations = new ArrayList<>();
    SearchTree tree = new SearchTree();
    ArrayList<String> Candidates = new ArrayList<>();
    private class final_candidates{
        double frequency;
        String word;
    }
    ArrayList<final_candidates> FINAL = new ArrayList<>();
    public static Comparator<final_candidates> FinalCandidatesComparator
            = new Comparator<final_candidates>() {
        @Override
        public int compare(final_candidates p1, final_candidates p2) {
            if (p1.frequency > p2.frequency)
                return -1;
            else return 1;
        }
    };

    public void clearSugestion(){
        AllCombinations.clear();
        Candidates.clear();
    }
    public void setDictionary(InputStream dictionary){
        tree.create_dictionary(dictionary);
    }
    public ArrayList<String> updateSugestions(int typed_number){
        ArrayList<String> temp_newAllcombinations = new ArrayList<>();
        /*The key pressed can be from three to four characters. For each of those, combine
        * with all previously already registered combinations. For example, if 2 is typed,
        * it can mean A, B or C. If previous combination was BCE, the new combinations are
        * BCEA, BCEB, BCEC. However, if one combination leads to a deadend, which means
        * no word can be formed from it, discard it. Example: cak is not a dead end, can be
        * cake. But bce is a dead end.*/
        /*The next if prevents skipping the next for and not adding new combinations*/
        if(AllCombinations.isEmpty()){
            AllCombinations.add("");
        }
        for(String allcombinations : AllCombinations){
            for(String newcombination : (getAllCombination(allcombinations,typed_number))){
                if(IsDeadEnd(newcombination)==0){
                    temp_newAllcombinations.add(newcombination);
                }
            }
        }
        AllCombinations.clear();
        for(String All : temp_newAllcombinations){
            AllCombinations.add(All);
        }
        temp_newAllcombinations.clear();
        System.out.println("All combinations=" + AllCombinations.toString());
        Candidates = GetBestCandidates(AllCombinations, 2);
        return Candidates;
    }

    public int IsDeadEnd(String word){
        return tree.isDeadEnd(word);
    }

    public ArrayList<String> getAllCombination(String already_typed, int number){
        /* Receives a string and number and returns an array of Strings which are
        *  the combinations of the string plus the chars possible for the number.
        *  For example if already_typed = ab and number = 2, returns aba, abb, abc.*/
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
        return combinations;
    }

    public ArrayList<String> GetBestCandidates(ArrayList<String> combinations,int depth){
        /*This method receives many character combinations, filters the words among them and
         * returns the 3 with highest frequency. If there are not even 3 words among the
         * combinations it tries to get words that are similar to the combinations given.
         * The depth means how deep into the search tree should it look for similar words.
         * Similar here means words that have the as prefix the characters typed by the user.
         * So if TH is one combination typed by the user, depth 1 means THE is accepted as
         * suggestion, and depth 3 means  THERE  is also accepted.*/
        FINAL.clear();
        ArrayList<String> BestCandidates = new ArrayList<>();

        for(String currentString : combinations){
            double currentString_frequency;
            currentString_frequency=tree.isWord(currentString);
            if(currentString_frequency>0) {
                final_candidates new_final=new final_candidates();
                new_final.frequency=currentString_frequency;
                new_final.word=currentString;
                FINAL.add(new_final);
                System.out.format("Found the word "+ currentString + " among the combinations\n");
            }
        }
        /*If there are less than 3 candidates, mark them with the highest priority (frequency)
        * so they are the first suggestions and search for similar candidates (with higher lenght)*/
        if(FINAL.size()<3){
            for(int i=0;i<FINAL.size();i++){
                FINAL.get(i).frequency=Double.MAX_VALUE-i;
            }
            for(String currentString : combinations){
                for(String new_words : tree.get_similar_word_depth(currentString,depth)){
                    final_candidates new_final=new final_candidates();
                    new_final.frequency=tree.isWord(new_words);
                    new_final.word=new_words;
                    FINAL.add(new_final);
                }
            }
        }
        //Sort candidates by frequency.
        Collections.sort(FINAL, FinalCandidatesComparator);
        System.out.println("FINAL IS THE FOLLOWING= ");
        for(int i=0;i<FINAL.size();i++){
            System.out.println(FINAL.get(i).word + " "+ FINAL.get(i).frequency);
            BestCandidates.add(i,FINAL.get(i).word);
        }
        while(BestCandidates.size()<3){
            BestCandidates.add("");
        }
        return BestCandidates;
    }
}

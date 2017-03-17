package com.company;
import java.util.*;
/**
 * Created by YeJiang on 3/16/17.
 */
public class QueryManager {
    // id_map -->  <id,  [playID, sceneID] >
    private Map<Integer, String[]> Id_map;
    private Map<String,Map<Integer,List<Integer>>> InvertedList;

    public QueryManager(Map<Integer, String[]> Id_map , Map<String,Map<Integer,List<Integer>>> InvertedList){
        this.Id_map = Id_map;
        this.InvertedList = InvertedList;
    }

    public List<String> findScenesWithTerm(String input){
        String term = input.toLowerCase();
        Map<Integer,List<Integer>> posList = InvertedList.get(term);
        List<Integer> idNums = new ArrayList<>();
        for(Integer id : posList.keySet()){
            idNums.add(id);
        }

        Set<String> result = new HashSet<>();
        for(Integer ID : idNums){
            result.add(Id_map.get(ID)[1]);
        }
        return new ArrayList<>(result);
    }

    public List<String> findPlaysWithTerm(String input){
        String term = input.toLowerCase();
        Map<Integer,List<Integer>> posList = InvertedList.get(term);
        List<Integer> idNums = new ArrayList<>();
        for(Integer id : posList.keySet()){
            idNums.add(id);
        }

        Set<String> result = new HashSet<>();
        for(Integer ID : idNums){
            result.add(Id_map.get(ID)[0]);
        }
        return new ArrayList<>(result);
    }

    public Map<Integer,Integer> findTermFrequencyEachScene(String term){
        Map<Integer,Integer> result = new HashMap<>();
        for(Integer key : Id_map.keySet()){
            result.put(key,0);
        }

        Map<Integer,List<Integer>> posList = InvertedList.get(term);
        for(Integer id : posList.keySet()){
            result.put(id, posList.get(id).size());
        }
        return result;
    }

    public List<String> findScenesWithPhrase(String phrase){
        String[] terms = phrase.split("\\s+");
        Stack<Map<Integer,List<Integer>>> stack = new Stack<>();
        stack.push(InvertedList.get(terms[0]));

        for(int i=1 ; i<terms.length ; i++){
            Map<Integer,List<Integer>> newMatch = findMatch(stack.peek(),InvertedList.get(terms[i]));
            stack.push(newMatch);
        }
        Map<Integer,List<Integer>> result = stack.peek();
        Set<String> set = new HashSet<>();
        for(Integer id : result.keySet()){
            set.add(Id_map.get(id)[1]);
        }
        return new ArrayList<>(set);
    }

    private Map<Integer,List<Integer>> findMatch(Map<Integer,List<Integer>>  left, Map<Integer,List<Integer>> right){
        Map<Integer,List<Integer>> result = new HashMap<>();
        for(Map.Entry<Integer, List<Integer>> entry : left.entrySet()){
            int id = entry.getKey();
            List<Integer> leftPositions = entry.getValue();
            List<Integer> rightPositions = right.get(id);
            if(rightPositions != null){
                for(Integer leftPos : leftPositions){
                    if(rightPositions.contains(leftPos+1)){
                        List<Integer> newList = result.get(id);
                        if(newList == null)newList = new ArrayList<>();
                        newList.add(leftPos+1);
                        result.put(id, newList);
                    }
                }
            }
        }
        return result;
    }



}

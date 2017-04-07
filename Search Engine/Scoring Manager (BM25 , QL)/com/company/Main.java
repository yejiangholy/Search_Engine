package com.company;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;

public class Main {

    public static void main(String[] args) {

        try {
            String content = new String(Files.readAllBytes(Paths.get("/Users/YeJiang/Desktop/Course/446/data.json")));
            JSONObject root = new JSONObject(content);
            JSONArray corpus = root.getJSONArray("corpus");

            //data structures
            // id_map -->  <id,  [playID, sceneID] >
            Map<Integer,String[]> Id_map = new HashMap<>();
            Map<String, Map<Integer,List<Integer>>> InvertedList = new HashMap<>();
            Map<Integer, String> docs = new HashMap<>();
            double avagLen = 0;
            BigInteger collectionLen = BigInteger.ZERO;

            for(int i=0 ; i<corpus.length();i++){
                JSONObject scene = corpus.getJSONObject(i);
                // put Id number into Id_map
                int id = scene.getInt("sceneNum");
                if(!Id_map.containsKey(id)){
                    String[] play_scene = {scene.getString("playId"),scene.getString("sceneId")};
                    Id_map.put(id,play_scene);
                }
                docs.put(id,scene.getString("text"));
                //process the text into invertedList
                String[] terms = scene.getString("text").split("\\s+");
                BigInteger len = BigInteger.valueOf(new Long(terms.length));
                collectionLen = collectionLen.add(len);
                int pos = 0;
                for(String term : terms){
                    Map<Integer,List<Integer>> posList = InvertedList.get(term);
                    if(posList == null)posList = new HashMap<>();
                    List<Integer> curList = posList.get(id);
                    if(curList == null) curList = new ArrayList<>();
                    curList.add(pos++);
                    posList.put(id,curList);
                    InvertedList.put(term,posList);
                }
            }
            // ----------------------- ScoringManager start to take queries -------------------------------
            avagLen = collectionLen.divide(BigInteger.valueOf(Id_map.size())).doubleValue();
            ScoringManager manager = new ScoringManager(Id_map, InvertedList,avagLen,collectionLen);
            String Q1 = "the king queen royalty";
            String Q2 = "servant guard soldier";
            String Q3 = "hope dream sleep";
            String Q4 = "ghost spirit";
            String Q5 = "fool jester player";
            String Q6 = "to be or not to be";

            // BM25 scores for reach query
            Map<Integer,Double> Q1_PM25scores = manager.getBM25scores(docs,Q1);
            Map<Integer,Double> Q2_PM25scores = manager.getBM25scores(docs,Q2);
            Map<Integer,Double> Q3_PM25scores = manager.getBM25scores(docs,Q3);
            Map<Integer,Double> Q4_PM25scores = manager.getBM25scores(docs,Q4);
            Map<Integer,Double> Q5_PM25scores = manager.getBM25scores(docs,Q5);
            Map<Integer,Double> Q6_PM25scores = manager.getBM25scores(docs,Q6);
            manager.sortAndReport(Q1_PM25scores,"BM25","Q1");
            manager.sortAndReport(Q2_PM25scores,"BM25","Q2");
            manager.sortAndReport(Q3_PM25scores,"BM25","Q3");
            manager.sortAndReport(Q4_PM25scores,"BM25","Q4");
            manager.sortAndReport(Q5_PM25scores,"BM25","Q5");
            manager.sortAndReport(Q6_PM25scores,"BM25","Q6");

            // QL scores for each query
//            Map<Integer,Double> Q1_LMscores = manager.getLanguageModelScores(docs,Q1);
//            Map<Integer,Double> Q2_LMscores = manager.getLanguageModelScores(docs,Q2);
//            Map<Integer,Double> Q3_LMscores = manager.getLanguageModelScores(docs,Q3);
//            Map<Integer,Double> Q4_LMscores = manager.getLanguageModelScores(docs,Q4);
//            Map<Integer,Double> Q5_LMscores = manager.getLanguageModelScores(docs,Q5);
//            Map<Integer,Double> Q6_LMscores = manager.getLanguageModelScores(docs,Q6);
//            manager.sortAndReport(Q1_LMscores,"ql","Q1");
//            manager.sortAndReport(Q2_LMscores,"ql","Q2");
//            manager.sortAndReport(Q3_LMscores,"ql","Q3");
//            manager.sortAndReport(Q4_LMscores,"ql","Q4");
//            manager.sortAndReport(Q5_LMscores,"ql","Q5");
//            manager.sortAndReport(Q6_LMscores,"ql","Q6");


            // judgement logic for Q3
//            Map<String,Integer> Q3_estimate = new HashMap<>();
//            for(Integer id : Id_map.keySet()){
//                Double BM25 = Q3_PM25scores.get(id);
//                Double ql = Q3_LMscores.get(id);
//                if(BM25 >= 4.5){
//                    Q3_estimate.put(Id_map.get(id)[1],3);
//                }
//                else if(ql >= -20.8){
//                    Q3_estimate.put(Id_map.get(id)[1],3);
//                }
//                else if(BM25 >= 4.2 && ql >= -21.5){
//                    Q3_estimate.put(Id_map.get(id)[1],3);
//                }
//                else if(ql >= -22 || BM25 >= 4.1 ){
//                    Q3_estimate.put(Id_map.get(id)[1],2);
//                }
//                else if(BM25 >= 3.3 || ql >= -23) {
//                    Q3_estimate.put(Id_map.get(id)[1], 2);
//                }
//                else if(ql >= -25){
//                    Q3_estimate.put(Id_map.get(id)[1],1);
//                }
//                else{
//                    Q3_estimate.put(Id_map.get(id)[1],0);
//                }
//            }
//
//            List<Map.Entry<String,Integer>> estimation = new ArrayList<>();
//            for(Map.Entry<String,Integer> entry : Q3_estimate.entrySet())estimation.add(entry);
//
//           Collections.sort(estimation, new Comparator<Map.Entry<String, Integer>>() {
//               @Override
//               public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                   return Integer.compare(o2.getValue(),o1.getValue());
//               }
//           });
//
//            for(Map.Entry<String,Integer> entry : estimation){
//                System.out.println(entry.getKey() + "     "+entry.getValue());
//            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

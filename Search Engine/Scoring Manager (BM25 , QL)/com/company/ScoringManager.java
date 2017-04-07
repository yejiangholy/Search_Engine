package com.company;
import java.math.BigInteger;
import  java.util.*;

/**
 * Created by YeJiang on 3/31/17.
 */
public class ScoringManager {
    // id_map -->  <id,  [playID, sceneID] >
    private Map<Integer,String[]> Id_map;
    private Map<String,Map<Integer,List<Integer>>> InvertedList;
    private double averageLen;
    BigInteger collectionLen;

    public ScoringManager(Map<Integer, String[]> Id_map , Map<String,Map<Integer,List<Integer>>> InvertedList, double averageLen, BigInteger collectionLen){
        this.Id_map = Id_map;
        this.InvertedList = InvertedList;
        this.averageLen = averageLen;
        this.collectionLen = collectionLen;
    }

    // method 1 : generate all BM25 score for each document
    public Map<Integer,Double> getBM25scores(Map<Integer,String> docs, String query){

        Map<Integer,Double> scoresMap = new HashMap<>();

        for(Map.Entry<Integer,String> entry : docs.entrySet()){
            double score = BM25score(entry.getKey(),entry.getValue(),query);
            if(score != 0){
                scoresMap.put(entry.getKey(),score);
            }
        }
        return scoresMap;
    }

    // method 2 : generate all LanguageModel score for each document
    public Map<Integer,Double> getLanguageModelScores(Map<Integer,String> docs, String query){
        Map<Integer,Double> scoresMap = new HashMap<>();

        for(Map.Entry<Integer,String> entry : docs.entrySet()){
            scoresMap.put(entry.getKey(), LanguageModelScore(entry.getKey(),entry.getValue(),query));
        }
        return scoresMap;
    }

    private double LanguageModelScore(int docId, String doc, String query){
        String[] docWords = doc.split("\\s+");
        double docLen = docWords.length;
        int mu = 1000;
        double value = 0;
        String[] queryTerms = query.split("\\s+");

        for(String term : queryTerms){
            Map<Integer,List<Integer>> posMap = InvertedList.get(term);
            if(posMap == null)continue;
            List<Integer> curPosList = posMap.get(docId);
            double fqiD = 0;
            if(curPosList != null)fqiD = curPosList.size();
            int cqi = 0;
            for(List<Integer> posList : posMap.values()){
                cqi += posList.size();
            }
            if(cqi == 0 && fqiD ==0)continue;
            double muCqi =mu*cqi;
            double collectionTerm = muCqi/collectionLen.doubleValue();
            value += Math.log((fqiD + collectionTerm)/(docLen + mu));
        }
        return value;
    }

    private double BM25score(int docId,String doc, String query){
        String[] docTerms = doc.split("\\s+");
        double docLen = docTerms.length;
        double r = 0 ,R = 0;
        double k1 = 0.9 , k2 = 90;
        double b = 0.75;
        double k = k1*((1-b) + b*(docLen/averageLen));
        int N = Id_map.size();
        double score = 0;
        String[] queryTerms = query.split("\\s+");
        Map<String,Integer> queryTermFrequency = getTermFre(queryTerms);

        for(String term : queryTerms){
            int qfi = queryTermFrequency.get(term.toLowerCase());
            Map<Integer,List<Integer>> posList = InvertedList.get(term);
            if(posList == null)continue;
            List<Integer> curPosList = posList.get(docId);
            if(curPosList == null)continue;
            int fi = curPosList.size();
            int ni = posList.size();
            double part1 = ((r+0.5)/(R-r+0.5))/((ni-r+0.5)/(N-ni-R+r+0.5));
            double part2 = ((k1+1)*fi)/(k+fi);
            double part3 = ((k2 + 1)*qfi)/(k2 + qfi);
            double value = (Math.log(part1))*part2*part3;
            score += value;
        }
        return score;
    }

    private Map<String , Integer> getTermFre(String[] terms){
        Map<String, Integer> freqMap = new HashMap<>();
        for(String t : terms){
            Integer num = freqMap.get(t.toLowerCase());
            if(num == null){
                freqMap.put(t.toLowerCase(),1);
            }else{
                freqMap.put(t.toLowerCase(),num+1);
            }
        }
        return freqMap;
    }

    public void sortAndReport(Map<Integer,Double> socresMap, String model, String question){
       List<Map.Entry<Integer,Double>>  list = new ArrayList<>();
        int rank = 1;

        for(Map.Entry<Integer,Double> entry : socresMap.entrySet())list.add(entry);

        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return Double.compare(o2.getValue(),o1.getValue());
            }
        });
        for(Map.Entry<Integer,Double> entry : list){
            System.out.println(question+"  "+"skip" +"  "+Id_map.get(entry.getKey())[1] + "    "+ (rank++) + "   "+entry.getValue()+"  "+"yejiang"+"-"+model);
        }
    }
}

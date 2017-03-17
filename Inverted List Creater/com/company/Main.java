package com.company;
import org.json.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        //    Building the InvertedList from the json data
        try {
            boolean query = false;
            boolean calculateLen = false;
            String content = new String(Files.readAllBytes(Paths.get("/Users/YeJiang/Desktop/Course/446/data.json")));
            JSONObject root = new JSONObject(content);
            JSONArray corpus = root.getJSONArray("corpus");

            //  data structures
            // id_map -->  <id,  [playID, sceneID] >
            Map<Integer, String[]> ID_map = new HashMap<>();
            // InvertedList
            Map<String,Map<Integer,List<Integer>>> InvertedList = new HashMap<>();
            List<Integer> scenesLen = new ArrayList<>();
            Integer minSceneLen = Integer.MAX_VALUE;

            for(int i=0 ;i<corpus.length();i++){
                JSONObject scene = corpus.getJSONObject(i);
                // put ID number into ID_map
                int id = scene.getInt("sceneNum");
                if(!ID_map.containsKey(id)){
                    String[] play_scene = new String[2];
                    play_scene[0] = scene.getString("playId");
                    play_scene[1] = scene.getString("sceneId");
                    ID_map.put(id,play_scene);
                }
                // process the current text string
                String[] terms = scene.getString("text").split("\\s+");
                int len = terms.length;
                scenesLen.add(len);
                if(len < minSceneLen)minSceneLen = len;
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
            QueryManager manager = new QueryManager(ID_map, InvertedList);

            if(calculateLen) {
                double preLen = scenesLen.get(0);
                for (int i = 1; i < scenesLen.size(); i++) {
                    preLen = (preLen + scenesLen.get(i)) / 2;
                }
                System.out.println("average length of scene is " + preLen);
                System.out.println("min  length of scene is " + minSceneLen);
            }
            System.out.println("Inverted List constructed and QueryManager ready to take query");

            // ---------------Start to take care of queries with QueryManager ------------------\\

            if(query) {
                //1. Find scene(s) where "thee" or "thou" is used more than "you"
                Map<Integer, Integer> thee_frequencies = manager.findTermFrequencyEachScene("thee");
                Map<Integer, Integer> thou_frequencies = manager.findTermFrequencyEachScene("thou");
                Map<Integer, Integer> you_frequencies = manager.findTermFrequencyEachScene("you");
                Set<Integer> resId = new HashSet<>();
                for (int i = 0; i <= 747; i++) {
                    if (thee_frequencies.get(i) > you_frequencies.get(i) || thou_frequencies.get(i) > you_frequencies.get(i)) {
                        resId.add(i);
                    }
                }
                List<String> query1 = new ArrayList<>();
                for (Integer id : resId) query1.add(ID_map.get(id)[1]);
                System.out.println("1. Find scene(s) where \"thee\" or \"thou\" is used more than \"you\"");
                Set<String> treeSet = new TreeSet<>(query1);
                for (String res : treeSet) System.out.println(res);
                System.out.println();

                // 2. Find scene(s) where Verona, Rome, or Italy is mentioned.
                List<String> Verona_scenes = manager.findScenesWithTerm("Verona");
                List<String> Rome_scenes = manager.findScenesWithTerm("Rome");
                List<String> Italy_scenes = manager.findScenesWithTerm("Italy");
                Set<String> query2 = new HashSet<>();
                query2.addAll(Verona_scenes);
                query2.addAll(Rome_scenes);
                query2.addAll(Italy_scenes);
                System.out.println("2. Find scene(s) where Verona, Rome, or Italy is mentioned.");
                treeSet = new TreeSet<>(query2);
                for (String s : treeSet) System.out.println(s);

                // 3. Find the play(s) where "falstaff" is mentioned.
                List<String> falstaff_list = manager.findPlaysWithTerm("falstaff");
                System.out.println();
                System.out.println("3. Find the play(s) where \"falstaff\" is mentioned.");
                treeSet = new TreeSet<>(falstaff_list);
                for (String s : treeSet) System.out.println(s);

                // 4. Find the play(s) where "soldier" is mentioned.
                List<String> soldier_list = manager.findPlaysWithTerm("soldier");
                System.out.println();
                System.out.println("4. Find the play(s) where \"soldier\" is mentioned.");
                treeSet = new TreeSet<>(soldier_list);
                for (String s : treeSet) System.out.println(s);

                // 5. Find scene(s) where "lady macbeth" is mentioned.
                List<String> phraseQ1 = manager.findScenesWithPhrase("lady macbeth");
                System.out.println();
                System.out.println("5. Find scene(s) where \"lady macbeth\" is mentioned.");
                treeSet = new TreeSet<>(phraseQ1);
                for (String s : treeSet) System.out.println(s);

                //6. Find the scene(s) where "a rose by any other name" is mentioned.
                List<String> phraseQ2 = manager.findScenesWithPhrase("a rose by any other name");
                System.out.println();
                System.out.println("6. Find the scene(s) where \"a rose by any other name\" is mentioned.");
                treeSet = new TreeSet<>(phraseQ2);
                for (String s : treeSet) System.out.println(s);

                //7. Find the scene(s) where "cry havoc" is mentioned.
                List<String> phraseQ3 = manager.findScenesWithPhrase("cry havoc");
                System.out.println();
                System.out.println("7. Find the scene(s) where \"cry havoc\" is mentioned.");
                treeSet = new TreeSet<>(phraseQ3);
                for (String s : treeSet) System.out.println(s);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

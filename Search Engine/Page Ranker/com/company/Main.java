package com.company;
import java.io.FileInputStream;
import java.util.*;

public class Main {

    public static void calculate(Graph graph)
    {
        int size = graph.nodes.size();
        System.out.println(size);
        float random =  0.2f;
        for(Node node : graph.nodes){node.curValue =  1/size;}
        boolean converge = false;
        float diff = 0;
        while(!converge){
            for(Node node : graph.nodes) {
                node.proValue = random / size;
                if (node.neighbours.size() > 0) {
                    for (Node p : node.neighbours) {
                        node.proValue += (1 - random) * p.curValue / p.outLinks;
                    }
                }
                diff = Math.max(diff, Math.abs(node.curValue - node.proValue));
                if (diff < 0.002) converge = true;
                node.curValue = node.proValue;
            }
        }
    }


    public static void main(String[] args) {
        Graph g = new Graph();
        List<Node> list = new ArrayList<>();
        FileInputStream inputStream = null;
        Scanner scanner = null;

        Map<String,Node> map = new HashMap<>();

     try {
         inputStream = new FileInputStream("/Users/YeJiang/IdeaProjects/PageRank/src/com/company/links.srt");
         scanner = new Scanner(inputStream,"UTF-8");
         while (scanner.hasNextLine()) {
             String line = scanner.nextLine();
             String[] detail = line.split("\\s+");
             if(detail.length == 2) {
                 if (map.containsKey(detail[0])) {
                     map.get(detail[0]).incrementOutLinks();
                     if (map.containsKey(detail[1])) {
                         map.get(detail[1]).neighbours.add(map.get(detail[0]));
                         // increment its in link value
                         map.get(detail[1]).incrementInLinks();

                     } else {
                         Node newDes = new Node(detail[1], 1); // create with 1 inLink value instead of 0
                         newDes.neighbours.add(map.get(detail[0]));
                         map.put(detail[1], newDes);
                     }
                 }
                 else {
                     if (map.containsKey(detail[1])) {
                         Node newOrin = new Node(detail[0]);
                         map.get(detail[1]).neighbours.add(newOrin);
                         // increment its in Link value
                         map.get(detail[1]).incrementInLinks();
                         newOrin.incrementOutLinks();
                         map.put(detail[0], newOrin);
                     }
                     // both need create new
                     else {
                         Node newOrin = new Node(detail[0]);
                         // construct with inLink 1 value
                         Node newDesti = new Node(detail[1], 1);
                         newDesti.neighbours.add(newOrin);
                         newOrin.incrementOutLinks();
                         map.put(detail[0], newOrin);
                         map.put(detail[1], newDesti);
                     }
                 }
             }
         }
     }catch(Exception e){
         e.printStackTrace();
     }
     finally {
         try {
             if (inputStream != null) inputStream.close();
             if (scanner != null) scanner.close();

         }catch (Exception e){
             e.printStackTrace();
         }
     }
        list.addAll(map.values());
        g.nodes = list;

        // use calculate method to get page ranks for each page node
        calculate(g);

        // to get first 50 for Page Rank and in Links number, we create two Priority Queue for each use case

        PriorityQueue PRmaxQueue = new PriorityQueue<Node>(list.size(),new Comparator<Node>(){
                 @Override
                 public int compare(Node n1, Node n2){
                     return Double.compare(n2.curValue, n1.curValue);
                 }
        });

        PriorityQueue inLinkmaxQueue = new PriorityQueue<Node>(list.size(), new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Long.compare(o2.inLinks,o1.inLinks);
            }
        });

        for(Node n : list){
            PRmaxQueue.add(n);
            inLinkmaxQueue.add(n);
        }

        int i = 0;
        Node[] first50PangeRank = new Node[50];
        Node[] first50InLinks = new Node[50];
        while(i<50){
            first50PangeRank[i] = (Node)PRmaxQueue.poll();
            first50InLinks[i] = (Node)inLinkmaxQueue.poll();
            i++;
        }

        System.out.println("Here are first 50 InLinks and inLinks number");
        for(int j=0;j<50;j++){
            System.out.println(j+1 +" . "+ first50InLinks[j].name + "         " + first50InLinks[j].inLinks);
        }


        System.out.println("Here are first 50 page rank  and  PR value");
        for(int j=0;j<50;j++){
            System.out.println(j+1 +" . "+  first50PangeRank[j].name + "           " + first50PangeRank[j].proValue);
        }
    }
}

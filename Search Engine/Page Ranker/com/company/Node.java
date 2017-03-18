package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YeJiang on 2/12/17.
 */
public class Node {
    public String name;
    public float curValue;
    public float proValue;
    public List<Node> neighbours;
    public long inLinks;
    public long outLinks;

    public Node(String name){
         this.name = name;
        curValue = 0;
        proValue = 0;
        inLinks = 0;
        outLinks = 0;
        neighbours = new ArrayList<>();
    }

    public Node(String name, long i)
    {
        this.name = name;
        inLinks = i;
        curValue = 0;
        proValue = 0;
        neighbours = new ArrayList<>();
    }

    public void incrementInLinks(){
        this.inLinks = this.inLinks + 1;
    }

    public void incrementOutLinks(){
        this.outLinks = this.outLinks +1;
    }
}

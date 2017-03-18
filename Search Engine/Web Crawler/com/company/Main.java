package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        // part A , only umass domain 100 unique links
        boolean partA = true;
        boolean partB = true;
        if (partA) {
            try {
                Crawler umassCrawler = new Crawler("http://ciir.cs.umass.edu", 100, true);
                Set<String> Links = umassCrawler.start();
                List<String> links = new ArrayList(Links);
                PrintWriter pw = new PrintWriter("100Links.txt");
                for (int i = 0; i < 100; i++) {
                   if(!links.get(i).contains("ciir"))System.out.println(links.get(i));
                }
                System.out.println("Part A finished");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (partB) {
            // part B, no restrict in domain 1000 links
            try {
                Crawler crawler = new Crawler("http://ciir.cs.umass.edu", 1000, false);
                Set<String> Links = crawler.start();
                List links = new ArrayList(Links);
                System.out.println("we get " + Links.size() + "unique links");
                PrintWriter pw = new PrintWriter("1000Links.txt");
                for (int i = 0; i < 1000; i++) {
                    pw.print(links.get(i) + "  ");
                }
                System.out.println("Part B finished");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

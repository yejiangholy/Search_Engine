package com.company.supporter;
import java.io.*;
import java.net.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * Created by YeJiang on 2/3/17.
 */
public class Save {

    // copy the content of URL to this writer
    public static void downLoadURL(URL url, PrintWriter writer)throws IOException
    {
        BufferedInputStream input = new BufferedInputStream(url.openStream());
        for(int c = input.read(); c != -1 ; c= input.read()){
            writer.print(c);
        }
    }

    public static void downLoadURLtoFile(URL url, String filename)throws  IOException{

        PrintWriter writer = new PrintWriter(filename);
        downLoadURL(url, writer);
        writer.write(System.getProperty( "line.separator" ));
        writer.close();
    }

    // extract links from a URL
    public static List getLinks(String url)throws  IOException
    {
        Set<String> set = new HashSet<>();
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a");
        for (Element link : links) {
           String newUrl = link.absUrl("href").toLowerCase();
            set.add(newUrl);
        }
        return new ArrayList<>(set);
    }

    public static String getDomainName(String u) throws URISyntaxException {
        try {
            URL url = new URL(u);
            String host = url.getHost().toLowerCase();
            if(host.startsWith("www."))host = host.substring(4); // we do not want "www." in front of our domain name
            return host;
        }catch (Exception e){
            return "unknow domain";
        }
    }
}

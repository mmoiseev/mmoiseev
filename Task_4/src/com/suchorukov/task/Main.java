package com.suchorukov.task;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Reader r = new InputStreamReader(new BufferedInputStream(new FileInputStream("c:\\DiscD\\file.txt")),"UTF-8"); //"c:\\DiscD\\file.txt"
        StringBuilder strBuild = new StringBuilder();
        final Map<String, Integer> wordFrequency = new HashMap<String, Integer>();
        int wordCount = 0;

        int cur;
        while ((cur = r.read()) != -1) {
             char c = (char)cur;
            if (Character.isLetterOrDigit(c)) {
                strBuild.append(c);
                //System.out.println(strBuild.toString());
            } else if (!"".equals(strBuild.toString())) {
                Integer freq = wordFrequency.get(strBuild.toString());
                wordFrequency.put(strBuild.toString(), (freq != null ? freq : 0) + 1);
                strBuild.setLength(0);
                wordCount++;
            }
        }

        List<Map.Entry<String, Integer>> words = new ArrayList<Map.Entry<String, Integer>>(wordFrequency.entrySet());
        Collections.sort(words,
                new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        int q = o2.getValue().compareTo(o1.getValue());
                        return  q == 0 ? o1.getKey().compareTo(o2.getKey()) : q;
                    }
                });


        for (int i = 0; i < words.size(); i++) {
            Double p =((double) words.get(i).getValue()/wordCount) * 100;

            System.out.println(words.get(i).getKey() + "," + p + "%");

        }
        //System.out.println(wordFrequency.size() + " " + wordCount);

    }
}

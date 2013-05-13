package com.suchorukov.task;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // todo: убрать захардкоженный текстовый файл, пусть файл задается из коммандной строки, а если она пустая, то чтение должно быть из консоли
        Reader r = new InputStreamReader(new BufferedInputStream(new FileInputStream("c:\\file.txt")), "UTF-8");
        StringBuilder strBuild = new StringBuilder();
        final Map<String, Integer> wordFrequency = new HashMap<String, Integer>();
        int wordCount = 0;

        int cur;
        while ((cur = r.read()) != -1) {
            char c = (char) cur;
            if (Character.isLetterOrDigit(c)) {
                strBuild.append(c);


                // todo 2) Если файл кончается на букву, будет ли последнее слово учтено в статистике?
            } else if (strBuild.length() > 0) {
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
                        return q == 0 ? o1.getKey().compareTo(o2.getKey()) : q;
                    }
                });


        for (int i = 0; i < words.size(); i++) {
            Double p = ((double) words.get(i).getValue() / wordCount) * 100;

            System.out.println(words.get(i).getKey() + "," + p + "%");

        }

        // todo файл не закрыт!!
    }
}

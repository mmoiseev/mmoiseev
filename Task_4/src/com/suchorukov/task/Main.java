package com.suchorukov.task;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        InputStream in = null;
        if (args.length > 0) {
            try {
                in = new FileInputStream(args[0]);
            } catch (FileNotFoundException e) {
                System.out.println("Файл " + args[0] + " не найден, включён консольный ввод");
                in = System.in;
            }
        } else {
            in = System.in;
        }

        Reader r = null;
        try {
            if (in.equals(System.in)) {
                Scanner scanner = null;
                try {
                    scanner = new Scanner(in);
                    String res = scanner.nextLine();
                    scanner.close();
                    in = new ByteArrayInputStream(res.getBytes());
                } catch (Exception e){
                    throw new Exception(e);
                } finally {
                    if (scanner != null)
                        scanner.close();
                }
            }
            r = new InputStreamReader(new BufferedInputStream(in), "UTF-8");
            calcFrequency(r);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка во время выполнения", e);
        } finally {
            if (r != null)
                r.close();
        }
    }

    private static void calcFrequency(Reader r) throws IOException {
        StringBuilder strBuild = new StringBuilder();
        final Map<String, Integer> wordFrequency = new HashMap<String, Integer>();
        int wordCount = 0;

        int cur;
        while ((cur = r.read()) != -1 || strBuild.length() > 0) {
            char c = (char) cur;
            if (Character.isLetterOrDigit(c)) {
                strBuild.append(c);
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
    }
}

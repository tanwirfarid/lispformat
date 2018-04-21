package com.tanwirfarid;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    private static List<String> processInputFile(String inputFilePath) {
        List<String> inputList = new ArrayList<String>();
        try {
            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            inputList = br.lines().map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
        }
        return inputList;
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("errör");
        } else {
            List<String> list = processInputFile(args[0].replaceAll("\"", ""));

            Path path = Paths.get(args[0].concat(".lisp").replace("\"", ""));

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (String line : list) {
                    writer.write(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("yeay");

        }
    }


    private static Function<String, String> mapToItem = (line) -> {
        String[] p = line.split(";");// a CSV has comma separated lines

        StringBuilder builder = new StringBuilder();

        builder.append("'((");
        for (int i = 0; i < p.length - 1; i++) {
            builder.append("\"");
            builder.append(p[i].replace("ü", "ue")
                    .replace("ö", "oe")
                    .replace("ä", "ae")
                    .replace("ß", "ss")
                    .replace("Ü(?=[a-zäöüß ])", "Ue")
                    .replace("Ö(?=[a-zäöüß ])", "Oe")
                    .replace("Ä(?=[a-zäöüß ])", "Ae")
                    .replace("Ü", "UE")
                    .replace("Ö", "OE")
                    .replace("Ä", "AE")
                    .replace("�", "ue"));
            builder.append("\" ");
        }
        builder.append(") ");
        builder.append(" \"");
        builder.append(p[p.length - 1]);
        builder.append("\" )\n");


        return builder.toString();
    };


}

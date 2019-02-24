package lab1.verifier;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ReportGenerator {

    private ReportGenerator() {}

    private static String template = "/Users/acarus/modeling-lab1/report_template.html";

    public static void generateReport(List<Integer> seq, String file) {
        StringBuilder data = new StringBuilder();
        data.append("[");
        for (int i = 0; i < seq.size(); ++i) {
            if (i > 0) {
                data.append(",");
            }
            data.append(seq.get(i));
        }
        data.append("]");
        try {
            writeReport(data.toString(), file);
        } catch (IOException e) {}
    }

    private static String readTemplate() throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(template));
        StringBuilder buff = new StringBuilder();
        while (in.hasNext()) {
            buff.append(in.nextLine() + "\n");
        }
        in.close();
        return buff.toString();
    }

    private static void writeReport(String data, String file) throws IOException {
        String resFile = readTemplate().replace("{{data}}", data);
        FileWriter out = new FileWriter(file);
        out.write(resFile);
        out.close();
    }
}

package aiAssignment3;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Scanner;

public class Tester {

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/aiAssignment3/houn.txt");
        String content = Files.readString(fileName);
        content = content.replaceAll("\\p{Punct}", "");
        Scanner scanner = new Scanner((content));

        Path fileName2 = Path.of("src/aiAssignment3/sign.txt");
        String content2 = Files.readString(fileName);
        content2 = content2.replaceAll("\\p{Punct}", "");
        Scanner scanner2 = new Scanner((content));

        for (int i = 1; i <=8; i++) {
            scanner.nextLine();
            scanner2.nextLine();
        }

        StringBuilder finalStory = new StringBuilder();
        StringBuilder finalStory2 = new StringBuilder();

        while (scanner.hasNextLine()) {
            String temp = scanner.nextLine();
            if (temp.contains("CHAPTER")) {
                scanner.nextLine();
                temp = scanner.nextLine();
            }
            finalStory.append(temp+"\n");
        }

        while (scanner2.hasNextLine()) {
            String temp = scanner2.nextLine();
            if (temp.contains("CHAPTER")) {
                scanner2.nextLine();
                temp = scanner2.nextLine();
            }
            finalStory2.append(temp+"\n");
        }

        String readStory = finalStory.toString().toLowerCase();
        String readStory2 = finalStory2.toString().toLowerCase();


        Markov trigram1 = new Markov(readStory, readStory2);
        trigram1.WriteAStory();

        FileWriter myWriter = new FileWriter("src/aiAssignment3/Readme.txt");
        myWriter.write(trigram1.getMyStory());
    }
}

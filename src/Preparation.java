
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.RandomAccessFile;
import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class Preparation {

  private static void writeData(String filePath, String data, int seek) throws IOException {
		RandomAccessFile file = new RandomAccessFile(filePath, "rw");
		file.seek(seek);
		file.write(data.getBytes());
		file.close();
	}

    public static void main(String [ ] args) {

        Helpers helper = new Helpers();
        int[] a = new int[30*30*30];

        try {
            RandomAccessFile fileA = new RandomAccessFile("a.txt", "rw");
            // RandomAccessFile fileB1 = new RandomAccessFile("b1.txt", "rw");
            // RandomAccessFile fileB2 = new RandomAccessFile("b2.txt", "rw");
            RandomAccessFile fileB = new RandomAccessFile("b.txt", "rw");
            RandomAccessFile fileC = new RandomAccessFile("c.txt", "rw");

            File file = new File("rawindex.txt");
            Charset charset = StandardCharsets.ISO_8859_1;

            StringBuilder sb = new StringBuilder();
            InputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));

            String line;
            int pointer_in_c = 0;
            int pointer_in_b = 0;

            int counter = 0;

            String prevWord = "";
            outer: while ((line = br.readLine()) != null) {
                //sb.append(line + System.lineSeparator());
                String[] token = line.split("\\s");

                String word = token[0];
                int occurrence = Integer.parseInt(token[1]);

                //System.out.println(word + ", " + occurrence);

                // om ordet är nytt
                if (!prevWord.equals(word)){ // we have a new word!!
                    // Lägg till ordet och motsvarande förekomst i B
                    fileB.writeChars(word + ' ' + String.valueOf(pointer_in_c) + '\n');

                    //fileB.getFilePointer(); !!!!!!!

                    // fixa a på nåt sätt tror jag
                    //System.out.println("nu ska vi hitta hashen för ordet: "+ word);
                    int hashOfWord = helper.getHash(word);
                    //System.out.println("hashen för det ordet är: "+ hashOfWord);
                    a[hashOfWord] = pointer_in_b;
                    //writeData("a.txt", String.valueOf(pointer_in_b), hashOfWord*8);

                    pointer_in_b += 8;



                }

                // lägg till förekomst i c
                // c.add(occurrence);
                writeData("c.txt", String.valueOf(occurrence), pointer_in_c);

                pointer_in_c += 8;

                prevWord = word;

                // counter++;
                // if (counter == 3){
                //     break outer;
                //
                // }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

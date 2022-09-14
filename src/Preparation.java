
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

  // private static void writeData(String filePath, String data, int seek) throws IOException {
	// 	RandomAccessFile file = new RandomAccessFile(filePath, "rw");
	// 	file.seek(seek);
	// 	file.write(data.getBytes());
	// 	file.close();
	// }

    public static void main(String [ ] args) {

        Helpers helper = new Helpers();
        long[] a = new long[30*30*30];
        Arrays.fill(a, -1);

        try {
            //RandomAccessFile fileA = new RandomAccessFile("a.txt", "rw");
            // RandomAccessFile fileB1 = new RandomAccessFile("b1.txt", "rw");
            // RandomAccessFile fileB2 = new RandomAccessFile("b2.txt", "rw");
            RandomAccessFile fileB = new RandomAccessFile("b", "rw");
            RandomAccessFile fileC = new RandomAccessFile("c", "rw");

            File file = new File("rawindex.txt");
            Charset charset = StandardCharsets.ISO_8859_1;

            StringBuilder sb = new StringBuilder();
            InputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));

            String line;

            String prevWord = "";
            while ((line = br.readLine()) != null) {
                //sb.append(line + System.lineSeparator());
                String[] token = line.split("\\s");

                String word = token[0];
                int occurrence = Integer.parseInt(token[1]);

                //System.out.println(word + ", " + occurrence);

                // om ordet är nytt
                if (!prevWord.equals(word)){ // we have a new word!!
                    // fixa A
                    //System.out.println("nu ska vi hitta hashen för ordet: "+ word);
                    int hashOfWord = helper.getHash(word);
                    //System.out.println("hashen för det ordet är: "+ hashOfWord);
                    if (a[hashOfWord] == -1) {
                      // Ny kombination av 3 första bokstäverna
                      a[hashOfWord] = fileB.getFilePointer();
                    }
                    // Lägg till ordet och motsvarande förekomst i B
                    fileB.writeChars(word + ' ' + String.valueOf(fileC.getFilePointer()) + '\n');
                }

                // lägg till förekomst i c
                fileC.writeChars(String.valueOf(occurrence) + '\n');

                prevWord = word;
            }

            try
            {
                FileOutputStream fos = new FileOutputStream("arrayA");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(a);
                oos.close();
                fos.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }

            //fileA.writeChars(a.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

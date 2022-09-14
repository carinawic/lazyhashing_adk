import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;

public class Konkordans {
    public static void main(String [] args) {

      Helpers helper = new Helpers();

      // real data: /afs/kth.se/misc/info/kurser/DD2350/adk22/labb1
      if (args.length == 0) {
        System.err.println("Inget ord angivet.");
        return;
      }

      long[] a = retriveAFromFile();
      String completeWord = args[0];
      int hashedValue = helper.getHash(completeWord);
      // System.out.println("the hashed value of "+ completeWord+ " is " + hashedValue);

      // get the first word hash index
      long index_1_from_a = a[hashedValue];
      // ordet finns tidigast på plats index_1_from_a, om ordet finns
      if (index_1_from_a == -1){
        System.out.println("Ordet " + completeWord + " existerar inte i texten :(");
        return;
      }

      long index_2_from_a = -1;
      int hashCounter = 1;
      File fileB = new File("b");

      do{
        if (hashedValue + hashCounter > 30*30*30) {
          index_2_from_a = fileB.length()+1;  // längden av b + 1
          break;
        }
        index_2_from_a = a[hashedValue + hashCounter];
        hashCounter++;
      } while(index_2_from_a == -1);
      // ordet finns innan index_2_from_a, om ordet finns

      // binary search between index_1_from_a and index_2_from_a
      long index_from_B = getIndexInB(index_1_from_a, index_2_from_a, completeWord);
      // System.out.println("finshed binary search");
      if (index_from_B == -1) {
        System.out.println("Ordet " + completeWord + " existerar inte i texten :(");
        return;
      }
      System.out.println("found the occurrence that corresponds to ."+completeWord+". from B to C ."+index_from_B+".");
    }

    // Hitta ordet 'word' some finns tidigast på plats 'index1' och innan plats 'index2' om den finns.
    // Om ordet inte finns, retunera '-1'.
    private static long getIndexInB(long index1, long index2, String word) {
      //System.out.println("binary search between "+index1+" and "+index2);
      if (index1 >= index2) {
        return -1;
      }

      long mid = (index1 + index2) / 2;
      String row_at_mid = "";

      row_at_mid = getRowFromB(mid);
      // System.out.println("row_at_mid = " + row_at_mid);

      String[] rowitems = row_at_mid.split(" ");

      String word_in_b = rowitems[0];
      long occurrence = Long.parseLong(rowitems[1]);

      int compare = word_in_b.compareTo(word);
      // System.out.println("we compare " +row_at_mid+ " to " +word+ " value is: " +compare);

      if (compare == 0) {
        // hittat ordet
        // System.out.println("hittat " + word + " pa plats " + mid);
        return occurrence;
      } else if (compare < 0) {
        // System.out.println("ordet " + word + " ar efter " + mid);
        return getIndexInB(mid+1, index2, word);
      } else if (compare > 0) {
        // System.out.println("ordet " + word + " ar fore " + mid);
        return getIndexInB(index1, mid, word);
      }
      // ska aldrig komma hit.
      return -1;
    }

    private static String getRowFromB(long seek) {
      String word = "";
      try {
        RandomAccessFile file = new RandomAccessFile("b", "r");
        long fileLength = file.length();

        // Hitta början av ordet.
        char readChar = ' ';
        while(readChar != '\n'){
          if (seek < 0) {
            seek = 0;
            break;
          }
          file.seek(seek);

          byte[] bytes = new byte[1];
          file.read(bytes);

          readChar = (char) bytes[0];

          seek--;
        }
        seek = seek+2;

        // Hitta resten av ordet.

        while(true){
          if (seek > fileLength) {
            System.out.println("Fil B tog slut!");
            return word;
          }
          file.seek(seek);

          byte[] bs = new byte[1];
          file.read(bs);
          readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);

          seek++;
          file.seek(seek);

          file.read(bs);
          readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);
          
          if(readChar == '\n') break;
          word += readChar;
          seek++;
        }

        file.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      // System.out.println("Retunerar ordet ."+word+".");
      return word;
	   }

    private static byte[] readCharsFromFile(String filePath, long seek, int chars) throws IOException {
      try {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        file.seek(seek);
        byte[] bytes = new byte[chars];
        file.read(bytes);
        file.close();
        return bytes;
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
	   }

     // Läss in arrayA från fil och retunera arrayen.
     private static long[] retriveAFromFile() {
       long[] a = new long[30*30*30];
       try
        {
            FileInputStream fis = new FileInputStream("arrayA");
            ObjectInputStream ois = new ObjectInputStream(fis);
            a = (long[]) ois.readObject();
            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }

        return a;
     }
}

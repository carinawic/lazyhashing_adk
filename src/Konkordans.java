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
      String word = args[0];
      int hashedValue = helper.getHash(word);
      // System.out.println("the hashed value of "+ word+ " is " + hashedValue);

      // get the first row1 hash index
      long index_1_from_a = a[hashedValue];
      // ordet finns tidigast på plats index_1_from_a, om ordet finns
      if (index_1_from_a == -1){
        System.out.println("Ordet " + word + " existerar inte i texten :(");
        return;
      }

      long index_2_from_a = -1;
      int hashCounter = 1;
      File fileB = new File("b.txt");

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
      long[] indexes_from_B = getIndexesInB(index_1_from_a, index_2_from_a, word);
      // System.out.println("finshed binary search");
      if (indexes_from_B == null) {
        System.out.println("Ordet " + word + " existerar inte i texten :(");
        return;
      }
      System.out.println("found the occurrence that corresponds to ."+word+". from B to C ."+indexes_from_B[0]+"." + indexes_from_B[1]);

      // find row1 at index in c







    }



    // Hitta ordet 'row1' some finns tidigast på plats 'index1' och innan plats 'index2' om den finns.
    // Om ordet inte finns, retunera '-1'.
    private static long[] getIndexesInB(long index1, long index2, String word) {
      System.out.println("binary search between "+index1+" and "+index2);

      if (index1 >= index2) {
        System.out.println("REASON FOR NULL IS THIS");
        return null;
      }

      long mid = (index1 + index2) / 2;
      String[] rows_at_mid = getRowsFromB(mid);
      String word1 = rows_at_mid[0].split(" ")[0]; // row [0] before the space
      int compare = word1.compareTo(word);

      if (compare == 0) {
        long[] occurrences = new long[2];
        occurrences[0] = Long.parseLong(rows_at_mid[0].split(" ")[1]); // row [0] after the space
        occurrences[1] = Long.parseLong(rows_at_mid[1].split(" ")[1]); // row [1] after the space
      
        return occurrences;
      } else if (compare < 0) {
        System.out.println(word + " is after " + word1);
        return getIndexesInB(mid+1, index2, word);
      } else if (compare > 0) {
        
        System.out.println(word + " is before " + word1);
        return getIndexesInB(index1, mid, word);
      }
      // ska aldrig komma hit.
      return null;
    }

    private static String[] getRowsFromB(long seek) {
      String row1 = "";
      String row2 = "";
      try {
        RandomAccessFile file = new RandomAccessFile("b.txt", "r");
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

        // Hitta resten av raden.

        while(true){ // find row 1
          if (seek > fileLength) {
            System.out.println("Fil B tog slut!");
            break;
          }
          file.seek(seek);

          byte[] bs = new byte[1];
          file.read(bs);
          readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);

          seek++;
          file.seek(seek);

          file.read(bs);
          readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);
          seek++;
          if(readChar == '\n') break;
          row1 += readChar;
          
        }

        while(true){ // find row 2
          if (seek > fileLength) {
            System.out.println("Fil B tog slut!");
            break;
          }
          file.seek(seek);

          byte[] bs = new byte[1];
          file.read(bs);
          readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);

          seek++;
          file.seek(seek);

          file.read(bs);
          readChar = (new String(bs, StandardCharsets.ISO_8859_1)).charAt(0);
          seek++;
          if(readChar == '\n') break;
          row2 += readChar;
          
        }

        file.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      // System.out.println("Retunerar ordet ."+row1+".");

      String[] returnRows = {row1, row2};
      return returnRows;
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

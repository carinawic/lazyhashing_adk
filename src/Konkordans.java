import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//import org.apache.commons.lang3.StringUtils;

public class Konkordans {
    public static void main(String [] args) {

      // System.setProperty("file.encoding","ISO_8859_1");
      // Field charset = Charset.class.getDeclaredField("defaultCharset");
      // charset.setAccessible(true);
      // charset.set(null,null);

      Helpers helper = new Helpers();

      // real data: /afs/kth.se/misc/info/kurser/DD2350/adk22/labb1
      if (args.length == 0) {
        System.err.println("Inget ord angivet.");
        return;
      }

      String completeWord = args[0];

      int hashedValue = helper.getHash(completeWord);
      System.out.println("the hashed value of "+ completeWord+ " is " + hashedValue);

      // get the first word hash index
      
      int index_1_from_a = getIndexInAIfExists(hashedValue);
      if (index_1_from_a == -1){
        System.out.println("Ordet " + completeWord + " existerar inte i texten :(");
        return;
      }
      
      int index_2_from_a = -1;
      int hashCounter = 1;
      do{
        index_2_from_a = getIndexInAIfExists(hashedValue+hashCounter);
        hashCounter++;
        // TODO: make sure we dont reach the end of the file!!
        
      }while(index_2_from_a == -1);

      // binary search between index_1_from_a and index_2_from_a
      System.out.println("binary search between "+index_1_from_a+" and "+index_2_from_a);
      
      


    }

    private static int getIndexInAIfExists(int hashedValue){

      
      try{
        byte[] read_from_a = readCharsFromFile("a.txt", hashedValue*8, 8);
        
        String string_from_a = new String(read_from_a);
        

        string_from_a = string_from_a.replaceAll("[^0-9]","");

        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(string_from_a);
        if (m.find()){
          //System.out.println(new String("MATCH!!!"));
          return Integer.valueOf(string_from_a);
        }else{
          //System.out.println(new String("NO MATCH!!!"));
          return -1;
        }


      } catch (IOException e) {
        e.printStackTrace();
      }

      return -1;
      

    }

    private static byte[] readCharsFromFile(String filePath, int seek, int chars) throws IOException {
      RandomAccessFile file = new RandomAccessFile(filePath, "r");
      file.seek(seek);
      byte[] bytes = new byte[chars];
      file.read(bytes);
      file.close();
      return bytes;
	}
}

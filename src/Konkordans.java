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
      System.out.println(hashedValue);

      


      try{
        byte[] read_from_a = readCharsFromFile("a.txt", hashedValue*8, 8);
        
        String string_from_a = new String(read_from_a);

        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(string_from_a);
        if (m.find()){
          System.out.println(new String("MATCH!!!"));

        }else{
          System.out.println(new String("NO MATCH!!!"));
          return;
        }


      } catch (IOException e) {
            e.printStackTrace();
      }
      

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

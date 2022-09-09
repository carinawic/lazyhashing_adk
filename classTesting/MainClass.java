import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class MainClass {
    public static void main(String [ ] args) {
      System.out.println("This is MainClass!");

      String in = "empty";
      byte[] byteArrray = {};
      try {
        //in = args[0].getBytes("UTF-8").toString();
        in = new String(args[0].getBytes("ISO_8859_1"));
        byteArrray = in.getBytes("ISO_8859_1");

        for (Byte var : args[0].getBytes())
      {
          System.out.println(var);
      }

      } catch (Exception e) {}

      System.out.println(args[0]);
      System.out.println(in);


      //HelpClass.fun();

    }
  }

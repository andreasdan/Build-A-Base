import java.io.*;

public class BuildABase
{
   public static void main(String[] args) throws FileNotFoundException
   {
      Menu menu = new Menu();
      boolean runProgram = true;


      while (runProgram)
      {
         runProgram = menu.displayMenu();
      }


   }
}

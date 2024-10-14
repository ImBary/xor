//Bartosz Smolibowski 278776
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileUtils {

    private static final String path = System.getProperty("user.dir")+"/";
    public static void CreateFile(String filename) {
        try {

            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
    }
    public static void WriteFile(String content, String filename){
        try{
            CreateFile(filename);
            FileWriter myWriter = new FileWriter(filename,true);
            myWriter.write(content);
            myWriter.close();


        }catch (IOException e){
            System.out.println("an error occuret");
        }
    }
    public static String ReadFile(String filename){
        try{
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            String data="";
            while(myReader.hasNextLine()){
                 data += myReader.nextLine();

            }
            myReader.close();
            return data;
        }catch (FileNotFoundException e){
            System.out.println("File not found");
            e.printStackTrace();
        }
        return "";
    }

    public static void DeleteFiles(String filename){

        File myObj = new File(filename);
        System.out.println(filename);
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            //System.out.println("Failed to delete the file.");
        }

    }
    public static void UsunPliki(){
        DeleteFiles(path+"crypto.txt");

        DeleteFiles(path+"plain.txt");
        DeleteFiles(path+"decrypt.txt");
    }
    public static boolean sprawdzPlik(String filename){
        File myObj = new File(filename);
        return myObj.isFile();
    }
}

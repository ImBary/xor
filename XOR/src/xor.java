import java.nio.charset.StandardCharsets;
import java.util.Arrays;

//Bartosz Smolibowski 278776
public class xor {
    public static void main(String[] args) {


        xorUtils test = new xorUtils();
        String path = System.getProperty("user.dir") +"/";
        //oryginalny plik tekst
        ;//tworzenie pliku 64 linijka

        ;//generowanie klucza


        //string z podzieleniem na linijki

        ;//zamiana klucza string->byte

        ;//xor klucza z linijka

        //xor n linijki crypto z n+1 linijka crypto


        if(args[0].equals("-p")){//towrzy klucz, orgin->plain, usuwa poprzedni klucz i towrzy nowy
            FileUtils.UsunPliki();
            String origText = FileUtils.ReadFile(path +"orig.txt");
            test.przygotowanie(origText,path + "plain.txt");
            //jesli chcemy wygenerowac klucz trzeba odkomentowac dwie linijki na dole
            //FileUtils.DeleteFiles(path+"key.txt");
            //test.StworzKlucz();
        }else if(args[0].equals("-e")){
            if(FileUtils.sprawdzPlik(path + "orig.txt") || FileUtils.sprawdzPlik(path + "plain.txt") || FileUtils.sprawdzPlik(path + "key.txt")){
                String plainText = FileUtils.ReadFile(path + "plain.txt");
                byte[]key = test.klucz();
                test.xorStrings(plainText,key);
            }else{
                System.out.println("pliki nie są przygotowane użyj opcji -p");
            }

        }else if(args[0].equals("-k")){
            test.cryptoAnalis();
        }else{
            System.out.println("Błędna akcja możliwe opcje -p, -e, -k");
        }



    }

}
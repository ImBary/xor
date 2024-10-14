//Bartosz Smolibowski 278776
import javax.swing.text.Utilities;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
public class xorUtils {
    private final String alf = "abcdefghijklmnopqrstuvwxyz ";
    private final String path = System.getProperty("user.dir")+"/";
    public void przygotowanie(String origText,String OutputFilename) {

        String plainText="";
        StringBuilder tmpText = new StringBuilder();
        origText = origText.replaceAll("[,.!?:;*&^%$#@{}\"/'-0123456789]", "");
        origText = origText.toLowerCase();

        int len = origText.length();
        if(len%64!=0){
            origText = origText + (" ").repeat(64-(len%64));
        }
        int a = 0;
        int b = 64;

        for(int i = 0; i<origText.length()/64; i++){

            plainText = origText.substring(a,b);
            tmpText.append(plainText);
            tmpText.append("\n");
            a=b;
            b+=64;
        }
        tmpText.delete(tmpText.length()-1,tmpText.length());
        FileUtils.WriteFile(tmpText.toString(),OutputFilename);

    }//wpisanie do pliku po 64 w linijce

    public void StworzKlucz(){
        StringBuilder klucz = new StringBuilder();

        Random rand = new Random();
        for(int i = 0; i<64; i++){
            klucz.append(alf.charAt(rand.nextInt(alf.length())));
        }
        FileUtils.WriteFile(klucz.toString(),"key.txt");
    }//random char generator

    public byte[] klucz() {
        String kluczString = FileUtils.ReadFile(path+"key.txt");
        byte[] bytes = kluczString.getBytes(StandardCharsets.US_ASCII);
        return bytes;
    }

    public void xorStrings(String plainText, byte[] klucz) {
        String linijka = "";
        String textall =plainText.replace("\n","");
        StringBuilder res = new StringBuilder();

        byte[] byteAll = textall.getBytes(StandardCharsets.US_ASCII);//wszystko na byte
        int a = 0;
        int b = 64;
        byte[] linijkaByte = new byte[64];
        byte[] wynikXor = new byte[64];
        for(int i =0; i<textall.length()/64; i++){
            linijka = textall.substring(a,b);
            linijkaByte = linijka.getBytes(StandardCharsets.US_ASCII);
            wynikXor = xor(linijkaByte,klucz);
            for(int j=0; j<wynikXor.length; j++){
                res.append(wynikXor[j]);
                res.append(" ");
            }
            if(i!=(textall.length()/64) -1){
                res.append("\n");
            }

            a=b;
            b+=64;
        }

        FileUtils.WriteFile(res.toString(),path+"crypto.txt");


    }//tworzenie crypto.txt

    // Funkcja XOR
    private byte[] xor(byte[] s1, byte[] s2) {
        byte[] result = new byte[s1.length];
        for (int i = 0; i < s1.length; i++) {
            result[i] = (byte) (s1[i] ^ s2[i]);
        }
        return result;
    }// pomocnicza w xorowaniu

    public void cryptoAnalis(){

        String crypto = FileUtils.ReadFile(path+"crypto.txt");
        String[] cryptoNumbers = crypto.split(" ");
        System.out.println(cryptoNumbers.length/64);
        int[][] lines = new int[cryptoNumbers.length/64][64];

        for (int i = 0; i < cryptoNumbers.length / 64; i++) {
            int[] line = new int[64]; // Create a new array for each row
            for (int k = 0; k < 64; k++) {
                line[k] = Integer.parseInt(cryptoNumbers[i * 64 + k]);
            }
            lines[i] = line; // Assign the new array to the lines array
        }
        System.out.println(lines[0][0]+" "+ lines[1][0]+" "+lines[2][0]);
        System.out.println(lines.length);
        // stworzenie list z kazdym xorowanym wczesniej wersem.
        char[][] decoded = initList(cryptoNumbers.length/64,64);
        int maxLini = 64;
        int maxWierszy = lines.length;
        System.out.println("dlugosc"+lines.length);
        for(int i =0; i<maxWierszy-2; i++){
            for(int j = 0; j<maxLini; j++){//bo sprawdzamy wiesz n z n+1
                int xorWiersza = lines[i][j] ^ lines[i+1][j];
                String xorBity = String.format("%8s",Integer.toBinaryString(xorWiersza)).replace(' ','0');
                System.out.println(xorBity);
                String poczatek = xorBity.substring(0,3);

                if(poczatek.equals("010")){//jeden ze znakow jest spacja
                    int xorWierszaDalej = lines[i+1][j] ^ lines[i+2][j];//m1m2
                    int xorWierszaPomiedzy = lines[i][j] ^ lines[i+2][j];//m1m3
                    String xorBityDalej = String.format("%8s",Integer.toBinaryString(xorWierszaDalej)).replace(' ','0');
                    String xorBityPomiedzy = String.format("%8s",Integer.toBinaryString(xorWierszaPomiedzy)).replace(' ','0');
                    if(xorBityDalej.substring(0,3).equals("000") && (!xorBityDalej.equals("00000000"))){//dwie litery ale nie identiko
                        int tmp = i;
                        for(int k = 0; k<lines.length; k++){
                            decoded[k][j] = (char) (lines[k][j] ^ lines[tmp][j] ^ 32);
                        }
                    }else if(xorBityDalej.substring(0,3).equals("010") && (!xorBityPomiedzy.equals("00000000"))){//jeden to spacja
                        int tmp = i+1;
                        for(int k = 0; k<lines.length; k++){
                            decoded[k][j] = (char) (lines[k][j] ^ lines[tmp][j] ^ 32);
                        }
                    }
                }
            }
        }
        String res = "";
        for(int i = 0; i<maxWierszy; i++){
            for(int j = 0 ; j<maxLini; j++){
                res+=decoded[i][j];

            }


        }
        przygotowanie(res,path + "decrypt.txt");
        //FileUtils.WriteFile(res,path + "decrypt.txt");

    }


    private char[][] initList(int numLines, int len){
        char[][] decoded = new char[numLines][len];
        for (int i = 0; i < numLines; i++) {
            for (int j = 0; j < len; j++) {
                decoded[i][j] = '_'; // Initialize with underscores
            }
        }
        return decoded;
    }
}

// Author: Przemysław Sałęga

package backend.key;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KeyGenerator {

    public static List<Character> getCharactersInOriginalOrder(){
        List<Character> list = new ArrayList<>();
        int number = 65536;
        for(int index=0;index<number; index++) {
            list.add((char)index);
        }
        return list;
    }

    public static List<Character> generateKey(){
        List<Character> list = getCharactersInOriginalOrder();
        List<Character> key = new ArrayList<>();
        Random generator = new Random();
        for(int i = 0; i<65536; i++){
            int index = generator.nextInt(65536 - i);
            key.add(list.get(index));
            list.remove(index);
        }
        return key;
    }

    public static void saveKeyToFile(List<Character> collection, File file) throws IOException {
        byte[] charsAsBytes = new byte[collection.size()];
        System.out.println(collection.size());
        for(int i = 0; i<charsAsBytes.length; i++){
            charsAsBytes[i] = (byte) collection.get(i).charValue();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(charsAsBytes, 0, charsAsBytes.length);
        fos.flush();
        fos.close();
    }

    public static List<Character> readKeyFromFile(File file) throws IOException {
        byte[] charsAsBytes = new byte[(int)file.length()];
        FileInputStream fis = new FileInputStream(file);
        System.out.println(fis.read(charsAsBytes, 0, charsAsBytes.length));
        fis.close();
        List<Character> list = new ArrayList<>();
        for(byte b: charsAsBytes){
            list.add((char)b);
        }
        return list;
    }

}

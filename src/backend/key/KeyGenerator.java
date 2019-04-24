// Author: Przemysław Sałęga

package backend.key;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KeyGenerator {

    public static List<Character> generateKey(){
        List<Character> list = new ArrayList<>();
        List<Character> key = new ArrayList<>();
        int number = 65536;
        for(int index=0;index<number; index++) {
            list.add((char)index);
        }
        Random generator = new Random();
        for(int i = 0; i<number; i++){
            int index = generator.nextInt(number - i);
            key.add(list.get(index));
            list.remove(index);
        }
        return key;
    }

}

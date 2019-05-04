// Author: Przemysław Sałęga

package backend.stats;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StatCounter {

    public static Map<Character, Integer> createStats(String text){
        Map<Character, Integer> map = new HashMap<>();
        for(Character character: text.toCharArray()){
            if(map.containsKey(character)){
                map.put(character, map.remove(character) + 1);
            } else {
                map.put(character, 1);
            }
        }
        return map;
    }

    public static Map<Character, Integer> statsSort(Map<Character, Integer> map){
        return map.entrySet().stream().sorted(
                Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (old, n) -> old, LinkedHashMap::new));
    }

    public static String parseStats(Map<Character, Integer> stats){
        Map<Character, Integer> map = statsSort(stats);
        double sum = 0;
        for(Map.Entry<Character, Integer> entry: map.entrySet()){
            //System.out.println(entry.getKey() + " " + entry.getValue());
            sum += entry.getValue();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<Character, Integer> entry: map.entrySet()){
            stringBuilder.append(entry.getKey() + ": " + entry.getValue()/sum*100 + "%\n");
        }
        return stringBuilder.toString();
    }

    public static String parseFile(File file, Charset charset){
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("Cp1250")))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

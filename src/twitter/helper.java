package twitter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class helper {

    //helper function to take in a set of strings and return a matching set of strings (all lowercase)
    public static Set<String> setOfStrToLowerCase(Set<String> setOfStr){
        Set<String> setOfLower = new HashSet<String>();
        for(String string: setOfStr){
            setOfLower.add(string.toLowerCase());
        }
        
        return setOfLower; 
    }
    
    // helper function to take in a Map<String, Set<String>> and return 
    // a Map<String, Set<String>> where all the strings are lowercase
    public static Map<String, Set<String>> mapOfStrToLowerCase(Map<String, Set<String>> map){
        
        Map<String, Set<String>> newMap = new HashMap<String, Set<String>>();
        for(String key: map.keySet()){
            Set<String> valuesLowerCase = new HashSet<String>();
            valuesLowerCase = setOfStrToLowerCase(map.get(key));
            newMap.put(key.toLowerCase(), valuesLowerCase);
        }
        
        return newMap; 
    }
    
}

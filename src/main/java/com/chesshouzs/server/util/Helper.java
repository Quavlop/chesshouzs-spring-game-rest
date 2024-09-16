package com.chesshouzs.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Helper {
    
    public static Boolean isCharEmpty(char ch){
        return ch == ' ';
    }

    public static String convertObjectToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean compareJson(Object obj1, Object obj2) throws Exception {

        String jsonObj1 = convertObjectToJson(obj1); 
        String jsonObj2 = convertObjectToJson(obj2);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode objNode1 = mapper.readTree(jsonObj1);
        JsonNode objNode2 = mapper.readTree(jsonObj2);
        
        return objNode1.equals(objNode2);
    }
    

}

package com.chesshouzs.server.util;

import com.chesshouzs.server.dto.custom.match.PositionDto;
import com.datastax.oss.driver.shaded.guava.common.collect.HashBasedTable;
import com.datastax.oss.driver.shaded.guava.common.collect.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.lettuce.core.StringMatchResult.Position;

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

    public static Table<Integer, Integer, Boolean> generateHashBasedTable(PositionDto ...positions){
        Table<Integer, Integer, Boolean> table = HashBasedTable.create();

        for (PositionDto pos : positions){
            table.put(pos.getRow(), pos.getCol(), true);
        }

        return table;
    }
    

}

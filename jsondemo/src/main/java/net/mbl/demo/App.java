package net.mbl.demo;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );
        File file = new File("/software/projects/AllIdeaProjects/gits/JavaDemoParent/jsondemo/src/main/resources/demo.json");
        if(!file.exists()) {
            System.out.println("where is the file?");
            return;
        }
        ObjectMapper objMapper = new ObjectMapper();
        JsonNode rootNode = objMapper.readTree(file);
        Entity entity = objMapper.readValue(rootNode.toString().toLowerCase(), Entity.class);
        System.out.println(entity);
    }
}

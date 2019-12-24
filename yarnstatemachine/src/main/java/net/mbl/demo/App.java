package net.mbl.demo;

import net.mbl.demo.statedemo.LocalizedResource;
import org.apache.hadoop.yarn.state.VisualizeStateMachine;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        VisualizeStateMachine.main(
                new String[] {"imageName", LocalizedResource.class.getName(), "filename"});
        System.out.println("Hello World!");
    }
}

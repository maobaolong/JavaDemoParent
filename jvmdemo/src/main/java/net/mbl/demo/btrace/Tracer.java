package net.mbl.demo.btrace;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.OnMethod;

import static com.sun.btrace.BTraceUtils.println;
@BTrace
public class Tracer {
@OnMethod(clazz = "java.lang.Thread", method = "start")

public static void onThreadStart() {

println("tracing method start");

} 

@OnMethod(clazz = "net.mbl.demo.btrace.BTraceOnMethodDemo", method = "say")
public static void onSay() {

println("wuha wuha haha");

} 

}

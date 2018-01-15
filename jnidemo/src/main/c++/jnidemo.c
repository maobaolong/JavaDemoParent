#include <stdio.h>
#include <stdlib.h>
#include "net_mbl_demo_jnidemo_JniDemo.h"

JNIEXPORT void Java_net_mbl_demo_jnidemo_JniDemo_sayHello(JNIEnv * env, jobject obj, jint len){
    printf("enter hello from jni\n");
    printf( "int = %d\n", len );

    int i;
    for (i=0; i < len; i++) {
        char* ptr = malloc(1 * 1024 * 1024);
        memset(ptr, 0, 1 * 1024 * 1024);
    }
    printf("exit hello from jni\n");
}
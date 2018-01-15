HEADER_PATH =\
-I/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/include/ \
-I/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/include/darwin

all:
	-mkdir ../../../target
	gcc $(HEADER_PATH) -c jnidemo.c -o ../../../target/jnidemo.o
	gcc -dynamiclib -o ../../../target/libjnidemo.jnilib ../../../target/jnidemo.o

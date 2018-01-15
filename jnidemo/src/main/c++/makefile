HEADER_PATH_MAC =\
-I/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/include/ \
-I/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/include/darwin

HEADER_PATH_LINUX =\
-I /software/servers/jdk1.8.0_65/include\
-I /software/servers/jdk1.8.0_65/include/linux

HEADER_PATH = $(HEADER_PATH_MAC)
mac:
	-mkdir ../../../target
	gcc $(HEADER_PATH) -c jnidemo.c -o ../../../target/jnidemo.o
	gcc -dynamiclib -o ../../../target/libjnidemo.jnilib ../../../target/jnidemo.o

linux:
    -mkdir ../../../target
    gcc -fPIC $(HEADER_PATH) -c jnidemo.c -o ../../../target/jnidemo.o
    gcc -shared -o ../../../target/libjnidemo.so ../../../target/jnidemo.o

JAVAC = javac -Xlint:unchecked
sources = $(shell find . -type f -name '*.java')
classes = $(sources:.java=.class)

all : $(classes)

clean :
	rm -f $(classes)

%.class : %.java
	$(JAVAC) -d bin -sourcepath src -cp lib/json-simple-1.1.1.jar $<

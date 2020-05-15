CC		:= g++ 
CFLAGS	:= -Wall -Wextra -g

BIN		:= bin
SRC		:= src
INCLUDE	:= include

EXECUTABLE	:= main
SOURCEDIRS	:= $(shell find $(SRC) -type d)
INCLUDEDIRS	:= $(shell find $(INCLUDE) -type d)

CINCLUDES	:= $(patsubst %,-I%, $(INCLUDEDIRS:%/=%))

SOURCES		:= $(wildcard $(patsubst %,%/*.cpp, $(SOURCEDIRS)))
OBJECTS		:= $(SOURCES:.cpp=.o)

all: $(BIN)/$(EXECUTABLE)

.PHONY: clean
clean:
	-$(RM) $(BIN)/$(EXECUTABLE)
	-$(RM) $(OBJECTS)


run: all
	./$(BIN)/$(EXECUTABLE) dicts/small.dict.rnd > tests/test1.out
	diff tests/test1.out tests/test1.std
	
contains_run: all
	./$(BIN)/$(EXECUTABLE) dicts/small.dict.asc > tests/test2.out
	diff tests/test2.out tests/test2.std
	
myrun: all
	./$(BIN)/$(EXECUTABLE) dicts/mydict.rnd

delete_run: all
	./$(BIN)/$(EXECUTABLE)  dicts/small.dict.asc  dicts/small.dict.des > tests/test3.out
	diff tests/test3.out tests/test3.std

it_run: all
	./$(BIN)/$(EXECUTABLE) dicts/small.dict.asc > tests/test4.out
	diff tests/test4.out tests/test4.std


$(BIN)/$(EXECUTABLE): $(OBJECTS)
	$(CC) $(CFLAGS) $(CINCLUDES) $^ -o $@

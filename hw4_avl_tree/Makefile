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

copy_run: all
	./$(BIN)/$(EXECUTABLE) dicts/small.dict.rnd > tests/test5.out
	diff tests/test5.out tests/test5.std
	
equal_run: all
	./$(BIN)/$(EXECUTABLE) dicts/small.dict.rnd > tests/test6.out
	diff tests/test6.out tests/test6.std
plus_run: all
	./$(BIN)/$(EXECUTABLE) dicts/small.dict.rnd dicts/tiny.dict > tests/test7.out
	diff tests/test7.out tests/test7.std
	
plus_equal_run: all
	./$(BIN)/$(EXECUTABLE) dicts/small.dict.rnd dicts/tiny.dict > tests/test8.out
	diff tests/test8.out tests/test8.std

string_plus_minus:
	./$(BIN)/$(EXECUTABLE) dicts/small.dict.rnd dicts/small.dict.des > tests/test9.out
	diff tests/test9.out tests/test9.std	

tree_string_plus_minus:
	./$(BIN)/$(EXECUTABLE) dicts/small.dict.rnd dicts/small.dict.des > tests/test10.out
	diff tests/test10.out tests/test10.std
	
$(BIN)/$(EXECUTABLE): $(OBJECTS)
	$(CC) $(CFLAGS) $(CINCLUDES) $^ -o $@

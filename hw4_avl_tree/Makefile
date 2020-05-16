
all: test1 test2 test3 test4 test5 test6 test7 test8 test9 test10 run diff

test1: tests/test1.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o $@

test2: tests/test2.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o $@

test3: tests/test3.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o $@

test4: tests/test4.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o $@
	
test5: tests/test5.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o $@

test6: tests/test6.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o $@

test7: tests/test7.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o $@

test8: tests/test8.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o $@

test9: tests/test9.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o $@

test10: tests/test10.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o $@

AVL.o: src/AVL.cpp include/AVL.hpp
	g++ -g -Wall -std=c++11 src/AVL.cpp -c
	
clean:
	rm *.o *.dot *.png test1 test2 test3 test4 test5 test6 test7 test8 test9 test10 onlytest9 out/*

run:
	./test1  dicts/small.dict.rnd  > out/test1.out
	./test2  dicts/small.dict.asc  > out/test2.out
	./test3  dicts/small.dict.asc dicts/small.dict.des > out/test3.out
	./test4  dicts/small.dict.asc > out/test4.out
	./test5  dicts/small.dict.rnd > out/test5.out
	./test6  dicts/small.dict.rnd > out/test6.out
	./test7  dicts/small.dict.rnd dicts/tiny.dict > out/test7.out
	./test8  dicts/small.dict.rnd dicts/tiny.dict > out/test8.out
	./test9  dicts/small.dict.rnd dicts/small.dict.des > out/test9.out
	./test10 dicts/small.dict.rnd dicts/small.dict.des > out/test10.out

diff:
	diff out/test1.out  std/test1.std
	diff out/test2.out  std/test2.std
	diff out/test3.out  std/test3.std
	diff out/test4.out  std/test4.std
	diff out/test5.out  std/test5.std
	diff out/test6.out  std/test6.std
	diff out/test7.out  std/test7.std
	diff out/test8.out  std/test8.std
	diff out/test10.out std/test10.std
	diff out/test9.out  std/test9.std

onlytest9: tests/test9.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o $@
	./$@  dicts/small.dict.rnd dicts/small.dict.des > out/test9.out
	diff out/test9.out  std/test9.std

debug9: tests/test9.cpp AVL.o
	g++ -Wall -g -std=c++11 $^ -o onlytest9

not9diff: test1 test2 test3 test4 test5 test6 test7 test8 test10
	./test1  dicts/small.dict.rnd  > out/test1.out
	./test2  dicts/small.dict.asc  > out/test2.out
	./test3  dicts/small.dict.asc dicts/small.dict.des > out/test3.out
	./test4  dicts/small.dict.asc > out/test4.out
	./test5  dicts/small.dict.rnd > out/test5.out
	./test6  dicts/small.dict.rnd > out/test6.out
	./test7  dicts/small.dict.rnd dicts/tiny.dict > out/test7.out
	./test8  dicts/small.dict.rnd dicts/tiny.dict > out/test8.out
	./test10 dicts/small.dict.rnd dicts/small.dict.des > out/test10.out
	diff out/test1.out  std/test1.std
	diff out/test2.out  std/test2.std
	diff out/test3.out  std/test3.std
	diff out/test4.out  std/test4.std
	diff out/test5.out  std/test5.std
	diff out/test6.out  std/test6.std
	diff out/test7.out  std/test7.std
	diff out/test8.out  std/test8.std
	diff out/test10.out std/test10.std
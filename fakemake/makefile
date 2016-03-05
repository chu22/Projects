hmap.o: hmap.c hmap.h
	gcc -c hmap.c -g
fakemake.o: fakemake.c fakemake.h hmap.o
	gcc -c fakemake.c -g
fakemake: main.c fakemake.o hmap.o
	gcc -o fakemake main.c fakemake.o hmap.o -g

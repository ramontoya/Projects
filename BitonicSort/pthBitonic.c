/* File:     pthBitonic.c
 * Author:   Raul Montoya
 *
 * Purpose:  Implement parallel bitonic sort using pthreads
 *           and a logical partition of the list.
 *
 * Input:    threadCount: integer number of threads (power of 2)
 *		       n: integer (evenly divisible by threadCount)
 *		       g: char (optional flag) generate pseudorandom list 
 *		       o: char (optional flag) print original/final lists
 * Output:   Sorted list (optional)
 *
 * Compile:  gcc -g -Wall -o pb pthBitonic.c -lpthread -DDEBUG
 * Usage:    ./pb <threadCount> <n> [g] [o]
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <pthread.h>
#include "timer.h"

/* shared variables */
int *list, *tmp;
int threadCount, n;
const int RMAX = 1000000;
int barrierThreadCount = 0;
pthread_mutex_t barrierMutex;
pthread_cond_t proceed;

/* serial functions */
void Usage(char* prog_name);
void CheckInput(int argc, char* argv[], int* print);
void GenerateList();
void ReadList();
void PrintList(char* prompt);
int Compare(const void* x_p, const void* y_p);
void SplitLow(int head1, int head2);
void SplitHigh(int head1, int head2);
void Barrier(void);
void BitonicSort(long thread, int partner, int phase, 
	unsigned andBit);

/* debug functions */
void PrintIntermediate(int threads, int phase);

/* thread function */
void *ThreadWork(void* rank);

/*-----------------------------------------------------------------*/
int main(int argc, char* argv[]) {
	long thread;  
	pthread_t* threadHandles; 
	int print; 
	double start, finish; 

	print = 0;
	
	/* check command line */
	CheckInput(argc, argv, &print);

	/* malloc threadHandles */
	threadHandles = malloc(threadCount*sizeof(pthread_t*));
	
	/* initialize mutex */
	pthread_mutex_init(&barrierMutex, NULL);
	/* initialize condition variable */
	pthread_cond_init(&proceed, NULL);

	GET_TIME(start);
	/* start up threads */
	for(thread = 0; thread < threadCount; thread++) {
		pthread_create(&threadHandles[thread], NULL, ThreadWork,
			(void*) thread);
	}
	/* join threads */
	for(thread = 0; thread < threadCount; thread++) {
		pthread_join(threadHandles[thread], NULL);
	}
	GET_TIME(finish);

	/* print final list */
	if(print) PrintList("Sorted:"); 

	/* print elapsed time */
	printf("Elapsed time = %e seconds\n", finish - start);

	pthread_mutex_destroy(&barrierMutex);
    	pthread_cond_destroy(&proceed);
	free(tmp);
	free(list);
	free(threadHandles);
	return 0;
}  /* main */

/*------------------------------------------------------------------
 * Function:  Usage
 * Purpose:   print a message showing what the command line should
 *            be, and terminate
 * In arg :   prog_name
 */
void Usage (char* prog_name) {
   fprintf(stderr, 
   	"usage: %s <thread count> <number of elements> [g] [o] \n", 
   	prog_name);
   exit(0);
}  /* Usage */

/*------------------------------------------------------------------
 * Function:  CheckInput
 * Purpose:   Check command line args    
 * In arg :   argc, argv, print
 */
void CheckInput(int argc, char* argv[], int* print) {
	int g = 0;

	/* check argc */
	if(argc < 3) Usage(argv[0]);
	
	/* get threadCount */
	threadCount = strtol(argv[1], NULL, 10);
	
	/* get n */
	n = strtol(argv[2], NULL, 10); 		  
	
	/* check both threadCount and n */
	if(threadCount < 1 || n < 1) Usage(argv[0]); 

	/* check 4th arg */
	if(argc > 3) {
		if(strcmp(argv[3], "g") == 0) g = 1;
		else if(strcmp(argv[3], "o") == 0) *print = 1;
	}

	/* check 5th arg */
	if(argc > 4) {
		if(strcmp(argv[4], "o") == 0) *print = 1;
		else if(strcmp(argv[3], "g") == 0) g = 1;
	}

	/* malloc list */
	list = malloc(n*sizeof(int));
	/* malloc tmp */
	tmp = malloc(n*sizeof(int));

	/* generate pseudorandom list */
	if(g) GenerateList();	
	/* read list */ 
	else ReadList();
	
	/* print original list */
	if(*print) PrintList("Unsorted:");
} /* CheckInput */

/*-----------------------------------------------------------------
 * Function:  GenerateList
 * Purpose:   Use random number generator to generate list elements
 */
void GenerateList() {
    int i;

    srandom(1);
    for (i = 0; i < n; i++) list[i] = random() % RMAX;
}  /* GenerateList */

/*------------------------------------------------------------------
 * Function:    ReadList
 * Purpose:     Read in the list
 */
void ReadList() {
    int i;

    printf("%s ", "Enter list:");
    for (i = 0; i < n; i++) scanf("%d", &list[i]);
}  /* ReadList */

/*-------------------------------------------------------------------
 * Function:  PrintList
 * Purpose:   Print a list of ints.
 * In args:   prompt: the string to be printed
 */
void PrintList(char* prompt) {
	int i;

	printf("%s ", prompt);
	for (i = 0; i < n; i++) printf("%d ", list[i]);	
	printf("\n"); 
} /* PrintList */

/*-------------------------------------------------------------------
 * Function:  PrintIntermediate
 * Purpose:   Print a current list.
 * In args:   threads, phase
 */
void PrintIntermediate(int threads, int phase) {
	int i;

	printf("%d-thread %d: ", threads, phase);
	for (i = 0; i < n; i++) printf("%d ", list[i]);	
	printf("\n"); 
} /* PrintList */

/*-----------------------------------------------------------------
 * Function:     Compare
 * Purpose:      Compare two ints and determine which is larger
 * In args:      x_p, y_p
 * Ret val:      -1 if *x_p < *y_p
 *                0 if *x_p == *y_p
 *                1 otherwise
 * Note:         For use by qsort library function
 */
int Compare(const void* x_p, const void* y_p) {
   int x = *((int*)x_p);
   int y = *((int*)y_p);

   if (x < y)
      return -1;
   else if (x == y)
      return 0;
   else /* x > y */
      return 1;
}  /* Compare */

/*-------------------------------------------------------------------
 * Function:    SplitLow
 * Purpose:     Merge the smallest size elements in a and b into c. 
 * In args:     a, size, b
 * In/out args: c
 */
void SplitLow(int head1, int head2) {
    int ai, bi, ci, size, i;
    
    size = n/threadCount;
    ai = head1;
    bi = head2;
    ci = ai;

    for(i = 0; i < size; i++) {
    	if(list[ai] <= list[bi]) {
    		tmp[ci] = list[ai];
    		ci++;
    		ai++;
    	} else {
    		tmp[ci] = list[bi];
    		ci++;
    		bi++;
    	}
    }
}  /* SplitLow */

/*-------------------------------------------------------------------
 * Function:    SplitHigh
 * Purpose:     Merge the largest size elements in a and b into c.
 * In args:     a, size, b
 * In/out args: c
 */
void SplitHigh(int head1, int head2) {
    int ai, bi, ci, size, i;
   
    size = n/threadCount;
    ai = head1+size-1;
    bi = head2+size-1;
    ci = ai;

    for(i = 0; i < size; i++) {
    	if(list[ai] >= list[bi]) {
    		tmp[ci] = list[ai];
    		ci--;
    		ai--;
    	} else {
    		tmp[ci] = list[bi];
    		ci--;
    		bi--;
    	}
    }
}  /* SplitHigh */

/*-------------------------------------------------------------------
 * Function:    Barrier
 * Purpose:     Use condition wait barriers to synchronize threads.
 * Global var:  threadCount, barrierThreadCount, barrierMutex,
 *              proceed
 * Return val:  Ignored
 */
void Barrier() {
    pthread_mutex_lock(&barrierMutex);
    barrierThreadCount++;
    if (barrierThreadCount == threadCount) {
    	barrierThreadCount = 0;
    	pthread_cond_broadcast(&proceed);
    } else {
    	while (pthread_cond_wait(&proceed, &barrierMutex) != 0);
    }
	pthread_mutex_unlock(&barrierMutex);
} /* Barrier */

/*-------------------------------------------------------------------
 * Function:    BitonicSort
 * Purpose:     Compute sequence of thread's sublist, 
 *		do corresponding split with partner, 
 *		copy split into shared list. 
 * In arg:      rank, partner, phase, andBit
 * Return val:  Ignored
 */
void BitonicSort(long thread, int partner, int phase,
	unsigned andBit) {
	int size = n/threadCount;
	int decrease = thread & andBit;
	int head1 = thread*size;
	int head2 = partner*size;
	
	if (decrease) {
		/* sequence decreases */
		if(thread < partner) SplitHigh(head1, head2);
		else SplitLow(head1, head2);
	} else {
		/* sequence increases */	
		if(thread < partner) SplitLow(head1, head2);
		else SplitHigh(head1, head2);	
	}	  

	Barrier();
	if(thread == 0) {
		/* swap pointer to lists */
		int* swap = list;
		list = tmp;
		tmp = swap;
	} 
	Barrier();

# ifdef DEBUG
	/* print intermediate lists */
	if(thread == 0) PrintIntermediate(andBit, phase);
# endif
	
} /* BitonicSort */

/*-------------------------------------------------------------------
 * Function:    ThreadWork
 * Purpose:     Synchronize phases of butterfly communication 
 *		amongst the threads.
 * In arg:      rank
 * Global var:  threadCount, barrierThreadCount, barrierMutex,
 *              proceed
 * Return val:  Ignored
 */
void *ThreadWork(void* rank) {
	long thread = (long) rank; 
	unsigned bitmask = 1;
    unsigned andBit = 2;
   	unsigned reverse;
    int partner, revPartner, phase, size;

    size = n/threadCount;

    /* q-sort logical partition */
	qsort(&list[thread*(size)], size, sizeof(int), Compare);
	Barrier();

    while(bitmask < threadCount) {
    	/* butterfly phase */
    	phase = 1;
    	/* partner */
    	partner = thread ^ bitmask; 
    	/* bitonic-sort */
    	BitonicSort(thread, partner, phase, andBit);

    	/* ENTER REVERSE BUTTERFLY */
    	reverse = bitmask;
    	while(reverse > 1) {
    		phase++;
    		/* right-shift */
    		reverse >>= 1; 
    		/* partner */
    		revPartner = thread ^ reverse;
    		/* bitonic-sort */
    		BitonicSort(thread, revPartner, phase, andBit);
    	}
    	/* EXIT REVERSE BUTTERFLY */

    	/* left-shift */
      	bitmask <<= 1;
      	/* left-shift */ 
      	andBit <<= 1; 
   	}
   	return NULL;
}  /* ThreadWork */

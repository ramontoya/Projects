/* File:     mpiPrimes.c
 * Author:   Raul Montoya
 *
 * Purpose:  Find all primes less than or equal to an input value.
 *           This version doesn't bother checking even ints.
 *
 * Input:    n:  integer >= 2 (from command line)
 * Output:   Sorted list of primes between 2 and n,
 *
 * Compile:  mpicc -g -Wall -o mp mpiPrimes.c -lm -DDEBUG
 * Usage:    mpiexec -n <number of processes> ./mp <n> 
 *              n:  max int to test for primality
 */
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <mpi.h>
#include <string.h>

void Usage(char prog[]);
int GetN(int argc, char* argv[]);
int IsPrime(int i);
int FindPrimes(int myRank, int n, int p, int* primes);
void PrintPartition(int list[], int n, int my_rank);
void Communicate(int p, int myRank, int* primes, int* size, 
  MPI_Comm comm);
int ComputeRecvSize(int bitmask, int partner, int* sizes, 
  int p);
void Merge(int** primes_p, int* prime_count_p, 
  int received_primes[], int received_count, int** temp_p);
void PrintPhase(int list[], int n, int my_rank, int phase);

const int STRING_MAX = 10000; 

int main(int argc, char* argv[]) {
  int      p, myRank, n, i;
  int      *primes, *size;
  MPI_Comm comm;

  MPI_Init(&argc, &argv);
  comm = MPI_COMM_WORLD;
  MPI_Comm_size(comm, &p);
  MPI_Comm_rank(comm, &myRank);

  // convert n to int
  if(myRank == 0) n = GetN(argc, argv);

  // broadcast n 
  MPI_Bcast(&n, 1, MPI_INT, 0, comm); 

  // malloc for primes
  primes = malloc((n/2*p)+2*sizeof(int));
  
  // find values and populate primes 
  i = FindPrimes(myRank, n, p, primes);
  
  # ifdef DEBUG
  // print intial primes
  PrintPartition(primes, i, myRank); 
  fflush(stdout);
  # endif

  // malloc for size
  size = malloc(p*sizeof(int)); 

  // gather sizes of primes, into size
  MPI_Allgather(&i, 1, MPI_INT, size, 1, MPI_INT, comm); 

  // tree-structured communication
  Communicate(p, myRank, primes, size, comm); 


  free(primes);
  free(size);
  MPI_Finalize();
  return 0;
}  /* main */

/*-------------------------------------------------------------------
 * Function:  PrintPartition
 * Purpose:   Convert a list of ints to a single string before
 *            printing. This should make it less likely that the
 *            output is interrupted by another process. This is
 *            mainly intended for debugging purposes.
 * In args:   list:  the ints to be printed
 *            n:  the number of ints
 *            my_rank:  the usual MPI variable
 */
void PrintPartition(int list[], int n, int my_rank) {
   char string[STRING_MAX];
   char* s_p;
   int i;

   sprintf(string, "Proc %d > After search primes are ", my_rank);
   // Pointer arithmetic:  make s_p point to the character strlen(string)
   // into string; i.e., make it point at the `\0'
   s_p = string + strlen(string);
   for (i = 0; i < n; i++) {
      sprintf(s_p, "%d ", list[i]);
      s_p = string + strlen(string);
   }

   printf("%s\n", string);
   fflush(stdout);
}  /* PrintPartition */

/*-------------------------------------------------------------------
 * Function:  PrintPhase
 * Purpose:   Convert a list of ints to a single string before
 *            printing. This should make it less likely that the
 *            output is interrupted by another process. This is
 *            mainly intended for debugging purposes.
 * In args:   list:  the ints to be printed
 *            n:  the number of ints
 *            my_rank:  the usual MPI variable
 */
void PrintPhase(int list[], int n, int my_rank, int phase) {
   char string[STRING_MAX];
   char* s_p;
   int i;

   sprintf(string, "Proc %d > After Phase %d primes are ", 
    my_rank, phase);
   // Pointer arithmetic:  make s_p point to the character strlen(string)
   // into string; i.e., make it point at the `\0'
   s_p = string + strlen(string);
   for (i = 0; i < n; i++) {
      sprintf(s_p, "%d ", list[i]);
      s_p = string + strlen(string);
   }

   printf("%s\n", string);
   fflush(stdout);
}  /* PrintPhase */

/*-------------------------------------------------------------------
 * Function:  Usage
 * Purpose:   Print a brief message explaining how the program is run.
 *            Then quit.
 * In arg:    prog:  name of the executable
 */
void Usage(char prog[]) {
   fprintf(stderr, "usage: %s <n>\n", prog);
   fprintf(stderr, "   n = max integer to test for primality\n");
   exit(0);
}  /* Usage */

/*-------------------------------------------------------------------
 * Function:    GetN
 * Purpose:     Get the input value n
 * Input args:  argc:  number of command line args
 *              argv:  array of command line args
 */
int GetN(int argc, char* argv[]) {
   int n;

   if (argc != 2) {
	Usage(argv[0]);
	MPI_Finalize();	
	exit(0);
   }
   
   n = strtol(argv[1], NULL, 10); /* Convert string to int */

   if (n <= 1) { /* Check for bogus input */ 
	Usage(argv[0]);
	MPI_Finalize();
	exit(0);
   }

   return n;
}  /* GetN */

/*-------------------------------------------------------------------
 * Function:   IsPrime
 * Purpose:    Determine whether the argument is prime
 * Input arg:  i
 * Return val: true (nonzero) if arg is prime, false (zero) otherwise
 */
int IsPrime(int i) {
   int j;

   for (j = 2; j <= sqrt(i); j++)
      if (i % j == 0)
         return 0;
   return 1;
}  /* IsPrime */

/*-------------------------------------------------------------------
 * Function:   FindPrimes
 * Purpose:    Implement a cyclic partition to find the primes that
 *             correspond to each process.      
 * Input arg:  myRank:  process rank
 *             n:       the limit of incrementation
 *             p:       number of processes
 *             primes*: array in which to store primes
 *
 * Return val: i: total primes found
 */
int FindPrimes(int myRank, int n, int p, int* primes) {
  int elem, i = 0;

  if(myRank == 0) {
    primes[i] = 2; // add 2 to proc 0
    i++;
  }

  for(elem = 2*myRank + 3; elem <= n; elem = elem + 2*p) { 
    if(IsPrime(elem)) {
      primes[i] = elem; // cyclic partition of primes
      i++;
    }
  }
  
  return i;
}  /* FindPrimes */

/*-------------------------------------------------------------------
 * Function:    Merge
 * Purpose:     Merge two sorted lists
 * In/out arg:  primes_p:  on input one of the sorted lists
 *                  on return the combined list
 *              prime_count_p:  on input number of elements in primes_p
 *                  on return number of elements in updated primes_p
 * In args:     received_primes:  the other input list
 *              received_count:  size of received_primes
 *              temp_p:  temporary storage for the merged list
 */
void Merge(int** primes_p, int* prime_count_p, int received_primes[], 
  int received_count, int** temp_p) {
   
   int *a = *primes_p;
   int *c = *temp_p;
   int ai, bi, ci;

   ai = bi = ci = 0;
   while (ai < *prime_count_p && bi < received_count) 
      if (a[ai] <= received_primes[bi]) c[ci++] = a[ai++];
      else c[ci++] = received_primes[bi++];
   
   // append remaining original primes
   while (ai < *prime_count_p) c[ci++] = a[ai++]; 
      
   // append remaining received primes   
   while (bi < received_count) c[ci++] = received_primes[bi++]; 

   *primes_p = c; // swap pointers to arrays
   *prime_count_p = ci; // update size of primes
}  /* Merge */

/*-------------------------------------------------------------------
 * Function:    ComputeRecvSize
 * Purpose:     Find the sizes of the lists of primes each process
 *              will ultimately store. Also compute the maximum
 *              size list each process will receive in any one
 *              call to MPI_Recv.
 * Input args:  bitmask, parnter: usual message passing variables
 *              sizes: current sizes of primes list
 *              p: usual MPI variable
 */
int ComputeRecvSize(int bitmask, int partner, int* sizes, int p) {
  int others;

  if(bitmask > p - partner) others = p - partner;
  else others = bitmask;
  
  int i, recvSize = 0;

  /* Sum up the total number of primes on the processes
     ranging from partner to (partner + others). */
  for(i = 0; i < others; i++) recvSize += sizes[partner+i];

  return recvSize;
}  /* ComputeRecvSize */   

/*-------------------------------------------------------------------
 * Function:    Communicate
 * Purpose:     Implement tree-structured message passing amongst
 *              processes in order to gather all the primes, 
 *              sorted onto process 0.
 * Input args:  p, myRank, comm: usual MPI variables
 *              primes: current primes list
 *              size: current sizes of primes list
 */
void Communicate(int p, int myRank, int* primes, int* size, 
  MPI_Comm comm) {
  
  int partner, done, maxRecv, phase;
  unsigned bitmask = (unsigned) 1;

  done = maxRecv = phase = 0;
    
  while (!done && bitmask < p) {
      partner = myRank ^ bitmask;

      if (myRank < partner) { 
          if (partner < p) { 
              // receive size
              int recvSize = ComputeRecvSize(bitmask, partner, size, 
                p);    
              
              // maximum receive size
              if(maxRecv < recvSize) maxRecv = recvSize; 
              
              // malloc for receiving primes
              int* recvd = malloc(recvSize*sizeof(int)); 
              
              // receive
              MPI_Recv(recvd, recvSize, MPI_INT, partner, 0, 
                  comm, MPI_STATUS_IGNORE);

              // malloc for merged primes
              int* temp = malloc((size[myRank]+recvSize)*sizeof(int));
              
              // merge  
              Merge(&primes, &size[myRank], recvd, recvSize, &temp); 
              
#             ifdef DEBUG
              // print phase status
              PrintPhase(primes, size[myRank], myRank, phase); 
              fflush(stdout);
#             endif 
            }
            bitmask <<= 1;
            phase++; // track phases of communication 
        } else {
          // send
          MPI_Send(primes, size[myRank], MPI_INT, partner, 0, comm);
            
          done = 1; // idle process
        }
    }
# ifdef DEBUG
  // print max primes and max receive
  printf("Proc > %d Max primes = %d, max receive = %d\n", 
    myRank, size[myRank], maxRecv);
  fflush(stdout);
# endif 

}  /* Communicate */

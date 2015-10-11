/* File:     doublyLL.c
 * Author:   Raul Montoya
 *
 * Purpose:  Implement a sorted, doubly linked list with ops insert (at head),
 *           print, member, delete, freeList.
 * 
 * Input:    Single character lower case letters to indicate operators, 
 *           followed by arguments needed by operators.
 * Output:   Results of operations.
 *
 * Compile:  gcc -g -Wall -o dLL doublyLL.c
 * Run:      ./dLL
 *
 * Notes:
 *    1.  Repeated values are not allowed in the list
 *    2.  delete deletes the occurrence of a word
 *    3.  Program assumes a word will be entered when prompted
 *        for one.
 */
#include <stdio.h>
#include <stdlib.h>

struct listNodeS {
   char*    data;
   struct listNodeS* nextP;
   struct listNodeS* prevP;
};

struct doublyLL {
   struct listNodeS* headP;
   struct listNodeS* tailP;
};

int Member(struct doublyLL* list, char* word);
struct listNodeS* Insert(struct doublyLL* list, char* word);
struct listNodeS* Delete(struct doublyLL* list, char* word);
void Print(struct doublyLL* list);
struct listNodeS* FreeList(struct doublyLL* list); 
char GetCommand(void);
char*  GetWord(void);

/*-----------------------------------------------------------------*/
int main(void) {
   char command;
   char*  word = malloc(99*sizeof(char));
   struct doublyLL list;
   list.headP = NULL;
   list.tailP = NULL;
   
   /* start with empty list */
   command = GetCommand();
   while (command != 'q' && command != 'Q') {
      switch (command) {
         case 'i': 
         case 'I': 
            word = GetWord();
            list.headP = Insert(&list, word);
            break;
         case 'p':
         case 'P':
            Print(&list);
            break;
         case 'm': 
         case 'M':
            word = GetWord();
            if (Member(&list, word))
               printf("%s is in the list\n", word);
            else
               printf("%s is not in the list\n", word);
            break;
         case 'd':
         case 'D':
            word = GetWord();
            list.headP = Delete(&list, word);
            break;
         case 'f':
         case 'F':
            list.headP = FreeList(&list);
            break;
         default:
            printf("There is no %c command\n", command);
            printf("Please try again\n");
      }
      command = GetCommand();
   }
   free(word);
   list.headP = FreeList(&list);
   return 0;
}  /* main */

/*-----------------------------------------------------------------
 * Function:    Member
 * Purpose:     search list for word
 * Input args:  headP:  pointer to head of list
 *              word:  word to search for
 * Return val:  1 if word is in list, 0 otherwise
 */
int Member(struct doublyLL* list, char* word) {
   struct listNodeS* currP = list->headP;

   while (currP != NULL) {
      if (strcmp(currP->data, word) == 0){	  // found word
         return 1;
      } else{
         if(strcmp(currP->data, word) > 0) { // hit successor of word 
            return 0;
         }
         currP = currP->nextP;
      }
   }
   return 0;
}  /* Member */

/*-----------------------------------------------------------------
 * Function:   Delete
 * Purpose:    Delete the first occurrence of word from list
 * Input args: headP: pointer to the head of the list
 *             word:    word to be deleted
 * Return headP: Possibly updated pointer to head of list
 */
struct listNodeS* Delete(struct doublyLL* list, char* word) {
   struct listNodeS* currP = list->headP;

   while (currP != NULL) {  
      
      if(currP->prevP == NULL && currP->nextP == NULL) {// list contains one node
         if(strcmp(currP->data, word) == 0) { // word matches
            free(currP->data);
            free(currP);
            list->headP = NULL;
            return list->headP;
         }
         printf("%s is not in the list\n", word); // word does not match
         return list->headP;
      }

      if (strcmp(currP->data, word) == 0) {  // found word to delete   
         break;

      } else if (strcmp(currP->data, word) > 0) { // hit successor of word
      		printf("%s is not in the list\n", word);
            return list->headP;
      }
      currP = currP->nextP;
    }
      
   if (currP == NULL) {                      // word was not found
      printf("%s is not in the list\n", word); 
      return list->headP;
   
   }  else if (currP->prevP == NULL) {         // delete from head
        list->headP = currP->nextP;
      	list->headP->prevP = NULL;

   } else {
      if(currP->nextP != NULL) {            // delete from body 
         currP->prevP->nextP = currP->nextP;
         currP->nextP->prevP = currP->prevP;

      } else {                                // delete from tail
         list->tailP = currP->prevP;
         list->tailP->nextP = NULL;
      }
   }

   free(currP->data);
   free(currP);
   return list->headP;
}  /* Delete */

/*-----------------------------------------------------------------
 * Function:   Insert
 * Purpose:    Insert word into list
 * Input args: headP: pointer to head of list
 *             word:  new word to be inserted
 * Return headP: Pointer to head of list
 */
struct listNodeS* Insert(struct doublyLL* list, char* word) {
   struct listNodeS* tempP = malloc(sizeof(struct listNodeS));
   struct listNodeS* currP = malloc(sizeof(struct listNodeS));
   tempP->data = malloc(strlen(word)+1*sizeof(char));
   currP->data = malloc(strlen(word)+1*sizeof(char));
   tempP->data = word;

   if(list->headP == NULL) {                  // list is empty
      tempP->prevP = NULL;
      tempP->nextP = NULL;
      list->headP = tempP;
      list->tailP = tempP;

   } else {                                   // list is not empty 
      currP = list->headP;
      while(currP != NULL) { 
         if(strcmp(currP->data, word) > 0) { // found appropriate position to store node
            break;
         } else if (strcmp(currP->data, word) == 0) { // word already exists
         	printf("%s, is already in the list\n", word);
      		return list->headP;
         }

         if(currP->nextP == NULL) {         // insert at tail
            currP->nextP = tempP;
            tempP->prevP = currP;
            tempP->nextP = NULL;
            list->tailP = tempP;
            return list->headP;
         }
         currP = currP->nextP;
      }

      if(currP->prevP == NULL) {            // insert at head
         tempP->nextP = list->headP;
         list->headP->prevP = tempP; 
         tempP->prevP = NULL;
         list->headP = tempP;
         
      } else {                                // insert in body
         currP->prevP->nextP = tempP;
         tempP->prevP = currP->prevP;
         tempP->nextP = currP;
         currP->prevP = tempP;
      }
   }
   return list->headP;
}   /* Insert */

/*-----------------------------------------------------------------
 * Function:   Print
 * Purpose:    Print list on a single line of stdout
 * Input arg:  headP
 */
void Print(struct doublyLL* list) {
   struct listNodeS* currP = list->headP;

   printf("list = ");
   while (currP != NULL) {
      printf("%s ", currP->data);
      currP = currP->nextP;
   }  
   printf("\n");
}  /* Print */

/*-----------------------------------------------------------------
 * Function:    FreeList
 * Purpose:     free each node in the list
 * Input arg:   headP:  pointer to head of list
 * Return val:  NULL pointer
 * Note:        headP is set to NULL on completion, indicating
 *              list is empty.
 */
struct listNodeS* FreeList(struct doublyLL* list) {
   struct listNodeS* currP;
   struct listNodeS* tempP;
   currP = list->headP;
   
   while (currP != NULL) {
      tempP = currP;
      currP = currP->nextP;
      free(tempP->data);
      free(tempP);     
   }

   list->headP = NULL;
   return list->headP;
}  /* FreeList */

/*-----------------------------------------------------------------
 * Function:      GetCommand
 * Purpose:       Get a single character command from stdin
 * Return value:  the first non-whitespace character from stdin
 */
char GetCommand(void) {
   char c;

   printf("Please enter a command (i, p, m, d, f, q):  ");
   /* Put the space before the %c so scanf will skip white space */
   scanf(" %c", &c);
   return c;
}  /* GetCommand */

/*-----------------------------------------------------------------
 * Function:   GetWord
 * Purpose:    Get a word from stdin
 * Return value:  the next int in stdin
 * Note:       Behavior unpredictable if an word isn't entered
 */
char*  GetWord(void) {
   char* word = malloc(99*sizeof(char));

   printf("Please enter a word:  ");
   scanf("%s", word);
   return word;
}  /* GetWord */
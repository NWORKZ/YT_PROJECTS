//
//  stack.h
//  das
//
//  Created by Neil Immanuel De Guzman on 26/04/2019.
//  Copyright © 2019 Neil Immanuel De Guzman. All rights reserved.
//

#ifndef stack_h
#define stack_h

#include <stdlib.h>
#include <stdio.h>

//structure of strack
typedef struct Stack{
    int size;//size
    int top;//index
    void** data;//pointer array of void pointer
}Stack;

Stack stack(int size){
    Stack stack;
    
    if(size == 0){
        stack.size = 10;
    }else{
        stack.size = size;
    }
    
    stack.top = size - 1;
    stack.data = malloc(sizeof(void*) * size);//long(8) * size(10) = 80bytes
    
    return stack;
}

void push(Stack *stack, void* d){
    if(stack->top < 0){
        fprintf(stderr,"Stack Overflow..\n");
        free(stack->data);
        exit(EXIT_FAILURE);
    }else{
        stack->data[stack->top--] = d;
    }
}

void* pop(Stack *stack){
    int index = ++stack->top;
    void* d = NULL;
    
    if(index >= stack->size){
        fprintf(stderr,"Stack Underflow..\n");
        free(stack->data);
        exit(EXIT_FAILURE);
    }else{
        d = stack->data[index];
        stack->data[index] = NULL;
    }
    
    return d;
}
#endif /* stack_h */

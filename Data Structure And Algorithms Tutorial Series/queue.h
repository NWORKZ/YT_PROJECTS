//
//  queue.h
//  das
//
//  Created by Neil Immanuel De Guzman on 27/04/2019.
//  Copyright © 2019 Neil Immanuel De Guzman. All rights reserved.
//

#ifndef queue_h
#define queue_h

#include <stdlib.h>
#include <stdio.h>

//structure of strack
typedef struct Queue{
    int size;//size
    int last;//index
    void** data;//pointer array of void pointer
}Queue;

Queue queue(int size){
    Queue queue;
    
    if(size == 0){
        queue.size = 10;
    }else{
        queue.size = size;
    }
    
    queue.last = 0;
    queue.data = malloc(sizeof(void*) * size);//long(8) * size(10) = 80bytes
    
    return queue;
}

void enqueue(Queue *queue, void *d){
    
    if(queue->last >= queue->size){
        fprintf(stderr,"Stack Overflow..\n");
        free(queue->data);
        exit(EXIT_FAILURE);
    }else{
        queue->data[queue->last++] = d;
    }
}

void* dequeue(Queue *queue){
    void* d = queue->data[0];
    
    if(queue->last < 0){
        fprintf(stderr,"Stack Underflow..\n");
        free(queue->data);
        exit(EXIT_FAILURE);
    }else{
        for(int i = 0; i < queue->last; i++){
            queue->data[i] = queue->data[i + 1];
        }
        
        queue->data[queue->last--] = NULL;
    }
    return d;
}

void* peek(Queue *queue){
    return queue->data[0];
}

#endif /* queue_h */

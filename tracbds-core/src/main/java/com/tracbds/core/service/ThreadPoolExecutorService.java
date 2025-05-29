package com.tracbds.core.service;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class ThreadPoolExecutorService {

   private ThreadPoolExecutor threadPoolExecutor;


    @PostConstruct
   public void init(){
        int coreThreads = Runtime.getRuntime().availableProcessors();
        int maxThreads = coreThreads * 4;
        threadPoolExecutor = new ThreadPoolExecutor(coreThreads, maxThreads, 5L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
   }

   public void submit(Runnable task){
        this.threadPoolExecutor.execute(task);
   }
}

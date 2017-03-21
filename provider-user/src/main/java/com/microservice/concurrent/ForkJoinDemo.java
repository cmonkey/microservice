package com.microservice.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

// 分而治之策略，即将人物递归划分成更小的子任务，直到子任务足够小，从而能够被连续地处理掉为止
// 优势是处理过程可以适用并行发生，这种情况特别适合给予多核处理器的并行编程，根据java api 中的定义
// 分而治之的建议临界点定义在100-1000个操作中的某个位置
//
// ForkJoinTask 描述任务的抽象类
// ForkJoinPool 管理ForkJoinTask 的线程池
// RecursiveAction ForkJoinTask 子类，描述无返回值的任务
// RecursiveTask ForJoinTask 子类，描述有返回只的任务
public class ForkJoinDemo{

    public static void main (String [] args)throws Exception {

        ForkJoinPool pool = new ForkJoinPool();

        Future<Long> result = pool.submit(new MTask(0 , 1000001));
        System.out.println(result.get());

        pool.shutdown();

    }
}

class MTask extends RecursiveTask<Long> {
    static final int THRESHOLD = 1000;

    private int begin, end;

    public MTask(int begin, int end){
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Long compute(){
        long sum = 0;

        if((end - begin) <= THRESHOLD){
            for(int i = begin; i < end; i++){
                sum += i;
            }
        }else{
            int mid = (begin + end) / 2;

            MTask left = new MTask(begin, mid);

            left.fork();

            MTask right = new MTask(mid + 1, end);

            right.fork();

            Long lr = left.join();

            System.out.println(begin + "-" + mid + ":" + lr);

            Long rr = right.join();

            System.out.println((mid + 1)+ "-" + end + ":" + rr);

            sum = lr + rr;
        }

        return sum;
    }
}

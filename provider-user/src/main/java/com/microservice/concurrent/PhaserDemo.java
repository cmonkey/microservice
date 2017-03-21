package com.microservice.concurrent;

import java.util.concurrent.Phaser;

// 同步器Phaser
// 工作方式与CyclicBarrier 类似，但是可以定义多个阶段
// Phaser()/Phaser(int num) 使用定义0/num个party创建Phaser
// register(); 注册party
// arriveAndAdvance() 到达等待到所有party到达
// arriveAndDeregister() 到达时注销线程自己
public class PhaserDemo{

    public static void main (String [] args) {

        Phaser phaser = new Phaser(1);

        System.out.println("starting...");

        new Worker("服务器", phaser).start();
        new Worker("厨师", phaser).start();
        new Worker("上菜员", phaser).start();

        for (int i = 0 ; i <= 3;  i++) {
            phaser.arriveAndAwaitAdvance();
            System.out.println("Order " + i + "finished!");
        }

        phaser.arriveAndDeregister();

        System.out.println("All done!");
    }
}

class Worker extends Thread{
    private Phaser phaser;

    public Worker(String name, Phaser phaser){
        super(name);
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run(){
        for (int i = 0 ; i < 3; i++) {
            System.out.println("current order is: " + i + ":" + getName());

            if(3 == i){
                phaser.arriveAndDeregister();
            }else{
                phaser.arriveAndAwaitAdvance();
            }

            try {
                Thread.sleep(1000);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}

// 首先，构造函数中要将自己注册到phaser当中
// 其次，处理完成则arriveAndDeregister, 处理未完成arriveAndAwaitAdvance
// 还有， 有三个订单，对于每个订单必须所有人处理完毕之后，才能继续执行
// 最后，解除注册线程

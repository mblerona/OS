package Synchronization;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ProducerController {
    public static int NUM_RUN=50;
    static Semaphore accessBuffer;
    static Semaphore Lock;
    static Semaphore canCheck;
    public static void init(){
        accessBuffer =new Semaphore(1);
        Lock=new Semaphore(1);
        canCheck= new Semaphore(10);

    }

    public static class Buffer{
        public int numChecks=0;
        public void produce(){
            System.out.println("Producer is producing..");

        }

        public void check(){
            System.out.println("controller is checking..");
        }

        public static class Producer extends Thread{
            private final Buffer buffer;
            public Producer (Buffer b){
                this.buffer=b;
            }


            public void execute() throws InterruptedException{
                       accessBuffer.acquire();
                       this.buffer.produce();
                       accessBuffer.release();
            }

            public void run(){
                for(int i=0; i<NUM_RUN; i++){
                    try{
                        execute();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        public static class Controller extends Thread{
            private final Buffer buffer;
            public Controller(Buffer b){
                this.buffer=b;
            }

            public void execute() throws InterruptedException{
                  Lock.acquire();
                 if(this.buffer.numChecks==0){
                     accessBuffer.acquire();

                 }
                this.buffer.numChecks++;
                 Lock.release();


                 canCheck.acquire();
                this.buffer.check();

                Lock.acquire();
                this.buffer.numChecks--;
                canCheck.release();


                if(this.buffer.numChecks==0){
                    accessBuffer.release();
                }
                Lock.release();




            }
            public void run(){
                for(int i=0; i<NUM_RUN; i++){
                    try{
                        execute();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }


        }
    }
}

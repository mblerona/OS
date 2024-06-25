package Synchronization;

import java.util.HashSet;
import java.util.concurrent.Semaphore;
//Volleyball tournament
//        (35 поени) You need to implement a system for synchronizing a volleyball tournament according to the following rules:
//
//        There are 60 volleyball players participating to the tournament grouped in arbitrary teams. At most 12 players can enter in the tournament arena at once. After the enterence in the arena, each player should print Player inside.. Then the players should change their clothes in a dressing room with capacity of 4, meaning that at most 4 players can be in the dressing room at once. When the players enter in the dressing room, they should print In dressing room.. When a player exits from the dressing room, it waits for the other players in order to start with the game. After all players are ready, the game can start, and every player should print Game started.. After the game is over, all players should print Player done., and the last one should print Game finished., denoting that the arena is free for another game. Then, another 12 players can enter in the arena.
//
//        The classes VolleyballTournament and Payer are given in the starter code below. In the main method from the class VolleyballTournament you should start 60 volleyball players, which are represented with the class Payer. Then, each of the players should start executing the previously defined scenario in background. The players behavior should be implemented in the execute method from the Payer class, which should be executed in parallel for all players. After starting all players, the main method should wait for each of the players to finish for 2 seconds (2000 ms). If some of the players does not finish for 2 seconds, you should print Possible deadlock! and terminate the thread, and if all payers finished in the given time, you should print Tournament finished..
//
//        Your task is to complete the code provided below according to the requirements, and be careful not to create a Race Condition or a Deadlock.
public class Volleyball {


    public static void main(String[] args) throws InterruptedException {
        HashSet<Player> threads = new HashSet<>();
        for(int i=0;i<12;i++){
            Player p=new Player();
            threads.add(p);
        }

         for (Thread thread:threads){
             thread.start();
         }
        for (Thread thread:threads){
            thread.join(2000);
        }

        for(Thread thread: threads){
            if(thread.isAlive()){
                System.out.println("Possible Deadlock");
                thread.stop();
            }
        }




    }


        public static int nrPlayers=0;
        public static Semaphore nrPlayersLock=new Semaphore(1);
        public static Semaphore enterArena=new Semaphore(12);
        public static Semaphore enterDressing=new Semaphore(4);
        public static Semaphore leaveGame= new Semaphore(0);

    static class Player extends Thread{


        public void execute() throws InterruptedException {
            enterArena.acquire();
               System.out.println("Player inside arena..");
               System.out.println("-----------------------");
            enterDressing.acquire();
               System.out.println("Player entered Dressing room");
               System.out.println("-----------------------");
               Thread.sleep(10000);

               nrPlayersLock.acquire();
               nrPlayers++;
               enterDressing.release(); //leave room after ready

            if(nrPlayers==12){
                System.out.println("Game started.. ");
                leaveGame.release(12);
            }

            nrPlayersLock.release();

            leaveGame.acquire();
            nrPlayersLock.acquire();
            nrPlayers--;
            System.out.println("Player done");


            if(nrPlayers==0){
                System.out.println("Game finished");
                enterArena.release(12);
            }

            nrPlayersLock.release(12);


        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}


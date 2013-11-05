package TRASH;

/**
 * Created with IntelliJ IDEA.
 * User: ivanPC
 * Date: 05.11.13
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public class NewThread extends Thread {
    String name;
    //Thread t;
    boolean suspendFlag;

    public NewThread(String name) {
        this.name = name;
        //t = new Thread(this, name);
        System.out.println("Новый поток: " + name);
        suspendFlag = false;
        //t.start();
    }

    public void run() {
        try {
            //Какие то действия
            for (int i = 15; i > 0; i--) {
                System.out.println(name + ": " + i);
                Thread.sleep(200);
                //проверка условия
                synchronized (this) {
                    while (suspendFlag) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println(name + " прерван.");
        }
        System.out.println(name + " завершен.");
    }

    public void mySuspend() {
        suspendFlag = true;
    }

    synchronized void myResume() {
        suspendFlag = false;
        notify();
    }

    public static void main(String[] args) throws InterruptedException {
        NewThread thread1 = new NewThread("Один");
        thread1.start();
        try {
            Thread.sleep(1000);
            thread1.mySuspend();
            System.out.println("Приостановка потока Один");
            Thread.sleep(7000);
            thread1.myResume();
            System.out.println("Возобновление потока Один");
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }
    }
}

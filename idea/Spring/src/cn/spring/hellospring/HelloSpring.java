package cn.spring.hellospring;

/**
 * Created by fic on 2019.03.30 09:05
 * Package:spring0330.cn.spring.hellospring
 * Desc:
 */
public class HelloSpring {
    private String who;

    public void setWho(String who) {
        this.who = who;
    }

    public void print() {
        System.out.println("hello" + who);
    }
}

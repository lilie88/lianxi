package cn.spring.factory;

/**
 * Created by fic on 2019.03.30 08:45
 * Package:spring0330.cnspring.factory
 * Desc:
 */
public class Cat implements Animal {
    @Override
    public void eat() {
        System.out.println("爱吃鱼");
    }

    @Override
    public void sleep() {
        System.out.println("打呼呼");
    }
}

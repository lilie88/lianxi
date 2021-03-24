package cn.spring.factory;

/**
 * Created by fic on 2019.03.30 08:46
 * Package:spring0330.cnspring.factory
 * Desc:
 */
public class Dog implements Animal {

    private String name;

    public Dog(String name) {
        this.name = name;
    }

    @Override
    public void eat() {
        System.out.println("啃骨头");
    }

    @Override
    public void sleep() {
        System.out.println("不睡");
    }
}

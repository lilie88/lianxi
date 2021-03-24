package cn.spring.factory;

/**
 * Created by fic on 2019.03.30 08:47
 * Package:spring0330.cnspring.factory
 * Desc:
 */
public class Factory {

    public Animal getInstance(String s) {
        return "cat".equals(s) ? new Cat() : new Dog("");
    }
}

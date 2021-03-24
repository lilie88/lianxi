package cn.spring;

import cn.spring.factory.Animal;
import cn.spring.factory.Factory;
import cn.spring.hellospring.HelloSpring;
import cn.spring.logger.entity.User;
import cn.spring.logger.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by fic on 2019.03.30 08:49
 * Package:spring0330.cn.spring
 * Desc:
 */
public class Test {

    @org.junit.Test
    public void testFactory() {
        Factory factory = new Factory();
        Animal cat = factory.getInstance("cat");
        cat.eat();
        cat.sleep();
    }

    @org.junit.Test
    public void testHelloSpring() {
        //1.找到xml配置
        ApplicationContext context =
                new ClassPathXmlApplicationContext("ApplicationContext.xml");//配置文件

        //2.调用对应的Bean,拿到我们需要的对象  控制反转IoC和依赖注入DI
        HelloSpring helloSpring = (HelloSpring) context.getBean("helloSpring");//这个参数就是配置文件中bean标签中的id值

        /* HelloSpring helloSpring1 = new HelloSpring();*/

        //3.调用对象中的helloSpring方法
        helloSpring.print();
    }

    /**
     * test logger
     */

    @org.junit.Test
    public void testLogger() {
        //1.找到xml配置
        ApplicationContext context =
                new ClassPathXmlApplicationContext("ApplicationContextAop.xml");

        //2.调用对应的Bean,拿到我们需要的对象  控制反转IoC和依赖注入DI
        UserService userService = (UserService) context.getBean("userService");

        User user = (User) context.getBean("user");

        //3.调用对象中的helloSpring方法
        userService.addNewUser(user);
    }
}

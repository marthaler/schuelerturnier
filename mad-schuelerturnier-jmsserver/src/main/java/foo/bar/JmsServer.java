package foo.bar;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JmsServer {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

        System.out.println("...");

        try {
            Thread.sleep(55555);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

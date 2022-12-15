package application.main;

import application.config.SpringConfig;
import application.menu.Menu;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        Menu menu = context.getBean("menu", Menu.class);
        menu.createMenu(1L, 1L);
    }
}

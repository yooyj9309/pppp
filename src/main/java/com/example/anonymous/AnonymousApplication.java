package com.example.anonymous;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@SpringBootApplication
public class AnonymousApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnonymousApplication.class, args);
    }
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/"); // HTML 파일 위치
        templateResolver.setSuffix(".html"); // HTML 확장명 사용
        templateResolver.setTemplateMode(TemplateMode.HTML); // HTML5 값은 비권장 됨
        templateResolver.setCacheable(false); // 캐시 사용 안함(사용하면 html 수정시 서버 재기동 필요)

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true); // Spring EL 사용

        return templateEngine;

    }



    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setCharacterEncoding("UTF-8"); // 한글 깨짐 방지
        viewResolver.setTemplateEngine(templateEngine());

        return viewResolver;
    }
}

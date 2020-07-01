package next.dao;

import core.di.factory.BeanFactory;
import core.jdbc.ConnectionManager;
import core.util.ReflectionUtils;
import next.model.Answer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.Set;

import static core.mvc.DispatcherServlet.ALL_TARGET_TYPES;
import static org.assertj.core.api.Assertions.assertThat;

public class AnswerDaoTest {
    private static final Logger log = LoggerFactory.getLogger(AnswerDaoTest.class);

    private BeanFactory beanFactory;

    @BeforeEach
    public void setup() {
        Reflections reflections = new Reflections("next.controller");
        Set<Class<?>> preInstanticateClazz = ReflectionUtils.getTypesAnnotatedWith(reflections, ALL_TARGET_TYPES);
        beanFactory = new BeanFactory(preInstanticateClazz);
        beanFactory.initialize();

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void addAnswer() throws Exception {
        long questionId = 1L;
        Answer expected = new Answer("javajigi", "answer contents", questionId);
        AnswerDao dut = beanFactory.getBean(AnswerDao.class);
        Answer answer = dut.insert(expected);
        log.debug("Answer : {}", answer);
        assertThat(answer).isNotNull();
    }
}

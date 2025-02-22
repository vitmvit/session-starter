package by.vitikova.session.processor;

import by.vitikova.session.annotation.SessionManagement;
import by.vitikova.session.client.SessionClient;
import by.vitikova.session.property.SessionManagementProperty;
import by.vitikova.session.proxy.SessionManagementInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Процессор бинов, который управляет сессиями для компонентов с аннотацией {@link SessionManagement}.
 * <p>
 * Создает прокси для бинов, содержащих методы с аннотацией {@link SessionManagement}.
 * Позволяет внедрять дополнительное поведение в управление сессиями.
 */
public class SessionManagementBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private final Map<String, Class<?>> annotatedMethods = new HashMap<>();
    private BeanFactory beanFactory;

    /**
     * Обрабатывает бины до их инициализации, записывая в карту имена бинов,
     * которые содержат методы с аннотацией {@link SessionManagement}.
     *
     * @param bean     экземпляр бина
     * @param beanName имя бина
     * @return исходный экземпляр бина
     * @throws BeansException если происходит ошибка при обработке бина
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(SessionManagement.class))
                .forEach(method -> annotatedMethods.put(beanName, clazz));
        return bean;
    }

    /**
     * Обрабатывает бины после их инициализации, создавая прокси-экземпляры
     * для бинов с аннотированными методами.
     *
     * @param bean     экземпляр бина
     * @param beanName имя бина
     * @return прокси-экземпляр бина или оригинальный бин, если аннотация отсутствует
     * @throws BeansException если происходит ошибка при обработке бина
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return Optional.ofNullable(annotatedMethods.get(beanName))
                .map(clazz -> getSessionProxy(bean))
                .orElse(bean);
    }

    /**
     * Устанавливает {@link BeanFactory} для текущего процесса.
     *
     * @param beanFactory {@link BeanFactory} для управления зависимостями
     * @throws BeansException если происходит ошибка при установке фабрики бинов
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Создает прокси для указанного бина, используя {@link Enhancer}.
     *
     * @param bean экземпляр бина, для которого создается прокси
     * @return прокси-экземпляр бина
     */
    private Object getSessionProxy(Object bean) {
        var sessionProviderCommunicator = beanFactory.getBean(SessionClient.class);
        var sessionManagerProperties = beanFactory.getBean(SessionManagementProperty.class);

        var enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback(new SessionManagementInterceptor(bean, sessionProviderCommunicator, sessionManagerProperties, beanFactory));

        return isPresentDefaultConstructor(bean)
                ? enhancer.create()
                : enhancer.create(getNotDefaultConstructorArgTypes(bean), getNotDefaultConstructorArgs(bean));
    }

    /**
     * Проверяет, есть ли у бина конструктор по умолчанию.
     *
     * @param bean экземпляр бина
     * @return true, если конструктор по умолчанию присутствует, иначе false
     */
    private boolean isPresentDefaultConstructor(Object bean) {
        return Arrays.stream(bean.getClass().getConstructors()).anyMatch(constructor -> constructor.getParameterCount() == 0);
    }

    /**
     * Получает типы аргументов конструктора, отличные от конструктора по умолчанию.
     *
     * @param object экземпляр объекта, для которого нужно получить аргументы конструктора
     * @return массив классов аргументов конструктора
     */
    private Class<?>[] getNotDefaultConstructorArgTypes(Object object) {
        return Arrays.stream(object.getClass().getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .map(Constructor::getParameterTypes)
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Получает аргументы для конструктора, отличные от конструктора по умолчанию.
     *
     * @param object экземпляр объекта, для которого нужно получить аргументы конструктора
     * @return массив аргументов для конструктора
     */
    private Object[] getNotDefaultConstructorArgs(Object object) {
        Class<?>[] constructorArgTypes = getNotDefaultConstructorArgTypes(object);
        return Arrays.stream(constructorArgTypes).map(beanFactory::getBean).toArray();
    }
}
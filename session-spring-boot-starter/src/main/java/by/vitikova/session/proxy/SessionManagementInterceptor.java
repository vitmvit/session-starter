package by.vitikova.session.proxy;

import by.vitikova.session.SessionCreateDto;
import by.vitikova.session.SessionInfo;
import by.vitikova.session.annotation.SessionManagement;
import by.vitikova.session.client.SessionClient;
import by.vitikova.session.exception.AnnotationException;
import by.vitikova.session.property.SessionManagementProperty;
import by.vitikova.session.provider.BlackListProvider;
import by.vitikova.session.provider.impl.DefaultBlackListProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.vitikova.session.util.LoginUtil.*;

/**
 * Перехватчик методов, который управляет сессиями для методов с аннотацией {@link SessionManagement}.
 * <p>
 * Данный класс реализует {@link MethodInterceptor} и осуществляет проверку сессий
 * перед вызовом аннотированных методов, а также управление черными списками логинов.
 */
@RequiredArgsConstructor
public class SessionManagementInterceptor implements MethodInterceptor {

    private final Object targetBean;
    private final SessionClient sessionClient;
    private final SessionManagementProperty sessionManagerProperties;
    private final BeanFactory beanFactory;


    /**
     * Перехватывает вызов метода и управляет логикой сессий для методов с аннотацией
     * {@link SessionManagement}.
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (method.isAnnotationPresent(SessionManagement.class)) {
            SessionManagement annotation = method.getAnnotation(SessionManagement.class);
            var extractedLogin = extractLoginFromArgs(args, method.getParameters());

            var blackList = Optional.of(annotation).stream()
                    .map(annot -> getBlackListProviders(getResultSetProviders(annot)))
                    .map(blackListsFromProviders -> combineBlackLists(annotation.blackList(), blackListsFromProviders))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            return Stream.of(args)
                    .filter(SessionInfo.class::isInstance)
                    .map(SessionInfo.class::cast)
                    .map(SessionInfo::login)
                    .filter(login -> isLoginNotInBlackList(login, blackList))
                    .findFirst()
                    .map(login -> {
                        if (isSessionProvidedInParams(args)) {
                            return args;
                        } else {
                            throw new AnnotationException("Session not specified!");
                        }
                    })
                    .map(a -> sessionClient.getSession(new SessionCreateDto(extractedLogin)))
                    .map(session -> {
                        try {
                            return method.invoke(targetBean, substituteArgs(args, session));
                        } catch (IllegalAccessException | InvocationTargetException ex) {
                            throw new AnnotationException("Bad login: " + ex.getMessage());
                        }
                    })
                    .orElseThrow(() -> new AnnotationException("This login is blacklisted"));
        }
        return method.invoke(targetBean, args);
    }

    /**
     * Получает набор провайдеров черного списка на основе аннотации {@link SessionManagement}.
     *
     * @param annotation аннотация, содержащая данные о провайдерах черного списка
     * @return набор классов провайдеров черного списка
     */
    private Set<Class<? extends BlackListProvider>> getResultSetProviders(SessionManagement annotation) {
        Class<? extends BlackListProvider>[] listProviders = annotation.blackListProviders();
        return Stream.concat(Arrays.stream(listProviders), sessionManagerProperties.getBlackListProviders().stream())
                .filter(provider -> annotation.includeDefaultBlackListSource() || !provider.equals(DefaultBlackListProvider.class))
                .collect(Collectors.toSet());
    }

    /**
     * Получает объединенный черный список значений из предоставленных провайдеров.
     *
     * @param providers набор классов провайдеров черного списка
     * @return объединенный набор значений черного списка
     */
    private Set<String> getBlackListProviders(Set<Class<? extends BlackListProvider>> providers) {
        return providers.stream()
                .map(providerClass -> {
                    if (providerClass.equals(DefaultBlackListProvider.class)) {
                        return beanFactory.getBean(DefaultBlackListProvider.class).getBlackList();
                    } else {
                        return  beanFactory.getBean(providerClass).getBlackList();
                    }
                })
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    /**
     * Объединяет черные списки из параметров и набора значений.
     *
     * @param paramBlackList массив значений черного списка из параметров
     * @param blackLists     набор значений черного списка
     * @return объединенный набор значений черного списка
     */
    private Set<String> combineBlackLists(String[] paramBlackList, Set<String> blackLists) {
        return Stream.concat(Arrays.stream(paramBlackList), blackLists.stream()).collect(Collectors.toSet());
    }
}
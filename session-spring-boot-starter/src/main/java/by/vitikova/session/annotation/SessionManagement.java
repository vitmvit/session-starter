package by.vitikova.session.annotation;

import by.vitikova.session.provider.BlackListProvider;
import by.vitikova.session.provider.impl.DefaultBlackListProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, используемая для управления сессиями в приложении.
 * <p>
 * Эта аннотация позволяет указать настройки для обработки черных списков (black list),
 * которые будут применяться в определенных методах.
 *
 * <p>Возможные параметры аннотации:</p>
 *
 * <ul>
 *     <li>blackList - массив строк, представляющий настраиваемые черные списки для управления сессией.</li>
 *     <li>includeDefaultBlackListSource - флаг, указывающий, включать ли источник черного списка по умолчанию. По умолчанию этот параметр установлен в true.</li>
 *     <li>blackListProviders - массив классов, представляющий провайдеры черного списка. Класс DefaultBlackListProvider используется по умолчанию.</li>
 * </ul>
 *
 * <p>Аннотация может быть применена только к методам.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionManagement {

    String[] blackList() default {};

    boolean includeDefaultBlackListSource() default true;

    Class<? extends BlackListProvider>[] blackListProviders() default DefaultBlackListProvider.class;
}
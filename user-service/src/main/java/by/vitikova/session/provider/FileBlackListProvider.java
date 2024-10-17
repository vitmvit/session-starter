package by.vitikova.session.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Провайдер для загрузки черного списка из файла.
 * <p>
 * Этот класс реализует интерфейс {@link BlackListProvider} и
 * предоставляет метод для получения черного списка пользователей
 * из файла, путь к которому задается в конфигурации.
 */
@Slf4j
@Component
public class FileBlackListProvider implements BlackListProvider {

    private final String path;
    private final ResourceLoader loader;

    public FileBlackListProvider(@Value("${black-list.path}") String path) {
        this.path = path;
        this.loader = new DefaultResourceLoader();
    }

    /**
     * Получает черный список из файла.
     * <p>
     * Метод читает содержимое файла и возвращает набор строк,
     * представляющий черный список. Если возникла ошибка при
     * чтении файла, возвращается пустой набор.
     *
     * @return набор строк, представляющий черный список
     */
    @Override
    public Set<String> getBlackList() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResource(path).getInputStream()))) {
            log.info("FileBlackListProvider: read file - " + path);
            return reader.lines().collect(Collectors.toSet());
        } catch (IOException ex) {
            log.error("FileBlackListProvider: exception - ", ex);
            return Collections.emptySet();
        }
    }
}
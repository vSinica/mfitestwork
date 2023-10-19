package ru.vados.mfitestwork;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import ru.vados.mfitestwork.config.TestAppConfig;
@SpringBootTest(classes = {TestAppConfig.class})
@Profile("test")
public abstract class AbstractTest {
}

package ru.vados.mfitestwork;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
                "/sql/ddl-before-test.sql",
               // "/sql/prepare-test-set1.sql"
        })
})
public class Test extends AbstractTest {




}

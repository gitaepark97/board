package board.backend.common.infra;

import board.backend.TestcontainersConfiguration;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Execution(ExecutionMode.SAME_THREAD)
@ActiveProfiles("test")
@Import({TestcontainersConfiguration.class, QueryDSLConfig.class})
@DataJpaTest
public abstract class TestRepository {

}

package board.backend.common.infra;

import board.backend.TestcontainersConfiguration;
import board.backend.common.infra.QueryDSLConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import({TestcontainersConfiguration.class, QueryDSLConfig.class})
@DataJpaTest
public abstract class TestRepository {

}

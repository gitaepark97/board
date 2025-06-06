package board.backend.infra;

import board.backend.TestcontainersConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import({TestcontainersConfiguration.class, QueryDSLConfig.class})
@DataJpaTest
abstract class TestRepository {

}

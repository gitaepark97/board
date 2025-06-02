package board.backend.support;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class IdProviderTest {

    @Test
    @DisplayName("ID가 정상적으로 생성된다")
    void nextId_success() {
        // given
        IdProvider idProvider = new IdProvider();

        // when
        Long id = idProvider.nextId();

        // then
        assertThat(id).isNotNull();
        assertThat(id).isGreaterThan(0);
    }

    @Test
    @DisplayName("여러 번 호출 시 ID가 중복되지 않는다")
    void nextId_multipleIdGeneration() {
        // given
        IdProvider idProvider = new IdProvider();
        Set<Long> ids = new HashSet<>();

        // when
        for (int i = 0; i < 1000; i++) {
            ids.add(idProvider.nextId());
        }

        // then
        assertThat(ids).hasSize(1000);
    }

    @Test
    @DisplayName("ID는 시간 순으로 증가한다")
    void nextId_idIsTimeOrdered() {
        // given
        IdProvider idProvider = new IdProvider();

        // when
        Long id1 = idProvider.nextId();
        Long id2 = idProvider.nextId();

        // then
        assertThat(id2).isGreaterThan(id1);
    }

}
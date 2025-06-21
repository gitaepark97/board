package board.backend.view.application.fake;

import board.backend.view.application.port.ArticleViewDistributedLockRepository;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FakeArticleViewDistributedLockRepository implements ArticleViewDistributedLockRepository {

    private final Map<String, Long> lockMap = new HashMap<>();

    @Override
    public Boolean lock(Long articleId, String ip, Duration ttl) {
        String key = articleId + "::" + ip;
        if (lockMap.containsKey(key)) {
            return false; // 이미 락이 걸려 있음
        }
        lockMap.put(key, System.currentTimeMillis() + ttl.toMillis());
        return true;
    }

    public void release(Long articleId, String ip) {
        String key = articleId + "::" + ip;
        lockMap.remove(key);
    }

}

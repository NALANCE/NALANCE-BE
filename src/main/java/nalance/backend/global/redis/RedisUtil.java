package nalance.backend.global.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveEmailVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 1, TimeUnit.DAYS);
    }

    public String getEmailVerificationCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void deleteEmailVerificationCode(String email) {
        redisTemplate.delete(email);
    }
}

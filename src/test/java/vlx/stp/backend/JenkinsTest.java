package vlx.stp.backend;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JenkinsTest {

    @SneakyThrows
    @Test
    public void test(){
        TimeUnit.SECONDS.sleep(5L);
        assertThat(2).isEqualTo(2);
    }
}

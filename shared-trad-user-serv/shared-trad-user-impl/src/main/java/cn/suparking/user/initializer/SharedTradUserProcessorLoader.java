package cn.suparking.user.initializer;

import cn.suparking.user.service.intf.UserLoginService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SharedTradUserProcessorLoader implements ApplicationRunner {

    private final UserLoginService userLoginService;

    public SharedTradUserProcessorLoader(final UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    /**
     * 获取wx access token.
     */
    protected void init() {
        userLoginService.getAccessToken();
    }

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        init();
    }
}

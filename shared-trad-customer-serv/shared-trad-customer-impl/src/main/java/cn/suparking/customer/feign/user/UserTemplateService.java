package cn.suparking.customer.feign.user;

import cn.suparking.user.api.beans.SessionKeyDTO;
import cn.suparking.user.api.vo.SessionKeyVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "shared-trad-user-serv", path = "/user-center/userLogin")
public interface UserTemplateService {

    /**
     * 通过code获取session_key.
     *
     * @param sessionKeyDTO {@linkplain SessionKeyDTO}
     * @return {@linkplain SessionKeyVO}
     */
    @PostMapping("/getSessionKey")
    SessionKeyVO getSessionKey(@RequestBody SessionKeyDTO sessionKeyDTO);
}

package cn.suparking.customer.feign.user;

import cn.suparking.customer.feign.user.fallback.UserTemplateFallbackFactory;
import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.api.vo.PhoneInfoVO;
import cn.suparking.user.api.vo.RegisterVO;
import cn.suparking.user.api.vo.SessionVO;
import cn.suparking.user.api.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "shared-trad-user-serv", path = "/user-center", fallbackFactory = UserTemplateFallbackFactory.class)
public interface UserTemplateService {

    /**
     * 通过code获取session_key.
     *
     * @param code wx code
     * @return {@linkplain RegisterVO}
     */
    @PostMapping("/userLogin/getSessionKey")
    SessionVO getSessionKey(@RequestBody String code);

    /**
     * get user by mini open id.
     * @param miniOpenId mini open id
     * @return {@link UserVO}
     */
    @GetMapping("/user/getUserByOpenId")
    UserVO getUserByOpenId(@RequestBody String miniOpenId);


    /**
     * 新建或者更新用户信息.
     * @param userDTO {@link UserDTO}
     * @return {@link Integer}
     */
    @PostMapping("/user/")
    Integer createSharedUser(@RequestBody UserDTO userDTO);

    /**
     * get phone by phonecode.
     * @param phoneCode phone code
     * @return {@link PhoneInfoVO}
     */
    @GetMapping("/userLogin/getPhoneInfo")
    PhoneInfoVO getPhoneInfo(@RequestParam("phoneCode") String phoneCode);
}

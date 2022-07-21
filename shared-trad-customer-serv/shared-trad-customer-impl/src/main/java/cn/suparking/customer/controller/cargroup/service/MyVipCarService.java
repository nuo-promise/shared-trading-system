package cn.suparking.customer.controller.cargroup.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.cargroup.VipCarQueryDTO;
import cn.suparking.customer.dao.vo.cargroup.MyVipCarVo;
import cn.suparking.customer.dao.vo.cargroup.ProjectVipCarVo;
import cn.suparking.customer.dao.vo.cargroup.ProtocolVipCarVo;

import java.util.List;

public interface MyVipCarService {

    /**
     * 获取当前用户所办的合约信息.
     *
     * @param vipCarQueryDTO {@linkplain VipCarQueryDTO}
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    List<MyVipCarVo> myVipCarList(VipCarQueryDTO vipCarQueryDTO);

    /**
     * 获取线上所有可办合约的场库列表.
     *
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    List<ProjectVipCarVo> projectVipCarList();

    /**
     * 获取线上所有可办合约列表.
     *
     * @param projectNo 场库编号
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    List<ProtocolVipCarVo> protocolVipCarList(String projectNo);
}

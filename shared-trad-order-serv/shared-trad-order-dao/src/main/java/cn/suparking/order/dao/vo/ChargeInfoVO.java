package cn.suparking.order.dao.vo;

import cn.suparking.order.dao.entity.ChargeDetailDO;
import cn.suparking.order.dao.entity.ChargeInfoDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeInfoVO extends ChargeInfoDO {

    private static final long serialVersionUID = -1834086592109022344L;

    private LinkedList<ChargeDetailDO> chargeDetailDOList;

}

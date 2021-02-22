package com.dkha.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.list.SetUniqueList;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 消费系统会员信息
 *
 * @author Mark
 * @since v1.0.0 2020-10-14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_consumptionsystem_vip")
public class ScConsumptionsystemVipEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     *
     */
	private Long id;
    /**
     * 会员姓名
     */
	private String name;
    /**
     * 编号（学号、职工编号）
     */
	private String number;
    /**
     * 身份证
     */
	private String idno;
    /**
     * 会员卡号
     */
	private String cardId;
    /**
     * 账户总余额
     */
	private BigDecimal balance;
    /**
     * 赠送余额
     */
	private BigDecimal giveBalance;
    /**
     * 本金余额
     */
	private BigDecimal rechargeBalance;
    /**
     * 手机号
     */
	private String phone;
    /**
     * 身份类别（学生、教职工、其他）
     */
	private String type;
    /**
     * 更新时间
     */
	private String updateDate;
    /**
     * 旧卡号
     */
	private String oldCardId;
    /**
     * 注册时间
     */
	private String registerDate;
    /**
     * 累计付款金额
     */
	private BigDecimal totalPayAmt;
    /**
     * 累计消费金额
     */
	private BigDecimal costTotal;
    /**
     * 累计消费次数
     */
	private String costTotalNum;
    /**
     * 1.正常 2.挂失 3. 注销
     */
	private Integer state;



	public static void main(String[] args) {
		List<ScConsumptionsystemVipEntity> list = new ArrayList<>();
		ScConsumptionsystemVipEntity scConsumptionsystemVipEntity = new ScConsumptionsystemVipEntity();
		scConsumptionsystemVipEntity.setId(1L);
		scConsumptionsystemVipEntity.setCardId("2");
		list.add(scConsumptionsystemVipEntity);
		ScConsumptionsystemVipEntity scConsumptionsystemVipEntity1 = new ScConsumptionsystemVipEntity();
		scConsumptionsystemVipEntity1.setId(1L);
		scConsumptionsystemVipEntity1.setCardId("3");
		list.add(scConsumptionsystemVipEntity1);
		ScConsumptionsystemVipEntity scConsumptionsystemVipEntity2 = new ScConsumptionsystemVipEntity();
		scConsumptionsystemVipEntity2.setId(0L);
		scConsumptionsystemVipEntity2.setCardId("2");
		list.add(scConsumptionsystemVipEntity2);
		ScConsumptionsystemVipEntity scConsumptionsystemVipEntity3 = new ScConsumptionsystemVipEntity();
		scConsumptionsystemVipEntity3.setId(5L);
		scConsumptionsystemVipEntity3.setCardId("6");
		list.add(scConsumptionsystemVipEntity3);

		Set<ScConsumptionsystemVipEntity> list1 = new LinkedHashSet<>();
		List<ScConsumptionsystemVipEntity> list2 = new ArrayList<>();


		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getId().equals(list.get(i).getId()) || list.get(j).getCardId().equals(list.get(i).getCardId())) {
					list1.add(list.get(j));//把相同元素加入list(找出相同的)
					list1.add(list.get(i));//把相同元素加入list(找出相同的)
					list.remove(j);//删除重复元素
				}else{
					list2.add(list.get(j));//把相同元素加入list(找出相同的)
				}
			}
		}
		for (ScConsumptionsystemVipEntity consumptionsystemVipEntity : list1) {
			System.out.println(JSONObject.toJSONString(consumptionsystemVipEntity));
		}
		for (ScConsumptionsystemVipEntity consumptionsystemVipEntity : list2) {
			System.out.println(JSONObject.toJSONString(consumptionsystemVipEntity));
		}
//		System.out.println(JSONObject.toJSONString(list1));
//		System.out.println(JSONObject.toJSONString(list2));

	}
}

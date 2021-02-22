package com.dkha.mq;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.tools.rabbitmq.MQExchangeNameConfig;
import com.dkha.commons.tools.rabbitmq.MQQueueNameConfig;
import com.dkha.dao.ScConsumptionsystemConsumeDao;
import com.dkha.dao.ScConsumptionsystemRechargeDao;
import com.dkha.dao.ScConsumptionsystemVipDao;
import com.dkha.dto.ConsumptionSystemDTO;
import com.dkha.entity.ScConsumptionsystemConsumeEntity;
import com.dkha.entity.ScConsumptionsystemRechargeEntity;
import com.dkha.entity.ScConsumptionsystemVipEntity;
import com.rabbitmq.client.Channel;
import org.bouncycastle.eac.EACIOException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConsumptionsystemMQ {

    @Autowired
    private ScConsumptionsystemVipDao scConsumptionsystemVipDao;

    @Autowired
    private ScConsumptionsystemConsumeDao scConsumptionsystemConsume;

    @Autowired
    private ScConsumptionsystemRechargeDao scConsumptionsystemRechargeDao;


    @RabbitListener(bindings = @QueueBinding(value = @Queue(MQQueueNameConfig.SCHOOL_CONSUMPTION_SYSTEM_QUEUE)
            , exchange = @Exchange(durable = "false",name = MQExchangeNameConfig.SCHOOL_CONSUMPTION_SYSTEM_EXCHANGE))
            ,ackMode = "MANUAL")
    public void mqReciver(String alarmmsg, Channel channel, Message message) throws IOException {
        try {
            ConsumptionSystemDTO consumptionSystemDTO = JSONObject.parseObject(alarmmsg, ConsumptionSystemDTO.class);
            String data = consumptionSystemDTO.getData();
            int type = consumptionSystemDTO.getType();
            //11.会员信息2. 充值信息3. 消费信息
            switch (type) {
                case 1:
                    ScConsumptionsystemVipEntity scConsumptionsystemVipEntity = JSONObject.parseObject(data, ScConsumptionsystemVipEntity.class);
                    ScConsumptionsystemVipEntity vipEntity = scConsumptionsystemVipDao.selectOne(new QueryWrapper<ScConsumptionsystemVipEntity>().eq("card_id", scConsumptionsystemVipEntity.getCardId()));
                    //当存在会员信息就修改会员信息
                    if (vipEntity != null) {
                        scConsumptionsystemVipEntity.setId(vipEntity.getId());
                        scConsumptionsystemVipDao.updateById(scConsumptionsystemVipEntity);
                    } else {
                        scConsumptionsystemVipDao.insert(scConsumptionsystemVipEntity);
                    }
                    break;
                case 2:
                    ScConsumptionsystemRechargeEntity scConsumptionsystemRechargeEntity = JSONObject.parseObject(data, ScConsumptionsystemRechargeEntity.class);
                    scConsumptionsystemRechargeDao.insert(scConsumptionsystemRechargeEntity);
                    break;
                case 3:
                    ScConsumptionsystemConsumeEntity scConsumptionsystemConsumeEntity = JSONObject.parseObject(data, ScConsumptionsystemConsumeEntity.class);
                    scConsumptionsystemConsume.insert(scConsumptionsystemConsumeEntity);
                    break;
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            e.printStackTrace();
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    public static void main(String[] args) {
        String xx = "{\"msgId\":\"C1A6D9D5-ADB0-41F4-809F-22C3B053D7C4\",\"type\":3,\"msg\":\"消费信息\",\"sendTime\":1602690628764,\"data\":{\"cardId\":\"1009\",\"costTotal\":\"-48.0000\",\"actualAmt\":\"168.0000\",\"discountAmt\":\"0.0000\",\"createDate\":\"2020-10-14 9:21:29\",\"business\":\"电子科大\",\"billId\":\"00012010140001\"}}";

        JSONObject jsonObject = JSONObject.parseObject(xx);
        ScConsumptionsystemConsumeEntity scConsumptionsystemConsumeEntity = JSONObject.parseObject(jsonObject.getString("data"), ScConsumptionsystemConsumeEntity.class);
        System.out.println();

    }

}

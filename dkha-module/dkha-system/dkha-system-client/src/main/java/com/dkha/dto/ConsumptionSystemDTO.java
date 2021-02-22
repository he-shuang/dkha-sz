package com.dkha.dto;


import lombok.Data;

@Data
public class ConsumptionSystemDTO {

    private String msgId;
    private int type;
    private String msg;
    private String sendTime;
    private String data;
}

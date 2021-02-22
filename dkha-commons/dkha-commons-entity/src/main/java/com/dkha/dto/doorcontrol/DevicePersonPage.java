package com.dkha.dto.doorcontrol;

import lombok.Data;

import java.util.List;

@Data
public class DevicePersonPage {
  private  int   pageCount	;// 总页数	Int
  private  int   pageIndex	;// 当前页数	Int
  private  int   pageSize	;// 每页数据数	Int
  private  int  totalCount	;// 总数据数量	Int
  private List<DeviceInnerPerson> dataInfo;//	人员列表子数据	List
}

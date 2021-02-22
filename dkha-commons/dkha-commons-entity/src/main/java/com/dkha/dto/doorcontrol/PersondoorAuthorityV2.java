package com.dkha.dto.doorcontrol;

import lombok.Data;

import java.util.List;

@Data
public class PersondoorAuthorityV2 {
    @Data
    public  class TimeRange {
        /**
         * 权限开始时间	String	N
         * 1、如“07:30:00”；
         * 2、值为null（空对象）或""（英文双引号）时，表示默认时间“00:00:00”
         */
        private String startTime;
        /**
         * 权限结束时间	String	N
         * 1、如“09:00:00”；
         * 2、值为null（空对象）或""（英文双引号）时，表示默认时间“23:59:59”；
         * 3、结束时间不能早于或等于开始时间
         */
        private String endTime;

        @Data
        public class AuthorityDetailsTime {
            /**
             * 权限开始日期	String	N
             * 1、如“2020-03-04”；
             * 2、值为null（空对象）或""（英文双引号）时，表示开始日期不限
             */
            private String startDate;
            /**
             * 权限结束日期	String	N
             * 1、如“2020-03-05”；
             * 2、值为null（空对象）或""（英文双引号）时，表示结束日期为不限
             */
            private String endDate;
            /**
             * 周一到周日生效范围	String	N
             * 1、用数字1-7表示周一到周日，并用英文逗号分隔；如“1,3,5,6”表示权限只在周一、周三、周五和周六生效，在周二、周四和周日时不生效；
             * 2、值为null（空对象）时，表示每天都生效；3、值为""（英文双引号）时， 表示权限禁止；4、值为“1,2,3,4,5”时，表示工作日
             */
            private String workingDays;
            /**
             * 权限生效时间集	List	Y	1、可以添加多个时间段；2、如果集合的size为0，按新增一条默认数据处理
             */
            private List<TimeRange> timeRangeList;
        }

        private String personSerial;            //	人员编号	String	Y
        private List<AuthorityDetailsTime> authorityDetails;      //	人员权限集	List	Y
    }
}

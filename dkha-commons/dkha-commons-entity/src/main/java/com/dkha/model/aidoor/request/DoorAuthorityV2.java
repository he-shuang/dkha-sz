package com.dkha.model.aidoor.request;

import java.io.Serializable;
import java.util.List;

/**
 * @desc 人员权限
 * @author
 * @date 2020/3/4 13:47
 */
public class DoorAuthorityV2 implements Serializable {

    /**
     * 人员编号
     */
    private String personSerial;

    /**
     * 权限详情集
     */
    private List<DoorAuthorityDetail> authorityDetails;

    public String getPersonSerial() {
        return personSerial;
    }

    public void setPersonSerial(String personSerial) {
        this.personSerial = personSerial;
    }

    public List<DoorAuthorityDetail> getAuthorityDetails() {
        return authorityDetails;
    }

    public void setAuthorityDetails(List<DoorAuthorityDetail> authorityDetails) {
        this.authorityDetails = authorityDetails;
    }

    /**
     * 权限详情
     */
    public static class DoorAuthorityDetail implements Serializable {

        /**
         * 权限开始日期，格式如“2020-03-04”，为空表示开始日期为当前日期；
         */
        private String startDate;

        /**
         * 权限结束日期，格式如“2021-03-04”，为空表示结束日期为未来无限长；
         */
        private String endDate;

        /**
         * 一周内生效范围：用数字“1...7”表示“周一...周日”，用逗号分割；如“1,3,6”表示周一、周三、周六生效，
         * 周二、周四、周五、周日不生效；为空表示周一到周日都不生效
         */
        private String workingDays;

        /**
         * 时分秒范围
         */
        private List<TimeAuthority> timeRangeList;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getWorkingDays() {
            return workingDays;
        }

        public void setWorkingDays(String workingDays) {
            this.workingDays = workingDays;
        }

        public List<TimeAuthority> getTimeRangeList() {
            return timeRangeList;
        }

        public void setTimeRangeList(List<TimeAuthority> timeRangeList) {
            this.timeRangeList = timeRangeList;
        }

        public static class TimeAuthority implements Serializable {

            /**
             * 开始时间，如“08:30:00”
             */
            private String startTime;

            /**
             * 结束时间，如“12:00:00”
             */
            private String endTime;

            /**
             * 时间段描述，预留字段
             */
            private String timeDesc;

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getTimeDesc() {
                return timeDesc;
            }

            public void setTimeDesc(String timeDesc) {
                this.timeDesc = timeDesc;
            }
        }
    }
}

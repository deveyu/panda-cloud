package com.ydc.basepack.vo;

import java.util.List;

/**
 * @author ydc
 * @description 接口响应结果
 */
public class ModifyResVO {
    private String status;
    private String message;
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String jobId;
        private String status;
        private String message;
        private List<ModifyVO> detail;

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<ModifyVO> getDetail() {
            return detail;
        }

        public void setDetail(List<ModifyVO> detail) {
            this.detail = detail;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "jobId='" + jobId + '\'' +
                    ", status='" + status + '\'' +
                    ", message='" + message + '\'' +
                    ", detail=" + detail +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ModifyResVO{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

package com.example.demo.infrastructure.exception;

import java.util.List;
public class ErrorResponse {
    private List<ErrorItem> errors;

    public ErrorResponse(List<ErrorItem> errors) { this.errors = errors; }

    public List<ErrorItem> getErrors() { return errors; }
    public void setErrors(List<ErrorItem> errors) { this.errors = errors; }

    public static class ErrorItem {
        private String status;
        private String title;
        private String detail;

        public ErrorItem(String status, String title, String detail) {
            this.status = status;
            this.title = title;
            this.detail = detail;
        }

        // getters y setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDetail() { return detail; }
        public void setDetail(String detail) { this.detail = detail; }
    }
}

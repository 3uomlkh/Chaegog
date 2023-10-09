package com.cchaegog.chaegog.Model;

import java.util.HashMap;
import java.util.Map;

public class ReportInfo {
    private String postId;
    private Map<String, Boolean> mailSend = new HashMap<>();

    public ReportInfo(String postId, Map<String, Boolean> mailSend) {
        this.postId = postId;
        this.mailSend = mailSend;
    }
    public ReportInfo() {

    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Map<String, Boolean> getMailSend() {
        return mailSend;
    }

    public void setMailSend(Map<String, Boolean> mailSend) {
        this.mailSend = mailSend;
    }
}

package com.peixinchen.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

public class Album {
    public Integer aid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer uid;
    public String name;
    public String cover;
    public Integer count;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String header;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String brief;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Story> storyList;
}

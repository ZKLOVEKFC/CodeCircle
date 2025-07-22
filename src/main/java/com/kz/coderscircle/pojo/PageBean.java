package com.kz.coderscircle.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用分页结果封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean<T> {

    //总条数
    private Long total;

    //当前页数据列表
    private List<T> items;
}
package com.example.graduationspringboot.vo.params;

import lombok.Data;

@Data
public class GetUserParam {

    private String input;

    private String userRole;

    private int page;

    private int size;
}

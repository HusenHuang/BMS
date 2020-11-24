package com.limaila.bms.authority.controller;

import com.limaila.bms.common.response.RestResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @PostMapping
    public RestResponse<?> index() {
        return RestResponse.success("POST");
    }
}

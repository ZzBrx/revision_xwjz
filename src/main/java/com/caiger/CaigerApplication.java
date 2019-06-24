package com.caiger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @className:  CaigerApplication   
 * @description: 启动程序
 * @author: 黄凯杰 
 * @date: 2018年12月26日 下午9:08:16
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@SpringBootApplication
@MapperScan("com.caiger.module.**.dao")
public class CaigerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaigerApplication.class, args);
	}
}

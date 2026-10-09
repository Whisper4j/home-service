package com.homeservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * MySQL启动校验配置类
 * 校验应用启动时的MySQL连接和账号表可用性
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MySqlStartupVerifier implements ApplicationRunner {

    private final DataSource dataSource;

    /**
     * 执行应用启动检查
     */
    public void run(ApplicationArguments args) throws Exception {
        try (var connection = dataSource.getConnection()) {
            var metadata = connection.getMetaData();
            if (!"MySQL".equals(metadata.getDatabaseProductName()))
                throw new IllegalStateException("需要 MySQL 8.0.16+");
            String[] version = metadata.getDatabaseProductVersion().split("[.-]");
            int major = Integer.parseInt(version[0]),
                    minor = Integer.parseInt(version[1]),
                    patch = Integer.parseInt(version[2]);
            if (major < 8 || major == 8 && minor == 0 && patch < 16)
                throw new IllegalStateException("需要 MySQL 8.0.16+");
            try (var statement = connection.createStatement();
                    var result =
                            statement.executeQuery(
                                    "SELECT id, role, status FROM auth_account WHERE 1=0")) {
                log.info("MySQL {} 已连接，账号表只读探测成功；无自动初始化", metadata.getDatabaseProductVersion());
            }
        }
    }
}

package com.besscroft.diyfile.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.besscroft.diyfile.common.exception.DiyFileException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.internal.database.DatabaseType;
import org.flywaydb.core.internal.exception.FlywaySqlException;
import org.flywaydb.core.internal.plugin.PluginRegister;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * API 文档：https://documentation.red-gate.com/fd/api-java-184127629.html
 * 兼容性文档：https://flywaydb.org/documentation/database/mysql
 * @Description flyway 初始化配置
 * @Author Bess Croft
 * @Date 2023/3/3 16:14
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class FlywayConfigure {

    private final DataSource dataSource;
    private static final List<DatabaseType> SORTED_DATABASE_TYPES = new PluginRegister().getPlugins(DatabaseType.class).stream().sorted().collect(Collectors.toList());
    private final ApplicationContext applicationContext;

    @Value("${spring.flyway.enabled}")
    private Boolean enabled;

    @PostConstruct
    public void flywayMigrate() {
        String datasourceDriveClassName = applicationContext.getEnvironment().getProperty("spring.datasource.driver-class-name");
        String datasourceUrl = applicationContext.getEnvironment().getProperty("spring.datasource.url");
        // sqlite 数据库文件创建
        log.info("加载到驱动类：{}", datasourceDriveClassName);
        if (StrUtil.equals(datasourceDriveClassName, "org.sqlite.JDBC")) {
            String path = datasourceUrl.replace("jdbc:sqlite:", "");
            String folderPath = FileUtil.getParent(path, 1);
            if (!FileUtil.exist(folderPath)) {
                log.info("sqlite 数据库文件不存在，即将创建！}");
                FileUtil.touch(folderPath);
                log.info("sqlite 数据库文件创建成功，路径：{}", folderPath);
            } else {
                log.info("sqlite 数据库文件已存在，路径：{}", folderPath);
            }
        }
        if (!enabled) {
            log.info("flyway 已关闭，跳过初始化...");
            return;
        }
        try {
            String databaseProductName = dataSource.getConnection().getMetaData().getDatabaseProductName();
            String dbType = databaseProductName.toLowerCase(Locale.ROOT);
            log.info("当前数据源为：{}，当前数据库类型为：{}", databaseProductName, dbType);
            DatabaseMetaData databaseMetaData;
            try {
                databaseMetaData = dataSource.getConnection().getMetaData();
            } catch (SQLException e) {
                throw new FlywaySqlException("Unable to read database connection metadata: " + e.getMessage(), e);
            }
            if (databaseMetaData == null) {
                throw new FlywayException("Unable to read database connection metadata while it is null!");
            }
            String databaseProductVersion = databaseMetaData.getDatabaseProductVersion();
            log.info("当前数据库版本为：{}", databaseProductVersion);
            // 设置数据库是否初始化标志
            boolean isInit = false;
            if ("5".equals(StrUtil.sub(databaseProductVersion, 0, 1))) {
                log.warn("数据库版本不支持自动初始化，跳过初始化...");
                return;
            }
            for (DatabaseType type : SORTED_DATABASE_TYPES) {
                if (type.handlesDatabaseProductNameAndVersion(databaseProductName, databaseProductVersion, dataSource.getConnection())) {
                    log.info("数据库版本支持自动初始化，即将开始初始化...");
                    Flyway flyway = Flyway.configure().dataSource(dataSource).outOfOrder(true).locations("db/" + dbType + "/migration").load();
                    flyway.migrate();
                    isInit = true;
                }
            }
            if (!isInit) {
                log.warn("Unable to find a Flyway plugin for database: {}, with version: {}, and type: {}.", databaseProductName, databaseProductVersion, dbType);
                return;
            }
            log.info("数据库初始化完毕！");
        } catch (SQLException e) {
            throw new DiyFileException(e.getMessage());
        }
    }

}

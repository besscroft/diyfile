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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

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

    @Value("${spring.datasource.driver-class-name}")
    private String datasourceDriveClassName;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    /**
     * sqlite 数据库文件创建
     */
    @PostConstruct
    public void sqliteInit() {
        if (StrUtil.equals(datasourceDriveClassName, "org.sqlite.JDBC")) {
            String path = datasourceUrl.replace("jdbc:sqlite:", "");
            String folderPath = FileUtil.getParent(path, 1);
            log.info("sqlite 数据库文件路径：{}", folderPath);
            if (!FileUtil.exist(folderPath)) {
                FileUtil.touch(folderPath);
            }
        }
    }

    @PostConstruct
    @DependsOn("sqliteInit")
    public void flywayMigrate() {
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
            if (StrUtil.sub(databaseProductVersion, 0, 1).equals("5")) {
                log.info("数据库版本不支持自动初始化，跳过初始化...");
                return;
            }
            for (DatabaseType type : SORTED_DATABASE_TYPES) {
                if (type.handlesDatabaseProductNameAndVersion(databaseProductName, databaseProductVersion, dataSource.getConnection())) {
                    log.info("数据库版本支持自动初始化，即将开始初始化...");
                    Flyway flyway = Flyway.configure().dataSource(dataSource).outOfOrder(true).locations("db/" + dbType + "/migration").load();
                    flyway.migrate();
                }
            }
            log.info("数据库初始化完毕！");
        } catch (SQLException e) {
            throw new DiyFileException(e.getMessage());
        }
    }

}

package com.besscroft.xanadu.common.dto;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.NumberUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Description 系统信息
 * @Author Bess Croft
 * @Date 2022/12/19 14:02
 */
@Data
@Schema(title = "系统信息")
public class ServerInfo {

    private static final int OSHI_WAIT_SECOND = 1000;
    public static final int KB = 1024;
    public static final int MB = KB * 1024;
    public static final int GB = MB * 1024;

    /** CPU 信息 */
    @Schema(title = "CPU 信息", type = "String")
    private CpuInfo cpuInfo;

    /** 内存信息 */
    @Schema(title = "内存信息", type = "String")
    private MemoryInfo memoryInfo;

    /** 服务器信息 */
    @Schema(title = "服务器信息", type = "String")
    private SystemInfo systemInfo;

    /** 文件系统信息 */
    @Schema(title = "文件系统信息", type = "String")
    private List<DiskInfo> diskInfos;

    public static ServerInfo serverInfo() {
        ServerInfo serverInfo = new ServerInfo();
        oshi.SystemInfo si = new oshi.SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        serverInfo.cpuInfo(hal.getProcessor());
        serverInfo.memoryInfo(hal.getMemory());
        serverInfo.systemInfo();
        serverInfo.diskInfos(si.getOperatingSystem());
        return serverInfo;
    }

    private void cpuInfo(CentralProcessor processor) {
        cpuInfo = new CpuInfo();
        cpuInfo.setCpuNum(processor.getLogicalProcessorCount());
    }

    private void memoryInfo(GlobalMemory memory) {
        memoryInfo = new MemoryInfo();
        memoryInfo.setTotal(memory.getTotal());
        memoryInfo.setUsed(memory.getTotal() - memory.getAvailable());
        memoryInfo.setFree(memory.getAvailable());
    }

    private void systemInfo() {
        systemInfo = new SystemInfo();
        Properties props = System.getProperties();
        systemInfo.setComputerName(NetUtil.getLocalHostName());
        systemInfo.setComputerIp(NetUtil.getLocalhost().getHostAddress());
        systemInfo.setOsName(props.getProperty("os.name"));
        systemInfo.setOsArch(props.getProperty("os.arch"));
        systemInfo.setUserDir(props.getProperty("user.dir"));
    }

    private void diskInfos(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        diskInfos = new ArrayList<>();
        fsArray.forEach(fs -> {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            DiskInfo diskInfo = new DiskInfo();
            diskInfo.setDirName(fs.getMount());
            diskInfo.setSysTypeName(fs.getType());
            diskInfo.setTypeName(fs.getName());
            diskInfo.setTotal(convertFileSize(total));
            diskInfo.setFree(convertFileSize(free));
            diskInfo.setUsed(convertFileSize(used));
            diskInfo.setUsage(NumberUtil.div(used * 100, total, 4));
            diskInfos.add(diskInfo);
        });
    }

    /**
     * 字节转换
     * @param size 转换前的值
     * @return 转换后的值
     */
    public String convertFileSize(long size) {
        float castedSize = (float) size;
        if (size >= GB) {
            return String.format("%.1f GB", castedSize / GB);
        }
        if (size >= MB) {
            return String.format("%.1f MB", castedSize / MB);
        }
        return String.format("%.1f KB", castedSize / KB);
    }

}

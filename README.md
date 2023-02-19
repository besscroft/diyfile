# DiyFile

<p align="center">

![DiyFile](https://besscroft.com/uploads/diyfile.png)

</p>

一款好看的在线文件列表程序，由 Spring Boot 3 和 Vue 驱动。

[![](https://img.shields.io/badge/%E5%BC%80%E5%8F%91%E8%BF%9B%E5%BA%A6-%E5%BC%80%E5%8F%91%E4%B8%AD-brightgreen?style=flat-square)]() [![](https://img.shields.io/github/license/besscroft/diyfile?style=flat-square)](https://github.com/besscroft/diyfile/blob/master/LICENSE) ![GitHub repo size](https://img.shields.io/github/repo-size/besscroft/diyfile?style=flat-square&color=328657)

### 预览

[Demo](https://demo.besscroft.com/)

### 文档

[DiyFile 的文档](https://doc.diyfile.besscroft.com/) ，在这里你可以找到大部分问题的解答。

## 环境搭建

### 开发环境

* 示例命令：

```shell
docker run -d --name diyfile \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xms512m -Xmx512m -Duser.timezone=GMT+08 -Dfile.encoding=UTF8" \
  -e DB_URL="localhost:3306" \
  -e DB_NAME="diyfile" \
  -e DB_USERNAME="root" \
  -e DB_PASSWORD="666666" \
  besscroft/diyfile:latest
```

> 端口可以自定义，docker 容器内的程序端口为 8080，你可以自定义对应的宿主机的端口，以及网络类型。请注意，容器内连接主机端口，可以使用 ip 172.17.0.1。

### 代码贡献

[提出新想法 & 提交 Bug](https://github.com/besscroft/diyfile/issues/new) | [Fork & Pull Request](https://github.com/besscroft/diyfile/fork)

DiyFile 欢迎各种贡献，包括但不限于改进，新功能，文档和代码改进，问题和错误报告。

> 请注意，在 V1 版本发布前，可能会有较大的改动。

### 在线开发

你可以使用 Gitpod 进行在线开发：

<p><a href="https://gitpod.io/#https://github.com/besscroft/diyfile" rel="nofollow"><img src="https://camo.githubusercontent.com/1eb1ddfea6092593649f0117f7262ffa8fbd3017/68747470733a2f2f676974706f642e696f2f627574746f6e2f6f70656e2d696e2d676974706f642e737667" alt="Open in Gitpod" data-canonical-src="https://gitpod.io/button/open-in-gitpod.svg" style="max-width:100%;"></a></p>

或者克隆到本地开发:

```shell
git clone https://github.com/besscroft/diyfile.git
```
### 前端项目

[diyfile-web](https://github.com/besscroft/diyfile-web)

如果您有任何建议，欢迎反馈！

您可以将服务部署在 `DigitalOcean` ，如果你愿意走我的邀请链接注册，可以获得100美元的信用额度。

<a href="https://www.digitalocean.com/?refcode=6841be7284cc&utm_campaign=Referral_Invite&utm_medium=Referral_Program&utm_source=badge"><img src="https://web-platforms.sfo2.cdn.digitaloceanspaces.com/WWW/Badge%201.svg" alt="DigitalOcean Referral Badge" /></a>

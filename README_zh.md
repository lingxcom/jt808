## 🚀 领新北斗-车辆动态监控系统（TracSeek）

> 一个支持 大规模设备接入的开源车联网系统，内置模拟终端 + 可视化平台，5分钟快速体验完整链路。

<p>
    <img src="https://img.shields.io/badge/License-Apache 2.0-green.svg"/>
    <img src="https://img.shields.io/badge/platform-linux%20|%20macos%20|%20windows-blue.svg" />
    <img src="https://img.shields.io/badge/QQ-283853318-blue"/>
</p>
基于部标JT808标准实现的开箱即用的北斗定位监控平台，负责实现核心信令与设备管理后台部分，支持海康、大华、锐明、通立、博实结等品牌的终端设备接入。

## ✨ 项目亮点
* 🚗 完整支持 JT808 协议（车载终端接入标准）
* ⚡ 高性能架构设计（可扩展至大规模设备接入）
* 🛰 实时定位 / 轨迹回放 / 电子围栏
* 🧪 内置模拟终端（无需真实设备即可测试）
* 🌐 可视化监控平台
* 🔧 适用于物流 / 车队管理 / IoT 场景

## 🎯 解决什么问题？

如果你正在做：
* 车联网系统
* GPS / 北斗定位平台
* 物流调度系统
* IoT 设备接入平台

你通常会遇到：

* ❌ JT808 协议复杂难实现
* ❌ 没有完整的服务端 + 终端闭环
* ❌ 测试必须依赖真实设备
* ❌ 系统难以扩展

> 👉 TracSeek 提供一整套解决方案（服务端 + 模拟终端 + 可视化）

## ⚡ 5分钟快速体验（强烈建议先跑这个）
### 方式一：Docker（推荐）


```bash
git clone https://gitee.com/lingxcom/tracseek.git
cd tracseek
git clone https://gitee.com/lingxcom/tracseek-web.git
docker compose up -d
```
启动后访问：

```bash
http://localhost:8800
账号：admin
密码：a123456
```

---
### 方式二：下载一键启动
* Gitee ：[https://gitee.com/lingxcom/tracseek/releases/download/1.1/tracseek.zip](https://gitee.com/lingxcom/tracseek/releases/download/1.1/tracseek.zip)
* Github ：[https://github.com/lingxcom/tracseek/releases/download/1.1/tracseek.zip](https://github.com/lingxcom/tracseek/releases/download/1.1/tracseek.zip)


## 🖥 在线 Demo
http://gps.lingx.com/

账号:admin

密码:123456

终端设备接入

IP：47.100.112.218

端口：8808

压测记录：https://blog.csdn.net/lingx_gps/article/details/136833506

## 协议支持
| 协议名称                | 版本   | 开源版 |商业版 | 备注           |
|---------------------|------|-----|--------------|--------------|
| JT/T 808            | 2011 | 支持  | 支持   |
| JT/T 808            | 2013 | 支持  | 支持   |
| JT/T 808            | 2019 | 支持  | 支持   |
| JT/T 809            | 2011 | 未支持 | 支持   |
| JT/T 1078           | 2016 | 未支持 |  支持   |
| T/JSATL 12(主动安全-苏标) | 2017 | 未支持 | 支持   |
| 非标协议扩展              | -    | 未支持 | 支持   |

非标协议扩展（包括不限于）：油量、电量、温度、湿度、高精度定位、蓝牙信标、语音留言。

## 数据库MySQL8.0
数据库是采用MySQL8.0，下载地址：https://www.mysql.com/downloads/

## 后端代码仓库
* Gitee ：[https://gitee.com/lingxcom/tracseek](https://gitee.com/lingxcom/tracseek)
* Github ：[https://github.com/lingxcom/tracseek](https://github.com/lingxcom/tracseek)

## 前端代码仓库

* Gitee仓库地址：[https://gitee.com/lingxcom/tracseek-web](https://gitee.com/lingxcom/tracseek-web)
* Github仓库地址：[https://github.com/lingxcom/tracseek-web](https://github.com/lingxcom/tracseek-web)

## 软件下载
* Gitee ：[https://gitee.com/lingxcom/tracseek/releases/download/1.1/tracseek.zip](https://gitee.com/lingxcom/tracseek/releases/download/1.1/tracseek.zip)
* Github ：[https://github.com/lingxcom/tracseek/releases/download/1.1/tracseek.zip](https://github.com/lingxcom/tracseek/releases/download/1.1/tracseek.zip)

## 终端设备模拟工具
* Gitee ：[https://gitee.com/lingxcom/jt808-client/releases/download/1.1/jt808tools-exe.zip](https://gitee.com/lingxcom/jt808-client/releases/download/1.1/jt808tools-exe.zip)
* Github ：[https://github.com/lingxcom/jt808-client/releases/download/1.1/JT808.zip](https://github.com/lingxcom/jt808-client/releases/download/1.1/JT808.zip)


##⭐ 如果这个项目对你有帮助

请给一个 Star ⭐

这是对开源作者最大的支持 🙌


## 功能展示
- Software Startup Tool

![index](readme/20250516164941.png "index.png")

- Database Configuration Tool

![index](readme/20250516165024.png "index.png")

- Real-time GPS tracking

![index](readme/20250516165146.png "index.png")

- Real-time GPS tracking Group

  ![index](readme/20250516165319.png "index.png")

- History

  ![index](readme/20250516165526.png "index.png")

- History(dilution)

  ![index](readme/20250516165713.png "index.png")

- History(multiple)

  ![index](readme/20250516165854.png "index.png")

## License
```
Apache License, Version 2.0

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
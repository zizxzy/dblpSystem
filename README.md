# dblpSystem
dblp系统

## 快速开始
### 修改配置文件 src\main\resources\config\global.properties（不支持中文路径）
+ xml_file_location：dblp.xml路径
+ index_file_root：index索引文件和rank排序文件根目录（现版本需要手动创建根目录下的三个子文件夹：author, title, rank）
+ batch：xmlParseRunnable追加一次索引文件的阈值（默认1000000）

### 创建索引文件
+ 启动XmlParseTest类的main方法
+ 启动Combination类的test方法
+ 完成索引文件的创建

### 创建rank文件
+ 创建文章数作者排行文件：启动AuthorRankingListGenerator类的test方法
+ ...

### 配置服务器并部署
+ 自己配

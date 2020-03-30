# dblpSystem

## 特别注意事项
- 项目根目录/src/main/java/com/scut/comsubgraph/ComSubGraphGetter.java下的第21行情修改为自己项目中的global.properties的绝对路径，不然会报错无法运行
- 项目运行需要的所有索引文件和在项目根目录/public/dblp_index.zip中，请自行解压
## 快速开始
<<<<<<< Updated upstream
### 修改配置文件 src\main\resources\config\global.properties（不支持中文路径）
+ xml_file_location：dblp.xml路径
+ index_file_root：index索引文件和rank排序文件根目录（现版本需要手动创建根目录下的四个子文件夹：author, title, rank, comsubgraph）
=======
### 修改配置文件
+ 注意：如果已经解压了dblp_index压缩包，则创建索引文件和创建rank文件夹中的文件过程均可以不做，只需修改global.properties中的xml_file_location、index_file_root和title_file_root的值
+ 进入'项目根目录/src/main/resources/config/global.properties'
+ 修改xml_file_location的值为自己电脑中dblp.xml文件所在的位置
+ index_file_root：设置index_file_root的值为自己电脑中要存放的索引的根目录，同时在该目录下建立三个文件夹（author,rank,title)
+ title_file_root：设置title_file_root为自己电脑中存放title索引的目录
>>>>>>> Stashed changes
+ batch：xmlParseRunnable追加一次索引文件的阈值（默认1000000）
+ 然后设置index_file_root的值为自己电脑中要存放的索引的根目录，同时在该目录下建立三个文件夹（author,rank,title)
+ 同时该目录下有一个comsubgraph文件夹-存放与完全子图相关的信息，不需自己生成

### 创建索引文件
<<<<<<< Updated upstream
+ 启动XmlParseTest类的main方法
+ 启动Combination类的test方法
+ 启动SubGraphInit类的main方法
+ 完成索引文件的创建
=======

- 运行'dblpSystem\src\main\java\com\yejh\indexinit\IndexInitializer.java'类
- 运行'dblpSystem\src\main\java\com\yejh\xmlparse\XmlParseTest.java'的main函数
- 运行'dblpSystem\src\main\java\com\yejh\combination\Combination.java'中的tset测试方法进行压缩
>>>>>>> Stashed changes

### 创建rank文件
- 运行'dblpSystem\src\main\java\com\yejh\rank\AuthorRankingListGenerator.java'中的两个测试方法生成rank文件夹中的txt文件
- 同时rank目录下还有一个annual_hot_word_top10.txt文件-存放每年热词的文件

### 访问主页
- 启动tomcat服务器，访问'localhost:8080/dblpSystem/'访问主页
- 端口在不同pc上可能会不同，请根据自己的配置访问主页




# vod
视频点播系统实现视频的下载业务

部署环境：已正常使用weblib系统（包括jdk，MySQL等基本环境）

1.建立数据库vod。设置连接数据库的用户名和密码（与/home/vod/vod/tomcat/webapps/vod/WEB-INF/classes/jdbc.propertity中一致即可），以用户名和密码分别为vod，vod2017为例
创建数据库
	create database vod default charset utf8;
创建用户
	create user 'vod'@'localhost' identified by 'vod2017';
用户授权
	grant all privileges on vod.* to 'vod'@'%';
	grant all privileges on vod.* to 'vod'@'localhost';
 	flush privileges;
	
	
2.Linux创建用户vod，把安装包复制到/home/vod下面解压安装包，解压安装安装包
  cd /home/vod
  tar -xvf vod_setup.tar

解压后应该有
 vod/
 voddata/
 init_vod_resource.sql






3.初始化vod数据库，执行以下命令
  mysql -uvod -pvod2017
  use vod
  source /home/vod/init_vod_resource.sql


4.修改Global.propertities(/home/vod/vod/tomcat/webapps/vod/WEB-INF/classes/Global.propertities)中的webliburl 和weblib的用户名和密码,vod文件存放地址，用户名和密码对应的weblib用户应该对被请求的资源有访问权限

weblibUsername = guochao
weblibPasswd = guochao
webliburl = http://202.38.254.208:9090
FileRootPath = /home/vod/voddata/resource/208/




5.启动应用
进入/home/vod/vod/tomcat/bin目录下
执行startup.sh命令启动应用

http://202.38.254.208:12090/vod/resource/download?id=323277&isInline=1为访问接口，其中id为所访问资源在weblib中的id，isInline表示返回格式是否内嵌还是下载。


******注意********
换服务器的时候要改配置文件Global.propertities的请求服务器地址，资源存放地址和数据库连接地址

初始化新建文件夹的时候要新建到最底层，只能创建文件，不能创建目录

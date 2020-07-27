#!/usr/bin/env bash
#ps -ef查询这个系统中所有的进程， grep查询的是架包的名称，grep -v grep查询的对应的启动进程，awk获取进程的id
pid=`ps -ef | grep boot-lianxi.jar | grep -v grep | awk '{print $2}'`
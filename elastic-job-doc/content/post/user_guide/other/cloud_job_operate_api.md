+++
date = "2016-01-27T16:14:21+08:00"
title = "Elastic-Job-Cloud作业操作API"
weight=21
+++

# Elastic-Job-Cloud作业操作API

`Elastic-Job-Cloud`提供了作业操作的`Restful API`。可通过`curl`进行操作。

## 注册作业

url：`job/register`

方法：`POST`

参数类型：`application/json`

参数列表：

| 属性名                              | 类型  |是否必填 | 缺省值 | 描述                                                                        |
| -----------------------------------|:------|:------|:-------|:---------------------------------------------------------------------------|
|jobName                             |String |`是`    |       | 作业名称。为`Elastic-Job-Cloud`的作业唯一标识                                  |
|cron                                |String |`是`    |       | `cron`表达式，用于配置作业触发时间                                             |
|shardingTotalCount                  |int    |`是`    |       | 作业分片总数                                                                 |
|cpuCount                            |double |`是`    |       | 单片作业所需要的CPU数量                                                       |
|memoryMB                            |double |`是`    |       | 单片作业所需要的内存MB                                                        |
|dockerImageName                     |String |否      |       | `Docker`镜像名称。目前为预留值                                                |
|appURL                              |String |`是`    |       | 应用所在路径。必须是可以通过网络访问到的路径                                     |
|failover                            |boolean|否      |`false`| 是否开启失效转移                                                             |
|misfire                             |boolean|否      |`false`| 是否开启错过任务重新执行                                                      |

例：
`curl -l -H "Content-type: application/json" -X POST -d '{"jobName":"foo_job","cron":"0/5 * * * * ?","shardingTotalCount":5,"cpuCount":0.1,"memoryMB":64.0,"dockerImageName":"","appURL":"http://app_host:8080/foo-job.tar.gz","failover":true,"misfire":true}' http://elastic_job_cloud_host:8899/job/register`

## 删除作业

url：`job/unregister`

方法：`DELETE`

参数类型：`application/text`

参数：作业名称

`curl -l -H "Content-type: application/text" -X DELETE -d 'foo_job' http://elastic_job_cloud_host:8899/job/unregister`

在阿里工作的时候是使用Blink进行流数据处理和计算，通过编写sql实现Blink的计算job，开发简单高效，产品易用。目前想尝试实现Flink产品化，类似Blink这种产品。
   1.使用SQL为统一开发规范，SQL语言的好处是：声明式，易理解，稳定可靠，自动优化。如果采用API开发的话，最大的问题是对于job调优依赖程序员经验，比较困难，同时API开发方式侵入性太强(数据安全，集群安装等)。而sql可以自动调优，避免这两个问题的产生。
   2.目前实现思路：
用户输入sql（ddl,dml,query） -> ddl对应为Flink的source和source
                           -> dml的insert into对应将对应数据加载到 sink 
                           -> query数据处理和计算
    --> 封装为api对应Flink的Job:env.sqlQuery/env.sqlUpdate;table.writeToSink;
    --> Job提交执行 streamGraph.getJobGraph;   ClusterClient<ApplicationId> client = descriptor.deploy();client.runDetached(job, null);
    
二.开发过程
   1.SqlConvert是将用户sql语句解析为不同类型，安装ddl,source和sink等返回；
   2.ddl类型的sql语句，实现Flink的Source和Sink；

三.代码关注
apache calcite 的sql引擎源码解读
apache Flink sql实现
apache hive
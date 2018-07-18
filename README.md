在阿里使用Blink，即Apache Flink在阿里的产品化。使用Blink做流处理和计算，产品的易用性.目前自己想尝试或者挑战Flink产品化。
1.使用SQL作为统一开放规范，SQL开发语言的好处：声明式，易理解，稳定可靠，自动优化。
    如果采用API开发的话，对于job调优困难，同时API开发方式侵入性太强(数据安全，集群安装等)。同时sql可以自动调优，避免api调优依赖开发人员经验。  
2.实现思路：
    用户输入sql（ddl,dml,query） -> ddl对应为Flink的source和source
                               -> dml的insert into对应将对应数据加载到 sink
                               -> query数据处理和计算
    --> 封装为api对应Flink的Job:env.sqlQuery/env.sqlUpdate;table.writeToSink;
    --> Job提交执行 streamGraph.getJobGraph;                          
3.核心代码关注：
apache calcite 的sql引擎源码解读
apache Flink sql实现
apache hive
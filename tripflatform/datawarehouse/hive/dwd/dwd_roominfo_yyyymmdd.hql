drop table if exists dwd_roominfo;
create table if not exists dwd_roominfo as(

//6张表关联,形成一张维表
)

--日增数据
--不能drop整张表

1.分区表
alter table part_table drop partition(pc='20180418')

--外表分区表
alter table part_table add partition(pc='20180418')
location('/path/to/new/data/${yyyymmdd}')

--内表分区表
insert into part_table partition(pc='20180418')
select 数据放入新建的分区

2.不是分区表而是使用分表模式
drop table day_inc_table_${yyyymmdd};
create table if not exists day_inc_table_${yyyymmdd} as(

//select 数据
)
-----------------上面两种pds,dwd或者数量量很大的dw层----------------------
------------st表,数据展示,数据量少--------------
3.既不是分区表也不是分表的模式
-- 该表在hive中必须是支持事务的表
该表一定会有字段:date_day
delete from day_inc_table where date_day='${yyyymmdd}'


sqoop增量导





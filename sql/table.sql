CREATE database IF not exists GUMI;
create table  IF NOT EXISTS user
(
    id          bigint auto_increment comment 'id'
    primary key,
    user_name   varchar(256)                       null comment '用户昵称',
    phone       varchar(14)                        null comment ' 电话',
    address     varchar(100)                       null comment '地址',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除',
    user_role   int      default 0                 not null comment '用户角色 0 - 普通用户 1 - 管理员',
    tags        varchar(1024)                      null comment '标签 json 列表'
    );


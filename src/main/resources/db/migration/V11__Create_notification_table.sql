create table notification
(
    id BIGINT auto_increment,
    notifier BIGINT not null,
    receiver BIGINT not null,
    outer_id BIGINT not null,
    type int not null,
    gmt_create BIGINT not null,
    status int default 0 not null,
    constraint NOTIFICATION_PK
        primary key (id)
)
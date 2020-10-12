create table blog (
id bigint auto_increment primary key,
userId bigint,
title varchar(100),
description varchar(100),
content TEXT,
created_at datetime,
updated_at datetime
)
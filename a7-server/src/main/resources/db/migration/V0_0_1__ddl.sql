create table access_history
(
  id bigint auto_increment
    primary key,
  created_at timestamp default CURRENT_TIMESTAMP not null,
  updated_at timestamp default CURRENT_TIMESTAMP not null,
  browser_name varchar(255) not null,
  browser_version varchar(255) not null,
  is_mobile varchar(255) not null,
  language varchar(255) not null,
  os_name varchar(255) not null,
  os_version varchar(255) not null,
  timestamp bigint not null,
  timezone varchar(255) not null,
  user_agent text not null
)
;

create table session_history
(
  id bigint auto_increment
    primary key,
  created_at timestamp default CURRENT_TIMESTAMP not null,
  updated_at timestamp default CURRENT_TIMESTAMP not null,
  session_creation_time bigint not null,
  session_id varchar(255) not null
)
;

create table stomp_history
(
  id bigint auto_increment
    primary key,
  created_at timestamp default CURRENT_TIMESTAMP not null,
  updated_at timestamp default CURRENT_TIMESTAMP not null,
  native_session_id varchar(255) not null,
  simp_session_id varchar(255) not null
)
;


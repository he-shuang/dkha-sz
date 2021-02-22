create table sys_user
(
  id                   bigint not null comment 'id',
  username             varchar(50) comment '用户名',
  password             varchar(100) comment '密码',
  real_name            varchar(50) comment '姓名',
  head_url             varchar(200) comment '头像',
  gender               tinyint(4) unsigned comment '性别   0：男   1：女    2：保密',
  email                varchar(100) comment '邮箱',
  mobile               varchar(20) comment '手机号',
  dept_id              bigint comment '部门ID',
  super_admin          tinyint unsigned comment '超级管理员   0：否   1：是',
  status               tinyint(4) unsigned comment '状态  0：停用    1：正常',
  remark               varchar(200) comment '备注',
  del_flag             tinyint(4) unsigned comment '删除标识  0：未删除    1：删除',
  creator              bigint comment '创建者',
  create_date          datetime comment '创建时间',
  updater              bigint comment '更新者',
  update_date          datetime comment '更新时间',
  primary key (id),
  unique key uk_username (username),
  key idx_del_flag (del_flag),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='用户管理';


create table sys_dept
(
  id                   bigint not null comment 'id',
  pid                  bigint comment '上级ID',
  pids                 varchar(500) comment '所有上级ID，用逗号分开',
  name                 varchar(50) comment '部门名称',
  sort                 int unsigned comment '排序',
  del_flag             tinyint(4) unsigned comment '删除标识  0：未删除    1：删除',
  creator              bigint comment '创建者',
  create_date          datetime comment '创建时间',
  updater              bigint comment '更新者',
  update_date          datetime comment '更新时间',
  primary key (id),
  key idx_pid (pid),
  key idx_del_flag (del_flag),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='部门管理';


create table sys_menu
(
  id                   bigint not null comment 'id',
  pid                  bigint comment '上级ID，一级菜单为0',
  url                  varchar(200) comment '菜单URL',
  type                 tinyint unsigned comment '类型   0：菜单   1：按钮',
  icon                 varchar(50) comment '菜单图标',
  permissions          varchar(200) comment '权限标识，如：sys:menu:save',
  sort                 int(11) comment '排序',
  del_flag             tinyint(4) unsigned comment '删除标识  0：未删除    1：删除',
  creator              bigint comment '创建者',
  create_date          datetime comment '创建时间',
  updater              bigint comment '更新者',
  update_date          datetime comment '更新时间',
  primary key (id),
  key idx_pid (pid),
  key idx_del_flag (del_flag),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='菜单管理';


create table sys_role
(
  id                   bigint not null comment 'id',
  name                 varchar(32) comment '角色名称',
  remark               varchar(100) comment '备注',
  del_flag             tinyint(4) unsigned comment '删除标识  0：未删除    1：删除',
  dept_id              bigint comment '部门ID',
  creator              bigint comment '创建者',
  create_date          datetime comment '创建时间',
  updater              bigint comment '更新者',
  update_date          datetime comment '更新时间',
  primary key (id),
  key idx_dept_id (dept_id),
  key idx_del_flag (del_flag),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='角色管理';


create table sys_role_user
(
  id                   bigint not null comment 'id',
  role_id              bigint comment '角色ID',
  user_id              bigint comment '用户ID',
  creator              bigint comment '创建者',
  create_date          datetime comment '创建时间',
  primary key (id),
  key idx_role_id (role_id),
  key idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='角色用户关系';


create table sys_role_menu
(
  id                   bigint not null comment 'id',
  role_id              bigint comment '角色ID',
  menu_id              bigint comment '菜单ID',
  creator              bigint comment '创建者',
  create_date          datetime comment '创建时间',
  primary key (id),
  key idx_role_id (role_id),
  key idx_menu_id (menu_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='角色菜单关系';


create table sys_role_data_scope
(
  id                   bigint not null comment 'id',
  role_id              bigint comment '角色ID',
  dept_id              bigint comment '部门ID',
  creator              bigint comment '创建者',
  create_date          datetime comment '创建时间',
  primary key (id),
  key idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='角色数据权限';


-- 字典类型
create table sys_dict_type
(
    id                   bigint NOT NULL COMMENT 'id',
    dict_type            varchar(100) NOT NULL COMMENT '字典类型',
    dict_name            varchar(255) NOT NULL COMMENT '字典名称',
    remark               varchar(255) COMMENT '备注',
    sort                 int unsigned COMMENT '排序',
    creator              bigint COMMENT '创建者',
    create_date          datetime COMMENT '创建时间',
    updater              bigint COMMENT '更新者',
    update_date          datetime COMMENT '更新时间',
    primary key (id),
    UNIQUE KEY(dict_type)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='字典类型';

-- 字典数据
create table sys_dict_data
(
    id                   bigint NOT NULL COMMENT 'id',
    dict_type_id         bigint NOT NULL COMMENT '字典类型ID',
    dict_label           varchar(255) NOT NULL COMMENT '字典标签',
    dict_value           varchar(255) COMMENT '字典值',
    remark               varchar(255) COMMENT '备注',
    sort                 int unsigned COMMENT '排序',
    creator              bigint COMMENT '创建者',
    create_date          datetime COMMENT '创建时间',
    updater              bigint COMMENT '更新者',
    update_date          datetime COMMENT '更新时间',
    primary key (id),
    unique key uk_dict_type_value (dict_type_id, dict_value),
    key idx_sort (sort)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='字典数据';

-- 行政区域
CREATE TABLE sys_region (
    id bigint NOT NULL COMMENT 'id',
    pid bigint COMMENT '上级ID，一级为0',
    name varchar(100) COMMENT '名称',
    tree_level tinyint COMMENT '层级',
    leaf tinyint COMMENT '是否叶子节点  0：否   1：是',
    sort bigint COMMENT '排序',
    creator bigint COMMENT '创建者',
    create_date datetime COMMENT '创建时间',
    updater bigint COMMENT '更新者',
    update_date datetime COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='行政区域';

create table sys_params
(
  id                   bigint not null comment 'id',
  param_code           varchar(32) comment '参数编码',
  param_value          varchar(2000) comment '参数值',
  param_type           tinyint(4) unsigned default 1 comment '类型   0：系统参数   1：非系统参数',
  remark               varchar(200) comment '备注',
  del_flag             tinyint(4) unsigned comment '删除标识  0：未删除    1：删除',
  creator              bigint comment '创建者',
  create_date          datetime comment '创建时间',
  updater              bigint comment '更新者',
  update_date          datetime comment '更新时间',
  primary key (id),
  unique key uk_param_code (param_code),
  key idx_del_flag (del_flag),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='参数管理';


create table sys_log_login
(
  id                   bigint not null comment 'id',
  operation            tinyint unsigned comment '用户操作   0：用户登录   1：用户退出',
  user_agent           varchar(500) comment '用户代理',
  ip                   varchar(160) comment '操作IP',
  creator_name         varchar(50) comment '用户名',
  creator              bigint comment '创建者',
  create_date          datetime comment '创建时间',
  primary key (id),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='登录日志';


create table sys_log_operation
(
  id                   bigint not null comment 'id',
  module               varchar(32) comment '模块名称，如：sys',
  operation            varchar(50) comment '用户操作',
  request_uri          varchar(200) comment '请求URI',
  request_method       varchar(20) comment '请求方式',
  request_params       text comment '请求参数',
  request_time         int unsigned not null comment '请求时长(毫秒)',
  user_agent           varchar(500) comment '用户代理',
  ip                   varchar(160) comment '操作IP',
  status               tinyint(4) unsigned not null comment '状态  0：失败   1：成功',
  creator_name         varchar(50) comment '用户名',
  creator              bigint comment '创建者',
  create_date          datetime comment '创建时间',
  primary key (id),
  key idx_module (module),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='操作日志';


create table sys_log_error
(
  id                   bigint not null comment 'id',
  module               varchar(50) comment '模块名称，如：sys',
  request_uri          varchar(200) comment '请求URI',
  request_method       varchar(20) comment '请求方式',
  request_params       text comment '请求参数',
  user_agent           varchar(500) comment '用户代理',
  ip                   varchar(160) comment '操作IP',
  error_info           text comment '异常信息',
  creator              bigint comment '创建者',
  create_date          datetime comment '创建时间',
  primary key (id),
  key idx_module (module),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='异常日志';


CREATE TABLE sys_language (
  table_name varchar(32) NOT NULL COMMENT '表名',
  table_id bigint NOT NULL COMMENT '表主键',
  field_name varchar(32) NOT NULL COMMENT '字段名',
  field_value varchar(200) NOT NULL COMMENT '字段值',
  language varchar(10) NOT NULL COMMENT '语言',
  primary key (table_name, table_id, field_name, language),
  key idx_table_id (table_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='国际化';

-- 新闻管理
CREATE TABLE tb_news (
 id bigint NOT NULL COMMENT 'id',
 title varchar(255) NOT NULL COMMENT '标题',
 content mediumtext NOT NULL COMMENT '内容',
 pub_date datetime COMMENT '发布时间',
 dept_id bigint COMMENT '创建者dept_id',
 creator bigint COMMENT '创建者',
 create_date datetime COMMENT '创建时间',
 updater bigint COMMENT '更新者',
 update_date datetime COMMENT '更新时间',
 PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='新闻管理';

-- 通知管理
CREATE TABLE sys_notice (
    id bigint NOT NULL COMMENT 'id',
    type int NOT NULL COMMENT '通知类型',
    title varchar(200) COMMENT '标题',
    content text COMMENT '内容',
    receiver_type tinyint unsigned COMMENT '接收者  0：全部  1：部门',
    receiver_type_ids varchar(500) COMMENT '接收者ID，用逗号分开',
    status tinyint unsigned COMMENT '发送状态  0：草稿  1：已发布',
    sender_name varchar(50) COMMENT '发送者',
    sender_date datetime COMMENT '发送时间',
    creator bigint COMMENT '创建者',
    create_date datetime COMMENT '创建时间',
    PRIMARY KEY (id),
    key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='通知管理';

-- 我的通知
CREATE TABLE sys_notice_user (
    receiver_id bigint NOT NULL COMMENT '接收者ID',
    notice_id bigint NOT NULL COMMENT '通知ID',
    read_status tinyint unsigned COMMENT '阅读状态  0：未读  1：已读',
    read_date datetime COMMENT '阅读时间',
    PRIMARY KEY (receiver_id, notice_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='我的通知';

-- OAuth客户端令牌
CREATE TABLE sys_oauth_client_details (
    client_id varchar(128) NOT NULL COMMENT '客户端id',
    resource_ids varchar(128) COMMENT '资源ids',
    client_secret varchar(128) COMMENT '客户端密钥',
    scope varchar(128) COMMENT '授权范围',
    authorized_grant_types varchar(128) COMMENT '授权类型',
    web_server_redirect_uri varchar(128) COMMENT '回调地址',
    authorities varchar(128) COMMENT '权限标识',
    access_token_validity int COMMENT '访问令牌有效期',
    refresh_token_validity int COMMENT '刷新令牌有效期',
    additional_information varchar(4096) COMMENT '附加信息',
    autoapprove varchar(128) COMMENT '自动授权',
    PRIMARY KEY (client_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='OAuth客户端令牌';

-- OAuth授权码
CREATE TABLE oauth_code (
    code varchar(128),
    authentication blob
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='OAuth授权码';


INSERT INTO sys_user(id, username, password, real_name, head_url, gender, email, mobile, dept_id, super_admin, status, remark, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000001, 'admin', '$2a$10$012Kx2ba5jzqr9gLlG4MX.bnQJTD9UWqF57XDo2N3.fPtLne02u/m', '超级管理员', NULL, 1, 'root@renren.io', '13512345678', NULL, 1, 1, NULL, 0, NULL, now(), NULL, now());

INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000002, 0, '', 0, 'icon-lock', '', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000003, 1067246875800000002, 'sys/user', 0, 'icon-user', '', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000004, 1067246875800000003, '', 1, '', 'sys:user:page,sys:user:info', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000005, 1067246875800000003, '', 1, '', 'sys:user:save,sys:dept:list,sys:role:list', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000006, 1067246875800000003, '', 1, '', 'sys:user:update,sys:dept:list,sys:role:list', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000007, 1067246875800000003, '', 1, '', 'sys:user:delete', 3, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000008, 1067246875800000003, '', 1, '', 'sys:user:export', 4, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000009, 1067246875800000002, 'sys/dept', 0, 'icon-apartment', '', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000010, 1067246875800000009, '', 1, '', 'sys:dept:list,sys:dept:info', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000011, 1067246875800000009, '', 1, '', 'sys:dept:save', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000012, 1067246875800000009, '', 1, '', 'sys:dept:update', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000013, 1067246875800000009, '', 1, '', 'sys:dept:delete', 3, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000014, 1067246875800000002, 'sys/role', 0, 'icon-team', '', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000015, 1067246875800000014, '', 1, '', 'sys:role:page,sys:role:info', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000016, 1067246875800000014, '', 1, '', 'sys:role:save,sys:menu:select,sys:dept:list', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000017, 1067246875800000014, '', 1, '', 'sys:role:update,sys:menu:select,sys:dept:list', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000018, 1067246875800000014, '', 1, '', 'sys:role:delete', 3, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000019, 0, '', 0, 'icon-setting', NULL, 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000020, 1067246875800000019, 'sys/menu', 0, 'icon-unorderedlist', NULL, 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000021, 1067246875800000020, NULL, 1, NULL, 'sys:menu:list,sys:menu:info', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000022, 1067246875800000020, NULL, 1, NULL, 'sys:menu:save', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000023, 1067246875800000020, NULL, 1, NULL, 'sys:menu:update', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000024, 1067246875800000020, NULL, 1, NULL, 'sys:menu:delete', 3, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000025, 1067246875800000019, 'sys/params', 0, 'icon-fileprotect', '', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000026, 1067246875800000025, NULL, 1, NULL, 'sys:params:page,sys:params:info', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000027, 1067246875800000025, NULL, 1, NULL, 'sys:params:save', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000028, 1067246875800000025, NULL, 1, NULL, 'sys:params:update', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000029, 1067246875800000025, NULL, 1, NULL, 'sys:params:delete', 3, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000030, 1067246875800000025, '', 1, NULL, 'sys:params:export', 4, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000031, 1067246875800000019, 'sys/dict-type', 0, 'icon-gold', '', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000032, 1067246875800000031, '', 1, '', 'sys:dict:page,sys:dict:info', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000033, 1067246875800000031, '', 1, '', 'sys:dict:save', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000034, 1067246875800000031, '', 1, '', 'sys:dict:update', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000035, 1067246875800000031, '', 1, '', 'sys:dict:delete', 3, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000036, 0, '', 0, 'icon-container', '', 4, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000037, 1067246875800000036, 'sys/log-login', 0, 'icon-filedone', 'sys:log:login', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000038, 1067246875800000036, 'sys/log-operation', 0, 'icon-solution', 'sys:log:operation', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000039, 1067246875800000036, 'sys/log-error', 0, 'icon-file-exception', 'sys:log:error', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000040, 0, '', 0, 'icon-desktop', '', 5, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000041, 1067246875800000040, '{{ window.SITE_CONFIG["apiURL"] }}/monitor', 0, 'icon-medicinebox', '', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000042, 1067246875800000040, '{{ window.SITE_CONFIG["apiURL"] }}/doc.html', 0, 'icon-file-word', '', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1164489061834969089, 1067246875800000019, 'sys/region', 0, 'icon-location', '0', 3, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1164492214366130178, 1164489061834969089, '', 1, '', 'sys:region:list,sys:region:info', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1164492872829915138, 1164489061834969089, '', 1, '', 'sys:region:save', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1164493252347318273, 1164489061834969089, '', 1, '', 'sys:region:update', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1164493391254278145, 1164489061834969089, '', 1, '', 'sys:region:delete', 3, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1176372255559024642, 0, '', 0, 'icon-windows', '', 999, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1206460008292216834, 1176372255559024642, 'sys/news', 0, 'icon-file-word', 'demo:news:all', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000401, 0, '', 0, 'icon-bell', '', 4, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000402, 1067246875800000401, 'sys/notice', 0, 'icon-bell', 'sys:notice:all,sys:dept:list', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000403, 1067246875800000401, 'sys/notice-user', 0, 'icon-notification', 'sys:notice:all', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());


INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000002, 'name', '权限管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000002, 'name', '權限管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000002, 'name', 'Authority Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000003, 'name', '用户管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000003, 'name', '用戶管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000003, 'name', 'User Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000004, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000004, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000004, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000005, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000005, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000005, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000006, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000006, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000006, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000007, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000007, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000007, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000008, 'name', 'Export', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000008, 'name', '导出', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000008, 'name', '導出', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000009, 'name', 'Department Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000009, 'name', '部门管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000009, 'name', '部門管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000010, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000010, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000010, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000011, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000011, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000011, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000012, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000012, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000012, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000013, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000013, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000013, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000014, 'name', 'Role Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000014, 'name', '角色管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000014, 'name', '角色管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000015, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000015, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000015, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000016, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000016, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000016, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000017, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000017, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000017, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000018, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000018, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000018, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000019, 'name', 'Setting', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000019, 'name', '系统设置', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000019, 'name', '系統設置', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000020, 'name', 'Menu Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000020, 'name', '菜单管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000020, 'name', '菜單管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000021, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000021, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000021, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000022, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000022, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000022, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000023, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000023, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000023, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000024, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000024, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000024, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000025, 'name', 'Parameter Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000025, 'name', '参数管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000025, 'name', '參數管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000026, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000026, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000026, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000027, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000027, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000027, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000028, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000028, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000028, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000029, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000029, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000029, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000030, 'name', 'Export', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000030, 'name', '导出', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000030, 'name', '導出', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000031, 'name', 'Dict Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000031, 'name', '字典管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000031, 'name', '字典管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000032, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000032, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000032, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000033, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000033, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000033, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000034, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000034, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000034, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000035, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000035, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000035, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000036, 'name', 'Log Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000036, 'name', '日志管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000036, 'name', '日誌管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000037, 'name', 'Login Log', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000037, 'name', '登录日志', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000037, 'name', '登錄日誌', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000038, 'name', 'Operation Log', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000038, 'name', '操作日志', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000038, 'name', '操作日誌', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000039, 'name', 'Error Log', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000039, 'name', '异常日志', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000039, 'name', '異常日誌', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000040, 'name', 'System Monitoring', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000040, 'name', '系统监控', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000040, 'name', '系統監控', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000041, 'name', 'Service Monitoring', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000041, 'name', '服务监控', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000041, 'name', '服務監控', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000042, 'name', 'Swagger Api', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000042, 'name', '接口文档', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000042, 'name', '接口文檔', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164489061834969089, 'name', 'Administrative Regions', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164489061834969089, 'name', '行政区域', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164489061834969089, 'name', '行政區域', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164492214366130178, 'name', 'View', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164492214366130178, 'name', '查看', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164492214366130178, 'name', '查看', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164492872829915138, 'name', 'Add', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164492872829915138, 'name', '新增', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164492872829915138, 'name', '新增', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164493252347318273, 'name', 'Edit', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164493252347318273, 'name', '修改', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164493252347318273, 'name', '修改', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164493391254278145, 'name', 'Delete', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164493391254278145, 'name', '删除', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1164493391254278145, 'name', '刪除', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1176372255559024642, 'name', 'Demo', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1176372255559024642, 'name', '功能示例', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1176372255559024642, 'name', '功能示例', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1206460008292216834, 'name', 'News Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1206460008292216834, 'name', '新闻管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1206460008292216834, 'name', '新聞管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000401, 'name', 'Station Notice', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000401, 'name', '站内通知', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000401, 'name', '站內通知', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000402, 'name', 'Notice Management', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000402, 'name', '通知管理', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000402, 'name', '通知管理', 'zh-TW');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000403, 'name', 'My Notice', 'en-US');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000403, 'name', '我的通知', 'zh-CN');
INSERT INTO sys_language(table_name, table_id, field_name, field_value, language) VALUES ('sys_menu', 1067246875800000403, 'name', '我的通知', 'zh-TW');

INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000061, 1067246875800000062, '1067246875800000065,1067246875800000062', '技术部', 2, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000062, 1067246875800000065, '1067246875800000065', '长沙分公司', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000063, 1067246875800000065, '1067246875800000065', '上海分公司', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000064, 1067246875800000063, '1067246875800000065,1067246875800000063', '市场部', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000065, 0, '0', '集团', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000066, 1067246875800000063, '1067246875800000065,1067246875800000063', '销售部', 0, 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000067, 1067246875800000062, '1067246875800000065,1067246875800000062', '产品部', 1, 0, 1067246875800000001, now(), 1067246875800000001, now());

INSERT INTO sys_dict_type(id, dict_type, dict_name, remark, sort, creator, create_date, updater, update_date) VALUES (1160061077912858625, 'gender', '性别', '', 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061112075464705, 1160061077912858625, '男', '0', '', 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061146967879681, 1160061077912858625, '女', '1', '', 1, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061190127267841, 1160061077912858625, '保密', '2', '', 2, 1067246875800000001, now(), 1067246875800000001, now());

INSERT INTO sys_dict_type(id, dict_type, dict_name, remark, sort, creator, create_date, updater, update_date) VALUES (1225813644059140097, 'notice_type', '站内通知-类型', '', 1, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814069634195457, 1225813644059140097, '公告', '0', '', 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814107559092225, 1225813644059140097, '会议', '1', '', 1, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814271879340034, 1225813644059140097, '其他', '2', '', 2, 1067246875800000001, now(), 1067246875800000001, now());


INSERT INTO sys_oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('renrenio', '', '$2a$10$aiWOK7GmRzyLyRihSF0cMedcbsEo0t11/y8f.tnBkF2hCeSd0Bq.i', 'all', 'authorization_code,password,implicit,client_credentials,refresh_token', 'https://www.renren.io/', NULL, 50000, NULL, NULL, 'true');

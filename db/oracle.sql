create table sys_user
(
  id                   NUMBER(20, 0) not null,
  username             varchar2(50),
  password             varchar2(100),
  real_name            varchar2(50),
  head_url             varchar2(200),
  gender               NUMBER(2, 0),
  email                varchar2(100),
  mobile               varchar2(20),
  dept_id              NUMBER(20, 0),
  super_admin          NUMBER(2, 0),
  status               NUMBER(2, 0),
  remark               varchar2(200),
  del_flag             NUMBER(2, 0),
  creator              NUMBER(20, 0),
  create_date          date,
  updater              NUMBER(20, 0),
  update_date          date,
  primary key (id)
);
CREATE UNIQUE INDEX uk_sys_user_username on sys_user(username);
CREATE INDEX idx_sys_user_del_flag on sys_user(del_flag);
CREATE INDEX idx_sys_user_create_date on sys_user(create_date);

COMMENT ON TABLE sys_user IS '用户管理';
COMMENT ON COLUMN sys_user.id IS 'id';
COMMENT ON COLUMN sys_user.username IS '用户名';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.real_name IS '姓名';
COMMENT ON COLUMN sys_user.head_url IS '头像';
COMMENT ON COLUMN sys_user.gender IS '性别   0：男   1：女    2：保密';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.mobile IS '手机号';
COMMENT ON COLUMN sys_user.dept_id IS '部门ID';
COMMENT ON COLUMN sys_user.super_admin IS '超级管理员   0：否   1：是';
COMMENT ON COLUMN sys_user.status IS '状态  0：停用    1：正常';
COMMENT ON COLUMN sys_user.remark IS '备注';
COMMENT ON COLUMN sys_user.del_flag IS '删除标识  0：未删除    1：删除';
COMMENT ON COLUMN sys_user.creator IS '创建者';
COMMENT ON COLUMN sys_user.create_date IS '创建时间';
COMMENT ON COLUMN sys_user.updater IS '更新者';
COMMENT ON COLUMN sys_user.update_date IS '更新时间';


create table sys_dept
(
  id                   NUMBER(20, 0) not null,
  pid                  NUMBER(20, 0),
  pids                 varchar2(500),
  name                 varchar2(50),
  sort                 NUMBER(10, 0),
  del_flag             NUMBER(2, 0),
  creator              NUMBER(20, 0),
  create_date          date,
  updater              NUMBER(20, 0),
  update_date          date,
  primary key (id)
);
CREATE INDEX idx_sys_dept_pid on sys_dept(pid);
CREATE INDEX idx_sys_dept_del_flag on sys_dept(del_flag);
CREATE INDEX idx_sys_dept_create_date on sys_dept(create_date);

COMMENT ON TABLE sys_dept IS '部门管理';
COMMENT ON COLUMN sys_dept.id IS 'id';
COMMENT ON COLUMN sys_dept.pid IS '上级ID';
COMMENT ON COLUMN sys_dept.pids IS '所有上级ID，用逗号分开';
COMMENT ON COLUMN sys_dept.name IS '部门名称';
COMMENT ON COLUMN sys_dept.sort IS '排序';
COMMENT ON COLUMN sys_dept.del_flag IS '删除标识  0：未删除    1：删除';
COMMENT ON COLUMN sys_dept.creator IS '创建者';
COMMENT ON COLUMN sys_dept.create_date IS '创建时间';
COMMENT ON COLUMN sys_dept.updater IS '更新者';
COMMENT ON COLUMN sys_dept.update_date IS '更新时间';


create table sys_menu
(
  id                   NUMBER(20, 0) not null,
  pid                  NUMBER(20, 0),
  url                  varchar2(200),
  type                 NUMBER(2, 0),
  icon                 varchar2(50),
  permissions          varchar2(200),
  sort                 NUMBER(10, 0),
  del_flag             NUMBER(2, 0),
  creator              NUMBER(20, 0),
  create_date          date,
  updater              NUMBER(20, 0),
  update_date          date,
  primary key (id)
);
CREATE INDEX idx_sys_menu_pid on sys_menu(pid);
CREATE INDEX idx_sys_menu_del_flag on sys_menu(del_flag);
CREATE INDEX idx_sys_menu_create_date on sys_menu(create_date);

COMMENT ON TABLE sys_menu IS '菜单管理';
COMMENT ON COLUMN sys_menu.id IS 'id';
COMMENT ON COLUMN sys_menu.pid IS '上级ID，一级菜单为0';
COMMENT ON COLUMN sys_menu.url IS '菜单URL';
COMMENT ON COLUMN sys_menu.type IS '类型   0：菜单   1：按钮';
COMMENT ON COLUMN sys_menu.icon IS '菜单图标';
COMMENT ON COLUMN sys_menu.permissions IS '权限标识，如：sys:menu:save';
COMMENT ON COLUMN sys_menu.sort IS '排序';
COMMENT ON COLUMN sys_menu.del_flag IS '删除标识  0：未删除    1：删除';
COMMENT ON COLUMN sys_menu.creator IS '创建者';
COMMENT ON COLUMN sys_menu.create_date IS '创建时间';
COMMENT ON COLUMN sys_menu.updater IS '更新者';
COMMENT ON COLUMN sys_menu.update_date IS '更新时间';


create table sys_role
(
  id                   NUMBER(20, 0) not null,
  name                 varchar2(32),
  remark               varchar2(100),
  del_flag             NUMBER(2, 0),
  dept_id              NUMBER(20, 0),
  creator              NUMBER(20, 0),
  create_date          date,
  updater              NUMBER(20, 0),
  update_date          date,
  primary key (id)
);
CREATE INDEX idx_sys_role_dept_id on sys_role(dept_id);
CREATE INDEX idx_sys_role_del_flag on sys_role(del_flag);
CREATE INDEX idx_sys_role_create_date on sys_role(create_date);

COMMENT ON TABLE sys_role IS '角色管理';
COMMENT ON COLUMN sys_role.id IS 'id';
COMMENT ON COLUMN sys_role.name IS '角色名称';
COMMENT ON COLUMN sys_role.remark IS '备注';
COMMENT ON COLUMN sys_role.del_flag IS '删除标识  0：未删除    1：删除';
COMMENT ON COLUMN sys_role.dept_id IS '部门ID';
COMMENT ON COLUMN sys_role.creator IS '创建者';
COMMENT ON COLUMN sys_role.create_date IS '创建时间';
COMMENT ON COLUMN sys_role.updater IS '更新者';
COMMENT ON COLUMN sys_role.update_date IS '更新时间';


create table sys_role_user
(
  id                   NUMBER(20, 0) not null,
  role_id              NUMBER(20, 0),
  user_id              NUMBER(20, 0),
  creator              NUMBER(20, 0),
  create_date          date,
  primary key (id)
);
CREATE INDEX idx_sys_role_user_role_id on sys_role_user(role_id);
CREATE INDEX idx_sys_role_user_user_id on sys_role_user(user_id);

COMMENT ON TABLE sys_role_user IS '角色用户关系';
COMMENT ON COLUMN sys_role_user.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_user.user_id IS '用户ID';
COMMENT ON COLUMN sys_role_user.creator IS '创建者';
COMMENT ON COLUMN sys_role_user.create_date IS '创建时间';


create table sys_role_menu
(
  id                   NUMBER(20, 0) not null,
  role_id              NUMBER(20, 0),
  menu_id              NUMBER(20, 0),
  creator              NUMBER(20, 0),
  create_date          date,
  primary key (id)
);
CREATE INDEX idx_sys_role_menu_role_id on sys_role_menu(role_id);
CREATE INDEX idx_sys_role_menu_menu_id on sys_role_menu(menu_id);

COMMENT ON TABLE sys_role_menu IS '角色菜单关系';
COMMENT ON COLUMN sys_role_menu.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_menu.menu_id IS '菜单ID';
COMMENT ON COLUMN sys_role_menu.creator IS '创建者';
COMMENT ON COLUMN sys_role_menu.create_date IS '创建时间';


create table sys_role_data_scope
(
  id                   NUMBER(20, 0) not null,
  role_id              NUMBER(20, 0),
  dept_id              NUMBER(20, 0),
  creator              NUMBER(20, 0),
  create_date          date,
  primary key (id)
);
CREATE INDEX idx_data_scope_role_id on sys_role_data_scope(role_id);

COMMENT ON TABLE sys_role_data_scope IS '角色数据权限';
COMMENT ON COLUMN sys_role_data_scope.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_data_scope.dept_id IS '部门ID';
COMMENT ON COLUMN sys_role_data_scope.creator IS '创建者';
COMMENT ON COLUMN sys_role_data_scope.create_date IS '创建时间';


create table sys_dict_type
(
    id                   NUMBER(20, 0) NOT NULL,
    dict_type            varchar2(100),
    dict_name            varchar2(255),
    remark               varchar2(255),
    sort                 NUMBER(10, 0),
    creator              NUMBER(20, 0),
    create_date          date,
    updater              NUMBER(20, 0),
    update_date          date,
    primary key (id)
);
CREATE UNIQUE INDEX uk_sys_dict_type_dict_type on sys_dict_type(dict_type);

COMMENT ON TABLE sys_dict_type IS '字典类型';
COMMENT ON COLUMN sys_dict_type.id IS 'id';
COMMENT ON COLUMN sys_dict_type.dict_type IS '字典类型';
COMMENT ON COLUMN sys_dict_type.dict_name IS '字典名称';
COMMENT ON COLUMN sys_dict_type.remark IS '备注';
COMMENT ON COLUMN sys_dict_type.sort IS '排序';
COMMENT ON COLUMN sys_dict_type.creator IS '创建者';
COMMENT ON COLUMN sys_dict_type.create_date IS '创建时间';
COMMENT ON COLUMN sys_dict_type.updater IS '更新者';
COMMENT ON COLUMN sys_dict_type.update_date IS '更新时间';

create table sys_dict_data
(
    id                   NUMBER(20, 0) NOT NULL,
    dict_type_id         NUMBER(20, 0) NOT NULL,
    dict_label           varchar2(255),
    dict_value           varchar2(255),
    remark               varchar2(255),
    sort                 NUMBER(10, 0),
    creator              NUMBER(20, 0),
    create_date          date,
    updater              NUMBER(20, 0),
    update_date          date,
    primary key (id)
);
CREATE INDEX idx_sys_dict_data_sort on sys_dict_data(sort);
CREATE UNIQUE INDEX uk_dict_type_value on sys_dict_data(dict_type_id, dict_value);

COMMENT ON TABLE sys_dict_data IS '字典数据';
COMMENT ON COLUMN sys_dict_data.id IS 'id';
COMMENT ON COLUMN sys_dict_data.dict_type_id IS '字典类型ID';
COMMENT ON COLUMN sys_dict_data.dict_label IS '字典标签';
COMMENT ON COLUMN sys_dict_data.dict_value IS '字典值';
COMMENT ON COLUMN sys_dict_data.remark IS '备注';
COMMENT ON COLUMN sys_dict_data.sort IS '排序';
COMMENT ON COLUMN sys_dict_data.creator IS '创建者';
COMMENT ON COLUMN sys_dict_data.create_date IS '创建时间';
COMMENT ON COLUMN sys_dict_data.updater IS '更新者';
COMMENT ON COLUMN sys_dict_data.update_date IS '更新时间';

CREATE TABLE sys_region (
    id NUMBER(20, 0) NOT NULL,
    pid NUMBER(20, 0),
    name varchar2(100),
    tree_level NUMBER(2, 0),
    leaf NUMBER(2, 0),
    sort NUMBER(20, 0),
    creator NUMBER(20, 0),
    create_date date,
    updater NUMBER(20, 0),
    update_date date,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_region IS '行政区域';
COMMENT ON COLUMN sys_region.pid IS '上级ID';
COMMENT ON COLUMN sys_region.name IS '名称';
COMMENT ON COLUMN sys_region.tree_level IS '层级';
COMMENT ON COLUMN sys_region.leaf IS '是否叶子节点  0：否   1：是';
COMMENT ON COLUMN sys_region.sort IS '排序';
COMMENT ON COLUMN sys_region.creator IS '创建者';
COMMENT ON COLUMN sys_region.create_date IS '创建时间';
COMMENT ON COLUMN sys_region.updater IS '更新者';
COMMENT ON COLUMN sys_region.update_date IS '更新时间';


create table sys_params
(
  id                   NUMBER(20, 0) not null,
  param_code           varchar2(32),
  param_value          varchar2(2000),
  param_type           NUMBER(2, 0) DEFAULT 1 NOT NULL,
  remark               varchar2(200),
  del_flag             NUMBER(2, 0),
  creator              NUMBER(20, 0),
  create_date          date,
  updater              NUMBER(20, 0),
  update_date          date,
  primary key (id)
);
CREATE UNIQUE INDEX uk_sys_params_param_code on sys_params(param_code);
CREATE INDEX idx_sys_params_del_flag on sys_params(del_flag);
CREATE INDEX idx_sys_params_create_date on sys_params(create_date);

COMMENT ON TABLE sys_params IS '参数管理';
COMMENT ON COLUMN sys_params.param_code IS '参数编码';
COMMENT ON COLUMN sys_params.param_value IS '参数值';
COMMENT ON COLUMN sys_params.param_type IS '类型   0：非系统参数   1：系统参数';
COMMENT ON COLUMN sys_params.remark IS '备注';
COMMENT ON COLUMN sys_params.del_flag IS '删除标识  0：未删除    1：删除';
COMMENT ON COLUMN sys_params.creator IS '创建者';
COMMENT ON COLUMN sys_params.create_date IS '创建时间';
COMMENT ON COLUMN sys_params.updater IS '更新者';
COMMENT ON COLUMN sys_params.update_date IS '更新时间';


create table sys_log_login
(
  id                   NUMBER(20, 0) not null,
  operation            NUMBER(2, 0),
  user_agent           varchar2(500),
  ip                   varchar2(160),
  creator_name         varchar2(50),
  creator              NUMBER(20, 0),
  create_date          date,
  primary key (id)
);
CREATE INDEX idx_login_create_date on sys_log_login(create_date);

COMMENT ON TABLE sys_log_login IS '登录日志';
COMMENT ON COLUMN sys_log_login.id IS 'id';
COMMENT ON COLUMN sys_log_login.operation IS '用户操作   0：用户登录   1：用户退出';
COMMENT ON COLUMN sys_log_login.user_agent IS '用户代理';
COMMENT ON COLUMN sys_log_login.ip IS '操作IP';
COMMENT ON COLUMN sys_log_login.creator_name IS '用户名';
COMMENT ON COLUMN sys_log_login.creator IS '创建者';
COMMENT ON COLUMN sys_log_login.create_date IS '创建时间';


create table sys_log_operation
(
  id                   NUMBER(20, 0) not null,
  module               varchar2(32),
  operation            varchar2(50),
  request_uri          varchar2(200),
  request_method       varchar2(20),
  request_params       clob,
  request_time         NUMBER(10, 0),
  user_agent           varchar2(500),
  ip                   varchar2(160),
  status               NUMBER(2, 0),
  creator_name         varchar2(50),
  creator              NUMBER(20, 0),
  create_date          date,
  primary key (id)
);
CREATE INDEX idx_operation_module on sys_log_operation(module);
CREATE INDEX idx_operation_create_date on sys_log_operation(create_date);

COMMENT ON TABLE sys_log_operation IS '操作日志';
COMMENT ON COLUMN sys_log_operation.id IS 'id';
COMMENT ON COLUMN sys_log_operation.module IS '模块名称，如：sys';
COMMENT ON COLUMN sys_log_operation.operation IS '用户操作';
COMMENT ON COLUMN sys_log_operation.request_uri IS '请求URI';
COMMENT ON COLUMN sys_log_operation.request_method IS '请求方式';
COMMENT ON COLUMN sys_log_operation.request_params IS '请求参数';
COMMENT ON COLUMN sys_log_operation.request_time IS '请求时长(毫秒)';
COMMENT ON COLUMN sys_log_operation.user_agent IS '用户代理';
COMMENT ON COLUMN sys_log_operation.ip IS '操作IP';
COMMENT ON COLUMN sys_log_operation.status IS '状态  0：失败   1：成功';
COMMENT ON COLUMN sys_log_operation.creator_name IS '用户名';
COMMENT ON COLUMN sys_log_operation.creator IS '创建者';
COMMENT ON COLUMN sys_log_operation.create_date IS '创建时间';


create table sys_log_error
(
  id                   NUMBER(20, 0) not null,
  module               varchar2(50),
  request_uri          varchar2(200),
  request_method       varchar2(20),
  request_params       clob,
  user_agent           varchar2(500),
  ip                   varchar2(160),
  error_info           clob,
  creator              NUMBER(20, 0),
  create_date          date,
  primary key (id)
);
CREATE INDEX idx_error_module on sys_log_error(module);
CREATE INDEX idx_error_create_date on sys_log_error(create_date);

COMMENT ON TABLE sys_log_error IS '异常日志';
COMMENT ON COLUMN sys_log_error.id IS 'id';
COMMENT ON COLUMN sys_log_error.module IS '模块名称，如：sys';
COMMENT ON COLUMN sys_log_error.request_uri IS '请求URI';
COMMENT ON COLUMN sys_log_error.request_method IS '请求方式';
COMMENT ON COLUMN sys_log_error.request_params IS '请求参数';
COMMENT ON COLUMN sys_log_error.user_agent IS '用户代理';
COMMENT ON COLUMN sys_log_error.ip IS '操作IP';
COMMENT ON COLUMN sys_log_error.error_info IS '异常信息';
COMMENT ON COLUMN sys_log_error.creator IS '创建者';
COMMENT ON COLUMN sys_log_error.create_date IS '创建时间';


CREATE TABLE sys_language (
  table_name varchar2(32) NOT NULL,
  table_id NUMBER(20, 0) NOT NULL,
  field_name varchar2(32) NOT NULL,
  field_value varchar2(200) NOT NULL,
  language varchar2(10) NOT NULL,
  primary key (table_name, table_id, field_name, language)
);

CREATE INDEX idx_sys_language_table_id on sys_language(table_id);

COMMENT ON TABLE sys_language IS '国际化';
COMMENT ON COLUMN sys_language.table_name IS '表名';
COMMENT ON COLUMN sys_language.table_id IS '表主键';
COMMENT ON COLUMN sys_language.field_name IS '字段名';
COMMENT ON COLUMN sys_language.field_value IS '字段值';
COMMENT ON COLUMN sys_language.language IS '语言';

CREATE TABLE tb_news (
 id NUMBER(20, 0) NOT NULL,
 title varchar2(100),
 content clob,
 pub_date date,
 dept_id NUMBER(20, 0),
 creator NUMBER(20, 0),
 create_date date,
 updater NUMBER(20, 0),
 update_date date,
 PRIMARY KEY (id)
);

COMMENT ON TABLE tb_news IS '新闻管理';
COMMENT ON COLUMN tb_news.id IS 'id';
COMMENT ON COLUMN tb_news.title IS '标题';
COMMENT ON COLUMN tb_news.content IS '内容';
COMMENT ON COLUMN tb_news.pub_date IS '发布时间';
COMMENT ON COLUMN tb_news.dept_id IS '创建者dept_id';
COMMENT ON COLUMN tb_news.creator IS '创建者';
COMMENT ON COLUMN tb_news.create_date IS '创建时间';
COMMENT ON COLUMN tb_news.updater IS '更新者';
COMMENT ON COLUMN tb_news.update_date IS '更新时间';

CREATE TABLE sys_notice (
    id NUMBER(20, 0) NOT NULL,
    type NUMBER(10, 0) NOT NULL,
    title varchar2(200),
    content clob,
    receiver_type NUMBER(2, 0),
    receiver_type_ids varchar2(500),
    status NUMBER(2, 0),
    sender_name varchar2(50),
    sender_date date,
    creator NUMBER(20, 0),
    create_date date,
    PRIMARY KEY (id)
);
CREATE INDEX idx_sys_notice_create_date on sys_notice(create_date);

COMMENT ON TABLE sys_notice IS '通知管理';
COMMENT ON COLUMN sys_notice.id IS 'id';
COMMENT ON COLUMN sys_notice.type IS '通知类型';
COMMENT ON COLUMN sys_notice.title IS '标题';
COMMENT ON COLUMN sys_notice.content IS '内容';
COMMENT ON COLUMN sys_notice.receiver_type IS '接收者  0：全部  1：部门';
COMMENT ON COLUMN sys_notice.receiver_type_ids IS '接收者ID，用逗号分开';
COMMENT ON COLUMN sys_notice.status IS '发送状态  0：草稿  1：已发布';
COMMENT ON COLUMN sys_notice.sender_name IS '发送者';
COMMENT ON COLUMN sys_notice.sender_date IS '发送时间';
COMMENT ON COLUMN sys_notice.creator IS '创建者';
COMMENT ON COLUMN sys_notice.create_date IS '创建时间';


CREATE TABLE sys_notice_user (
    receiver_id NUMBER(20, 0) NOT NULL,
    notice_id NUMBER(20, 0) NOT NULL,
    read_status NUMBER(2, 0) NOT NULL,
    read_date date,
    PRIMARY KEY (receiver_id, notice_id)
);

COMMENT ON TABLE sys_notice_user IS '我的通知';
COMMENT ON COLUMN sys_notice_user.receiver_id IS '接收者ID';
COMMENT ON COLUMN sys_notice_user.notice_id IS '通知ID';
COMMENT ON COLUMN sys_notice_user.read_status IS '阅读状态  0：未读  1：已读';
COMMENT ON COLUMN sys_notice_user.read_date IS '阅读时间';


CREATE TABLE sys_oauth_client_details (
    client_id varchar2(128) NOT NULL,
    resource_ids varchar2(128),
    client_secret varchar2(128),
    scope varchar2(128),
    authorized_grant_types varchar2(128),
    web_server_redirect_uri varchar2(128),
    authorities varchar2(128),
    access_token_validity NUMBER(20, 0),
    refresh_token_validity NUMBER(20, 0),
    additional_information varchar2(4096),
    autoapprove varchar2(128),
    PRIMARY KEY (client_id)
);

COMMENT ON TABLE sys_oauth_client_details IS 'OAuth客户端令牌';
COMMENT ON COLUMN sys_oauth_client_details.client_id IS '客户端id';
COMMENT ON COLUMN sys_oauth_client_details.resource_ids IS '资源ids';
COMMENT ON COLUMN sys_oauth_client_details.client_secret IS '客户端密钥';
COMMENT ON COLUMN sys_oauth_client_details.scope IS '授权范围';
COMMENT ON COLUMN sys_oauth_client_details.authorized_grant_types IS '授权类型';
COMMENT ON COLUMN sys_oauth_client_details.web_server_redirect_uri IS '回调地址';
COMMENT ON COLUMN sys_oauth_client_details.authorities IS '权限标识';
COMMENT ON COLUMN sys_oauth_client_details.access_token_validity IS '访问令牌有效期';
COMMENT ON COLUMN sys_oauth_client_details.refresh_token_validity IS '刷新令牌有效期';
COMMENT ON COLUMN sys_oauth_client_details.additional_information IS '附加信息';
COMMENT ON COLUMN sys_oauth_client_details.autoapprove IS '自动授权';

CREATE TABLE oauth_code (
    code varchar2(128),
    authentication blob
);


INSERT INTO sys_user(id, username, password, real_name, head_url, gender, email, mobile, dept_id, super_admin, status, remark, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000001, 'admin', '$2a$10$012Kx2ba5jzqr9gLlG4MX.bnQJTD9UWqF57XDo2N3.fPtLne02u/m', '超级管理员', NULL, 1, 'root@renren.io', '13512345678', NULL, 1, 1, NULL, 0, NULL, CURRENT_DATE, NULL, CURRENT_DATE);

INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000002, 0, '', 0, 'icon-lock', '', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000003, 1067246875800000002, 'sys/user', 0, 'icon-user', '', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000004, 1067246875800000003, '', 1, '', 'sys:user:page,sys:user:info', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000005, 1067246875800000003, '', 1, '', 'sys:user:save,sys:dept:list,sys:role:list', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000006, 1067246875800000003, '', 1, '', 'sys:user:update,sys:dept:list,sys:role:list', 2, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000007, 1067246875800000003, '', 1, '', 'sys:user:delete', 3, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000008, 1067246875800000003, '', 1, '', 'sys:user:export', 4, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000009, 1067246875800000002, 'sys/dept', 0, 'icon-apartment', '', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000010, 1067246875800000009, '', 1, '', 'sys:dept:list,sys:dept:info', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000011, 1067246875800000009, '', 1, '', 'sys:dept:save', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000012, 1067246875800000009, '', 1, '', 'sys:dept:update', 2, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000013, 1067246875800000009, '', 1, '', 'sys:dept:delete', 3, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000014, 1067246875800000002, 'sys/role', 0, 'icon-team', '', 2, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000015, 1067246875800000014, '', 1, '', 'sys:role:page,sys:role:info', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000016, 1067246875800000014, '', 1, '', 'sys:role:save,sys:menu:select,sys:dept:list', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000017, 1067246875800000014, '', 1, '', 'sys:role:update,sys:menu:select,sys:dept:list', 2, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000018, 1067246875800000014, '', 1, '', 'sys:role:delete', 3, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000019, 0, '', 0, 'icon-setting', NULL, 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000020, 1067246875800000019, 'sys/menu', 0, 'icon-unorderedlist', NULL, 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000021, 1067246875800000020, NULL, 1, NULL, 'sys:menu:list,sys:menu:info', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000022, 1067246875800000020, NULL, 1, NULL, 'sys:menu:save', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000023, 1067246875800000020, NULL, 1, NULL, 'sys:menu:update', 2, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000024, 1067246875800000020, NULL, 1, NULL, 'sys:menu:delete', 3, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000025, 1067246875800000019, 'sys/params', 0, 'icon-fileprotect', '', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000026, 1067246875800000025, NULL, 1, NULL, 'sys:params:page,sys:params:info', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000027, 1067246875800000025, NULL, 1, NULL, 'sys:params:save', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000028, 1067246875800000025, NULL, 1, NULL, 'sys:params:update', 2, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000029, 1067246875800000025, NULL, 1, NULL, 'sys:params:delete', 3, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000030, 1067246875800000025, '', 1, NULL, 'sys:params:export', 4, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000031, 1067246875800000019, 'sys/dict-type', 0, 'icon-gold', '', 2, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000032, 1067246875800000031, '', 1, '', 'sys:dict:page,sys:dict:info', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000033, 1067246875800000031, '', 1, '', 'sys:dict:save', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000034, 1067246875800000031, '', 1, '', 'sys:dict:update', 2, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000035, 1067246875800000031, '', 1, '', 'sys:dict:delete', 3, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000036, 0, '', 0, 'icon-container', '', 4, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000037, 1067246875800000036, 'sys/log-login', 0, 'icon-filedone', 'sys:log:login', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000038, 1067246875800000036, 'sys/log-operation', 0, 'icon-solution', 'sys:log:operation', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000039, 1067246875800000036, 'sys/log-error', 0, 'icon-file-exception', 'sys:log:error', 2, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000040, 0, '', 0, 'icon-desktop', '', 5, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000041, 1067246875800000040, '{{ window.SITE_CONFIG["apiURL"] }}/monitor', 0, 'icon-medicinebox', '', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000042, 1067246875800000040, '{{ window.SITE_CONFIG["apiURL"] }}/doc.html', 0, 'icon-file-word', '', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1164489061834969089, 1067246875800000019, 'sys/region', 0, 'icon-location', '0', 3, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1164492214366130178, 1164489061834969089, '', 1, '', 'sys:region:list,sys:region:info', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1164492872829915138, 1164489061834969089, '', 1, '', 'sys:region:save', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1164493252347318273, 1164489061834969089, '', 1, '', 'sys:region:update', 2, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1164493391254278145, 1164489061834969089, '', 1, '', 'sys:region:delete', 3, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1176372255559024642, 0, '', 0, 'icon-windows', '', 999, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1206460008292216834, 1176372255559024642, 'sys/news', 0, 'icon-file-word', 'demo:news:all', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000401, 0, '', 0, 'icon-bell', '', 4, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000402, 1067246875800000401, 'sys/notice', 0, 'icon-bell', 'sys:notice:all,sys:dept:list', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_menu(id, pid, url, type, icon, permissions, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000403, 1067246875800000401, 'sys/notice-user', 0, 'icon-notification', 'sys:notice:all', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);


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

INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000061, 1067246875800000062, '1067246875800000065,1067246875800000062', '技术部', 2, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000062, 1067246875800000065, '1067246875800000065', '长沙分公司', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000063, 1067246875800000065, '1067246875800000065', '上海分公司', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000064, 1067246875800000063, '1067246875800000065,1067246875800000063', '市场部', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000065, 0, '0', '集团', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000066, 1067246875800000063, '1067246875800000065,1067246875800000063', '销售部', 0, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dept(id, pid, pids, name, sort, del_flag, creator, create_date, updater, update_date) VALUES (1067246875800000067, 1067246875800000062, '1067246875800000065,1067246875800000062', '产品部', 1, 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);

INSERT INTO sys_dict_type(id, dict_type, dict_name, remark, sort, creator, create_date, updater, update_date) VALUES (1160061077912858625, 'gender', '性别', '', 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061112075464705, 1160061077912858625, '男', '0', '', 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061146967879681, 1160061077912858625, '女', '1', '', 1, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061190127267841, 1160061077912858625, '保密', '2', '', 2, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);

INSERT INTO sys_dict_type(id, dict_type, dict_name, remark, sort, creator, create_date, updater, update_date) VALUES (1225813644059140097, 'notice_type', '站内通知-类型', '', 1, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814069634195457, 1225813644059140097, '公告', '0', '', 0, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814107559092225, 1225813644059140097, '会议', '1', '', 1, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814271879340034, 1225813644059140097, '其他', '2', '', 2, 1067246875800000001, CURRENT_DATE, 1067246875800000001, CURRENT_DATE);

INSERT INTO sys_oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('renrenio', '', '$2a$10$aiWOK7GmRzyLyRihSF0cMedcbsEo0t11/y8f.tnBkF2hCeSd0Bq.i', 'all', 'authorization_code,password,implicit,client_credentials,refresh_token', 'https://www.renren.io/', NULL, 50000, NULL, NULL, 'true');

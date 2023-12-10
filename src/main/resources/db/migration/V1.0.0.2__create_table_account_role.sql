create table if not exists account_role(
  id serial not null,
  account_id integer,
  role_name text not null,
  primary key(id),
  unique(account_id, role_name)
)
;

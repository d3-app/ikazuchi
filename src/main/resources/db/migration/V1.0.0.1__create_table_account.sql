create table if not exists account(
  id serial not null,
  name text not null,
  password text not null,
  primary key(id)
)
;

create table if not exists reserve(
  id serial not null,
  reserve_date date not null,
  thing_id integer not null,
  account_id integer not null,
  primary key(id),
  unique(reserve_date, thing_id, account_id)
)
;

-- // account
delete from account
;
insert into account(
  name,
  password
)
select
  concat('A', to_char(generate_series(1, 9), 'FM009')) as name,
  '$2a$10$TpEvJQ.2K7lpsUL7HVeWJOppLnZX9JbIzfT5CxVSkzOVVHPZc0qmG' as password --// 'thunder'
;
insert into account(
  name,
  password
)
values(
  'ikazuchi',
  '$2a$10$TpEvJQ.2K7lpsUL7HVeWJOppLnZX9JbIzfT5CxVSkzOVVHPZc0qmG' --// 'thunder'
)
;
-- // account_role
insert into account_role(
  account_id,
  role_name
)
select id as account_id, 'ROLE_ADMIN' as role_name from account where name = 'ikazuchi';
;

-- // thing
delete from thing
;
insert into thing(
  name
)
select
  concat('T', to_char(generate_series(1, 9), 'FM009')) as name
;
-- // reserve
delete from reserve
;
insert into reserve(
  reserve_date,
  thing_id,
  account_id
)
select current_date as reserve_date, t.id as thing_id, a.id as account_id from thing t, account a where t.name = 'T001' and a.name = 'A001'
union select current_date, t.id, a.id from thing t, account a where t.name = 'T002' and a.name = 'A002'
union select current_date, t.id, a.id from thing t, account a where t.name = 'T003' and a.name = 'A003'
union select current_date+1, t.id, a.id from thing t, account a where t.name = 'T004' and a.name = 'A001'
union select current_date+1, t.id, a.id from thing t, account a where t.name = 'T005' and a.name = 'A002'
union select current_date+1, t.id, a.id from thing t, account a where t.name = 'T006' and a.name = 'A003'
union select current_date+2, t.id, a.id from thing t, account a where t.name = 'T007' and a.name = 'A001'
union select current_date+2, t.id, a.id from thing t, account a where t.name = 'T008' and a.name = 'A002'
union select current_date+2, t.id, a.id from thing t, account a where t.name = 'T009' and a.name = 'A003'
order by reserve_date, thing_id, account_id
;

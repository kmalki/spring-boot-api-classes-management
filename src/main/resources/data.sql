
insert into teacher (id, first_name, last_name, email)
values
(nextval('teacher_id_seq'), 'Sidney', 'Reeves', 'sidneyreeves@edu.com'),
(nextval('teacher_id_seq'), 'Maxine', 'Stone', 'maxinestone@edu.com'),
(nextval('teacher_id_seq'), 'Noelle', 'Cooper', 'noellecooper@edu.com'),
(nextval('teacher_id_seq'), 'Maisie', 'Davis', 'maisiedavis@edu.com'),
(nextval('teacher_id_seq'), 'Webster', 'Rowe', 'websterrowe@edu.com')
on conflict (email) do nothing;

with
disciplines as(
select * from
(select id,floor(random() * 5)::int as disc_id
from teacher
union all
select id,floor(random() * 5)::int as disc_id
from teacher) a
left join
(
select teacher_id, discipline
from teacher_discipline
) b
on a.id = b.teacher_id and a.disc_id = b.discipline
where b.teacher_id is null
)


insert into teacher_discipline (teacher_id, discipline)
select distinct id, disc_id
from disciplines;


insert into class (id, name)
values
(nextval('class_id_seq'), '6A'),
(nextval('class_id_seq'), '6B'),
(nextval('class_id_seq'), '5A'),
(nextval('class_id_seq'), '5B'),
(nextval('class_id_seq'), '4A'),
(nextval('class_id_seq'), '4B'),
(nextval('class_id_seq'), '3A'),
(nextval('class_id_seq'), '3B'),
(nextval('class_id_seq'), '2A'),
(nextval('class_id_seq'), '2B')
on conflict (name) do nothing;


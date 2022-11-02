
insert into teacher (id, first_name, last_name, email)
values
(nextval('teacher_id_seq'), 'Sidney', 'Reeves', 'sidneyreeves@edu.com'),
(nextval('teacher_id_seq'), 'Maxine', 'Stone', 'maxinestone@edu.com'),
(nextval('teacher_id_seq'), 'Noelle', 'Cooper', 'noellecooper@edu.com'),
(nextval('teacher_id_seq'), 'Maisie', 'Davis', 'maisiedavis@edu.com'),
(nextval('teacher_id_seq'), 'Webster', 'Rowe', 'websterrowe@edu.com');




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


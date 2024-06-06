insert into authors(full_name)
values ('Иван Андреевич Крылов'), ('Михаил Юрьевич Лермонтов'), ('Иван Сергеевич Тургенев');

insert into genres(name)
values ('Басня'), ('Баллада'), ('Роман');

insert into books(title, author_id, genre_id)
values ('Волк на псарне', 1, 1), ('Бородино', 2, 2), ('Отцы и дети', 3, 3);

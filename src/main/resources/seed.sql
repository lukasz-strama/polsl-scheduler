-- 1. Słowniki (Typy, Lokacje, Prowadzący)
INSERT INTO class_types (id, name, short_name) VALUES 
(1, 'Wykład', 'W'),
(2, 'Laboratorium', 'L'),
(3, 'Projekt', 'P'),
(4, 'Seminarium', 'S');

INSERT INTO locations (id, building, room) VALUES 
(1, 'Wydział AEI', 'Aula A'),
(2, 'Wydział AEI', 'Sala 305'),
(3, 'Wydział MT', 'Lab 12'),
(4, 'Zdalnie', 'Zoom');

INSERT INTO academic_teachers (id, academic_title, first_name, last_name) VALUES 
(1, 'dr inż.', 'Jan', 'Kowalski'),
(2, 'prof. dr hab.', 'Anna', 'Nowak'),
(3, 'mgr inż.', 'Piotr', 'Wiśniewski');

-- 2. Przedmioty
INSERT INTO subjects (id, name, acronym, color_hex) VALUES 
(1, 'Programowanie Obiektowe', 'POiW', '#FF5733'),
(2, 'Analiza Matematyczna', 'AM', '#33FF57'),
(3, 'Fizyka Techniczna', 'FIZ', '#3357FF');

-- 3. Zajęcia w Kalendarzu (Schedule Entries)
-- Poniedziałek, 8:30, Wykład z POiW
INSERT INTO schedule_entries (id, subject_id, location_id, type_id, day_of_week, start_time, end_time, week_parity) VALUES 
(1, 1, 1, 1, 1, '08:30', '10:00', 0);

-- Wtorek, 10:15, Laboratorium z Analizy (Co 2 tygodnie - TP)
INSERT INTO schedule_entries (id, subject_id, location_id, type_id, day_of_week, start_time, end_time, week_parity) VALUES 
(2, 2, 2, 2, 2, '10:15', '11:45', 2);

-- Środa, 14:00, Projekt z POiW
INSERT INTO schedule_entries (id, subject_id, location_id, type_id, day_of_week, start_time, end_time, week_parity) VALUES 
(3, 1, 3, 3, 3, '14:00', '15:30', 0);

-- 4. Przypisanie Prowadzących (Relacja N:M)
-- Do wykładu z POiW (ID=1) przypisz Kowalskiego (ID=1)
INSERT INTO schedule_teachers (schedule_id, teacher_id) VALUES (1, 1);

-- Do laborki z Analizy (ID=2) przypisz Nowak (ID=2)
INSERT INTO schedule_teachers (schedule_id, teacher_id) VALUES (2, 2);

-- Do projektu z POiW (ID=3) przypisz OBU (Kowalski + Wiśniewski) - 2 prowadzących
INSERT INTO schedule_teachers (schedule_id, teacher_id) VALUES (3, 1);
INSERT INTO schedule_teachers (schedule_id, teacher_id) VALUES (3, 3);
-- 1. Słownik Typów Zajęć
CREATE TABLE IF NOT EXISTS class_types (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,  -- "Wykład", "Laboratorium", "Ćwiczenia"
    short_name TEXT NOT NULL    -- "W", "L", "Ć"
);

-- 2. Słownik Lokalizacji
CREATE TABLE IF NOT EXISTS locations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    building TEXT NOT NULL,
    room TEXT NOT NULL,
    CONSTRAINT location_unique UNIQUE (building, room)
);

-- 3. Słownik Prowadzących
CREATE TABLE IF NOT EXISTS academic_teachers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    academic_title TEXT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    CONSTRAINT teacher_unique UNIQUE (first_name, last_name)
);

-- 4. Tabela Przedmiotów
CREATE TABLE IF NOT EXISTS subjects (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    acronym TEXT,
    color_hex TEXT DEFAULT '#CCCCCC'
);

-- 5. Główna Tabela Planu
CREATE TABLE IF NOT EXISTS schedule_entries (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    subject_id INTEGER NOT NULL,
    location_id INTEGER,
    type_id INTEGER NOT NULL,
    
    day_of_week INTEGER NOT NULL CHECK (day_of_week BETWEEN 1 AND 7),
    start_time TEXT NOT NULL CHECK (length(start_time) = 5),
    end_time TEXT NOT NULL CHECK (length(end_time) = 5),
    week_parity INTEGER NOT NULL DEFAULT 0 CHECK (week_parity IN (0, 1, 2)), -- 0=Both, 1=Odd, 2=Even

    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE SET NULL,
    FOREIGN KEY (type_id) REFERENCES class_types(id) ON DELETE RESTRICT,
    
    CONSTRAINT time_logic CHECK (end_time > start_time)
);

-- 6.Tabela Łącząca (Wiele prowadzących do jednych zajęć)
CREATE TABLE IF NOT EXISTS schedule_teachers (
    schedule_id INTEGER NOT NULL,
    teacher_id INTEGER NOT NULL,
    PRIMARY KEY (schedule_id, teacher_id), -- Para musi być unikalna
    FOREIGN KEY (schedule_id) REFERENCES schedule_entries(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES academic_teachers(id) ON DELETE CASCADE
);

-- 7. Widok Pełnego Planu Zajęć
CREATE VIEW IF NOT EXISTS full_schedule_view AS
SELECT 
    s.id AS entry_id,
    sub.name AS subject_name,
    sub.color_hex,
    ct.short_name AS type,
    l.building || ' ' || l.room AS location,
    s.day_of_week,
    s.start_time,
    s.end_time,
    s.week_parity,
    -- Nazwiska w jednym polu, oddzielone przecinkami
    GROUP_CONCAT(t.academic_title || ' ' || t.last_name, ', ') AS teachers
FROM schedule_entries s
JOIN subjects sub ON s.subject_id = sub.id
JOIN class_types ct ON s.type_id = ct.id
LEFT JOIN locations l ON s.location_id = l.id
LEFT JOIN schedule_teachers st ON s.id = st.schedule_id
LEFT JOIN academic_teachers t ON st.teacher_id = t.id
GROUP BY s.id;
-- Миграция: Добавление столбцов context и scheduled_at в таблицу requests

-- Добавление столбца context
ALTER TABLE requests
ADD COLUMN context TEXT;

-- Добавление столбца scheduled_at
ALTER TABLE requests
ADD COLUMN scheduled_at TIMESTAMPTZ;
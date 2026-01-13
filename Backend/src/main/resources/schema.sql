-- Create crowd_data table if it doesn't exist
CREATE TABLE IF NOT EXISTS crowd_data (
    id BIGSERIAL PRIMARY KEY,
    temperature_celsius DECIMAL(5,2) NOT NULL,
    total_people_count INTEGER NOT NULL,
    entry_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index on entry_time for better query performance
CREATE INDEX IF NOT EXISTS idx_crowd_data_entry_time ON crowd_data (entry_time DESC);

-- Create index on temperature_celsius for range queries
CREATE INDEX IF NOT EXISTS idx_crowd_data_temperature ON crowd_data (temperature_celsius);

-- Create index on total_people_count for range queries
CREATE INDEX IF NOT EXISTS idx_crowd_data_people_count ON crowd_data (total_people_count);



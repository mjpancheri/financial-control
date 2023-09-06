CREATE TABLE income_summaries (
    id UUID PRIMARY KEY UNIQUE NOT NULL,
    user_id UUID REFERENCES users(id) NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    amount DECIMAL(10, 2) DEFAULT 0,
    type VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
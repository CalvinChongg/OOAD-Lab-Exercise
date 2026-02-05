package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {

    private static final String URL = "jdbc:sqlite:database.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        // 1. Users Table
        String createUserTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                roles TEXT NOT NULL
            );
            """;

        // 2. Submissions Table
        String createSubmissionsTable = """
            CREATE TABLE IF NOT EXISTS submissions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                student_id INTEGER NOT NULL,
                research_title TEXT NOT NULL,
                abstract TEXT NOT NULL,
                supervisor_name TEXT,
                presentation_type TEXT CHECK(presentation_type IN ('ORAL', 'POSTER', 'oral', 'poster')),
                file_path TEXT,
                submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                status TEXT DEFAULT 'PENDING',
                FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
            );
            """;

        // 3. Sessions Table
        String createSessionsTable = """
            CREATE TABLE IF NOT EXISTS sessions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                date DATE NOT NULL,
                time TEXT,
                type TEXT CHECK(type IN ('ORAL', 'POSTER', 'oral', 'poster')),
                venue TEXT,
                max_presentations INTEGER DEFAULT 10,
                status TEXT DEFAULT 'PLANNED'
            );
            """;

        // 4. Session Assignments Table
        String createSessionAssignmentsTable = """
            CREATE TABLE IF NOT EXISTS session_assignments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                session_id INTEGER NOT NULL,
                submission_id INTEGER NOT NULL,
                presentation_order INTEGER,
                evaluator_id INTEGER,
                FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE,
                FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE,
                FOREIGN KEY (evaluator_id) REFERENCES users(id) ON DELETE SET NULL
            );
            """;

        // 5. Evaluations Table
        String createEvaluationsTable = """
            CREATE TABLE IF NOT EXISTS evaluations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                submission_id INTEGER NOT NULL,
                evaluator_id INTEGER NOT NULL,
                problem_clarity INTEGER CHECK(problem_clarity BETWEEN 1 AND 10),
                methodology INTEGER CHECK(methodology BETWEEN 1 AND 10),
                results INTEGER CHECK(results BETWEEN 1 AND 10),
                presentation_quality INTEGER CHECK(presentation_quality BETWEEN 1 AND 10),
                overall_score REAL,
                comments TEXT,
                evaluation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE,
                FOREIGN KEY (evaluator_id) REFERENCES users(id) ON DELETE CASCADE,
                UNIQUE(submission_id, evaluator_id)
            );
            """;

        // 6. Awards Table
        String createAwardsTable = """
            CREATE TABLE IF NOT EXISTS awards (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                award_type TEXT CHECK(award_type IN ('BEST_ORAL', 'BEST_POSTER', 'PEOPLES_CHOICE')),
                submission_id INTEGER NOT NULL,
                winner_id INTEGER NOT NULL,
                ceremony_date DATE,
                FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE,
                FOREIGN KEY (winner_id) REFERENCES users(id) ON DELETE CASCADE
            );
            """;

        // Seed Users with consistent roles
        String seedUsers = """
            INSERT OR IGNORE INTO users (username, password, roles) VALUES
            ('ali', '123', 'student'),
            ('abu', '123', 'student'),
            ('coordinator', 'coordinator123', 'coordinator'),
            ('evaluator', 'evaluator123', 'evaluator');
            """;

        try (java.sql.Connection conn = connect();
            java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(createUserTable);
            stmt.execute(createSubmissionsTable);
            stmt.execute(createSessionsTable);
            stmt.execute(createSessionAssignmentsTable);
            stmt.execute(createEvaluationsTable);
            stmt.execute(createAwardsTable);
            stmt.execute(seedUsers);
            System.out.println("Database reset and initialized successfully.");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
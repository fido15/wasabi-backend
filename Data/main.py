import sqlite3

def create_connection(db_name):
    """Crée une connexion à la base de données SQLite."""
    conn = sqlite3.connect(db_name)
    print(f"Connexion établie à '{db_name}'")
    return conn

def create_tables(conn):
    """Crée les tables Sites, RSS_data, et Secteur."""
    cursor = conn.cursor()
    
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS Sites (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            url TEXT NOT NULL,
            faiblesse TEXT
        );
    """)
    
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS RSS_data (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT,
            link TEXT,
            published TEXT,
            updated TEXT,
            summary TEXT
        );
    """)
    
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS Secteur (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            categorie TEXT NOT NULL
        );
    """)

    print("Tables créées avec succès.")
    conn.commit()

def main():
    db_name = "scraping_data.db"
    
    conn = create_connection(db_name)
    
    create_tables(conn)
    
    conn.close()
    print("Connexion fermée.")

if __name__ == "__main__":
    main()

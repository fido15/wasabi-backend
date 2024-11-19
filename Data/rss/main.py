import feedparser
import time
import sqlite3

def parse(url):
    try:
        feed = feedparser.parse(url)
        return feed
    except Exception as e:
        print(f"Error parsing the feed: {e}")
        return None

                                                                                                                                                                                                                                                                                                            
conn = sqlite3.connect('rss_feed.db')
cursor = conn.cursor()
                                                                                                                                   

cursor.execute('''CREATE TABLE IF NOT EXISTS feed_entries (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT,
                    link TEXT,
                    published TEXT,
                    updated TEXT,
                    summary TEXT
                )''')

with open('link.txt', 'r') as file:
    urls = file.readlines()
    urls = [url.strip() for url in urls]

for url in urls:
    for attempt in range(5):
        feed = parse(url)
        if feed:
            break
        print(f"Retrying... ({attempt + 1}/5)")
        time.sleep(2)

    if feed:
        for entry in feed.entries:
            title = entry.title
            link = entry.link
            published = entry.published if hasattr(entry, 'published') else "Not available"
            updated = entry.updated if hasattr(entry, 'updated') else "Not available"
            summary = entry.summary if hasattr(entry, 'summary') else "Not available"

            

            cursor.execute('''INSERT INTO feed_entries (title, link, published, updated, summary) 
                              VALUES (?, ?, ?, ?, ?)''', (title, link, published, updated, summary))

conn.commit()
conn.close()

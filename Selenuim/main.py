from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# Fonction pour récupérer des informations à partir d'une URL
def scrape_info(url):
    # Configurer ChromeDriver avec des options
    chrome_options = Options()
    chrome_options.add_argument("--headless")  # Mode sans tête, optionnel si vous voulez juste récupérer des données
    service = Service(executable_path="path/to/chromedriver")  # Chemin vers votre ChromeDriver

    # Initialiser le navigateur
    driver = webdriver.Chrome(service=service, options=chrome_options)

    try:
        # Ouvrir l'URL
        driver.get(url)
        
        # Attendre que l'élément soit présent (par exemple, attendre un titre ou un élément spécifique)
        wait = WebDriverWait(driver, 10)
        element = wait.until(EC.presence_of_element_located((By.TAG_NAME, "h1")))  # Ici on attend un h1

        # Extraire des informations (par exemple, titre de la page et première balise h1)
        page_title = driver.title
        h1_text = driver.find_element(By.TAG_NAME, "h1").text

        # Afficher les informations
        print(f"Titre de la page: {page_title}")
        print(f"Premier h1: {h1_text}")

        # Vous pouvez récupérer d'autres informations en fonction de la structure du site
        # Exemple : liens, descriptions, etc.
        
        # Extraire tous les liens présents sur la page
        links = driver.find_elements(By.TAG_NAME, "a")
        for link in links:
            print(link.get_attribute('href'))  # Affiche les URLs des liens
          
    except Exception as e:
        print(f"Erreur lors du scraping : {e}")
    finally:
        # Fermer le navigateur
        driver.quit()

# Exemple d'URL
url = "https://example.com"

# Appeler la fonction pour récupérer les infos à partir de l'URL donnée
scrape_info(url)

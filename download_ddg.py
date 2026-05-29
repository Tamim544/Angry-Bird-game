from duckduckgo_search import DDGS
import urllib.request
import os

assets_dir = 'assets'
if not os.path.exists(assets_dir):
    os.makedirs(assets_dir)

queries = {
    'red_bird.png': 'angry birds red bird transparent sprite png',
    'green_pig.png': 'angry birds minion pig transparent sprite png',
    'yellow_bird.png': 'angry birds chuck yellow bird transparent png',
    'wood_block.jpg': 'wooden crate texture seamless tile',
    'stone_block.jpg': 'stone block texture seamless tile',
    'background.jpg': 'angry birds background sky grass seamless'
}

req_headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'}

with DDGS() as ddgs:
    for filename, query in queries.items():
        try:
            print(f"Searching for {filename}...")
            results = list(ddgs.images(query, max_results=1))
            if results:
                url = results[0]['image']
                print(f"Downloading {url}...")
                req = urllib.request.Request(url, headers=req_headers)
                with urllib.request.urlopen(req, timeout=10) as response:
                    with open(os.path.join(assets_dir, filename), 'wb') as f:
                        f.write(response.read())
            else:
                print(f"No results for {filename}")
        except Exception as e:
            print(f"Failed to download {filename}: {e}")


import urllib.request
import os

assets_dir = 'assets'
if not os.path.exists(assets_dir):
    os.makedirs(assets_dir)

images = {
    'red_bird.png': 'https://upload.wikimedia.org/wikipedia/en/3/36/Red%2C_Angry_Birds.png',
    'green_pig.png': 'https://upload.wikimedia.org/wikipedia/en/e/e4/Minion_Pig.png',
    'yellow_bird.png': 'https://upload.wikimedia.org/wikipedia/en/0/0f/Chuck%2C_Angry_Birds.png',
    'wood_block.jpg': 'https://upload.wikimedia.org/wikipedia/commons/4/4e/Wood_texture.jpg',
    'stone_block.jpg': 'https://upload.wikimedia.org/wikipedia/commons/d/d4/Brick_wall_texture.jpg',
    'background.jpg': 'https://upload.wikimedia.org/wikipedia/commons/1/1d/Clouds-landscape-background-200.jpg'
}

req_headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'}

for filename, url in images.items():
    try:
        print(f"Downloading {filename}...")
        req = urllib.request.Request(url, headers=req_headers)
        with urllib.request.urlopen(req) as response:
            with open(os.path.join(assets_dir, filename), 'wb') as f:
                f.write(response.read())
    except Exception as e:
        print(f"Failed to download {filename}: {e}")


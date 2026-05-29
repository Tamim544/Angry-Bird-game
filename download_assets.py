import urllib.request
import json
import os

# Create assets directory if not exists
assets_dir = 'core/src/main/resources/assets'
# In LibGDX desktop projects, assets might be in 'assets' directory at project root or 'core/assets'
# Let's check where it actually is

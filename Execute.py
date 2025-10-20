import qiskit
import json
from pathlib import Path


circuitDirectory = Path('Circuits/OutputCircuits')
json_file_paths = []
json_objects = []

if circuitDirectory.exists() and circuitDirectory.is_dir():
    for p in circuitDirectory.iterdir():
        if p.is_file() and p.suffix.lower() == '.json':
            json_file_paths.append(str(p))
            try:
                with p.open('r', encoding='utf-8') as f:
                    json_objects.append(json.load(f))
            except Exception:
                continue
json_objects[0].get('name')

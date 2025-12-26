from __future__ import annotations
import json
from pathlib import Path
from typing import Any, Optional
import tempfile
import os

"""
Utility helpers for reading and writing JSON files.

Place this file at:
/Users/maxblanksby/Desktop/quantum work/code/QuantumLink/PythonUtil/Util.py
"""



def load_json(path: str | Path, default: Optional[Any] = None, encoding: str = "utf-8") -> Any:
    """
    Read and parse a JSON file.
    Returns `default` if the file does not exist.
    Raises ValueError on invalid JSON.
    """
    p = Path(path)
    if not p.exists():
        return default
    try:
        with p.open("r", encoding=encoding) as f:
            return json.load(f)
    except json.JSONDecodeError as e:
        raise ValueError(f"Invalid JSON in {p}: {e}") from e


def save_json(path: str | Path, data: Any, indent: Optional[int] = 2, ensure_ascii: bool = False,
              encoding: str = "utf-8") -> None:
    """
    Write JSON to `path` atomically (writes to a temp file then replaces).
    Creates parent directories if needed.
    """
    p = Path(path)
    if not p.parent.exists():
        p.parent.mkdir(parents=True, exist_ok=True)

    # write to a temp file in the same directory then atomically replace
    fd, tmp_path = tempfile.mkstemp(prefix=p.name, dir=str(p.parent))
    try:
        with os.fdopen(fd, "w", encoding=encoding) as tmpf:
            json.dump(data, tmpf, indent=indent, ensure_ascii=ensure_ascii)
            tmpf.flush()
            os.fsync(tmpf.fileno())
        os.replace(tmp_path, p)
    finally:
        # cleanup if something went wrong and temp file still exists
        try:
            if Path(tmp_path).exists():
                Path(tmp_path).unlink()
        except Exception:
            pass


# Convenience alias
read_json = load_json
write_json = save_json


if __name__ == "__main__":
    # quick demo (won't run when imported)
    sample = {"name": "QuantumLink", "active": True}
    demo_path = Path.cwd() / "demo.json"
    save_json(demo_path, sample)
    print("Wrote:", read_json(demo_path)) 